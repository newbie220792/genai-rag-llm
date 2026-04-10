package com.document.module.services;

import com.document.module.entity.ChunkText;
import com.document.module.exception.DocumentException;
import com.document.module.models.ChunkTextReq;
import com.document.module.models.EmbeddingModelRes;
import com.document.module.readers.*;
import com.document.module.respository.ChunkTextRepository;
import com.document.module.retrievers.HybridSearch;
import com.document.module.retrievers.ISearch;
import com.document.module.retrievers.SemanticSearch;
import com.document.module.tokenizer.TokenSectionSplitter;
import com.document.module.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.document.id.RandomIdGenerator;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DocumentServiceImpl implements IDocumentService {
    private static final Logger log = LogManager.getLogger(DocumentServiceImpl.class);

    private final RestTemplate restTemplate;
    private final ChunkTextRepository chunkTextRepository;
    private final int MAX_CHUNK_TEXT_SIZE = 100;
    private final long MAX_FILE_SIZE = 20 * 1048576;
    private final JdbcTemplate jdbcTemplate;
    private final EmbeddingModel embeddingModel;

    public DocumentServiceImpl(RestTemplate restTemplate,
                               ChunkTextRepository chunkTextRepository,
                               EmbeddingModel embeddingModel,
                               JdbcTemplate jdbcTemplate) {
        this.restTemplate = restTemplate;
        this.chunkTextRepository = chunkTextRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.embeddingModel = embeddingModel;
    }

    @Value("${embedding.service.url}")
    private String embeddingServiceUrl;

    /**
     * Loads a document from the file system.
     * <p>
     * This method validates the existence and type of the provided document file
     * before attempting to load it. If the file does not exist or is not a regular
     * file, an exception will be thrown.
     * </p>
     *
     * @param document the {@link MultipartFile} object representing the document to be loaded.
     *                 Must be a valid, existing file.
     * @throws IllegalArgumentException if the document does not exist or is not a file
     */
    @Override
    public void loadingDocument(MultipartFile document) {
        if (document.isEmpty()) {
            throw new DocumentException("The document is not a valid file or is not a regular file (not supported by the OS).");
        }
        try {
            // 1. loading a document and convert to csv
            List<Document> documents = loadingFile(document);
            processingDocument(documents);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * @param document the {@link File} object representing the document to be loaded.
     *                 Must be a valid, existing file.
     * @throws Exception if any error occurs during document loading
     */
    @Override
    public void loadingDocument(File document) throws Exception {
        if (!document.exists()) {
            throw new DocumentException("The document is not a valid file or is not a regular file (not supported by the OS).");
        }
        try {
            // 1. loading a document and convert to csv
            List<Document> documents = loadingFile(document);
            processingDocument(documents);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private void processingDocument(List<Document> documents) {

        // 2. chunking text
        DocumentTransformer documentTransformer = new TokenSectionSplitter(500, 50);
        documents = documentTransformer.apply(documents);

        List<ChunkTextReq> chunkTextReqs = new ArrayList<>();

        documents.forEach(doc -> {
            ChunkTextReq chunkTextReq = new ChunkTextReq();
            chunkTextReq.setId(doc.getId());
            chunkTextReq.setText(doc.getText());
            chunkTextReq.setMetadata(doc.getMetadata());
            chunkTextReqs.add(chunkTextReq);
        });

        String documentId = new RandomIdGenerator().generateId();
        List<ChunkText> chunkTexts = new ArrayList<>();
        if (chunkTextReqs.size() <= MAX_CHUNK_TEXT_SIZE) {
            chunkTexts.addAll(getChunkTextReqs(documentId, chunkTextReqs));
        } else {
            int i = 0;
            while (i <= chunkTextReqs.size()) {
                chunkTexts.addAll(getChunkTextReqs(documentId, chunkTextReqs.subList(i, i + MAX_CHUNK_TEXT_SIZE)));
                if (i >= chunkTextReqs.size() - MAX_CHUNK_TEXT_SIZE) {
                    break;
                }
                i += MAX_CHUNK_TEXT_SIZE;
            }
            chunkTexts.addAll(getChunkTextReqs(documentId, chunkTextReqs.subList(i, chunkTextReqs.size())));
        }

        // 3. save chunks db
        chunkTextRepository.saveAll(chunkTexts);
    }

    private List<ChunkText> getChunkTextReqs(String documentId, List<ChunkTextReq> chunkTextReqs) {
        List<ChunkText> chunkTexts = new ArrayList<>();
        ResponseEntity<String> res = restTemplate.postForEntity(embeddingServiceUrl + "/api/v1/embedding-document",
                Map.of("documents", chunkTextReqs, "documentId", documentId),
                String.class);

        String body = res.getBody();
        if (body == null) {
            throw new DocumentException("Body in invalid format.");
        }
        Map<String, EmbeddingModelRes> map = GsonUtils.fromJson(res.getBody(), new TypeToken<Map<String, EmbeddingModelRes>>() {
        }.getType());

        if (map == null) {
            throw new DocumentException("Body in invalid format.");
        }

        map.forEach((k, v) -> {
            v.getResults().forEach(r -> {
                ChunkText chunkText = new ChunkText();
                chunkText.setContent(chunkTextReqs.get(r.getIndex()).getText());
                chunkText.setEmbedding(r.getOutput());
                chunkText.setTitle(k);
                chunkText.setMetadata(GsonUtils.toJson(r.getMetadata()));
                chunkTexts.add(chunkText);
            });
        });
        return chunkTexts;
    }

    /**
     * @param userPrompt String
     * @return docs List<Document>
     */
    @Override
    public List<Document> searchVector(String userPrompt) {
        ISearch vectorSearch = new SemanticSearch(jdbcTemplate, embeddingModel);
        return vectorSearch.search(userPrompt);
    }

    @Override
    public List<Document> hybridSearch(String userPrompt) {
        ISearch hybridSearch = new HybridSearch(chunkTextRepository, jdbcTemplate, embeddingModel);
        return hybridSearch.search(userPrompt);
    }

    public List<Document> loadingFile(MultipartFile document) {
        try {
            return loadingFile(document.getResource().getFile());
        } catch (IOException e) {
            throw new DocumentException(e);
        }
    }

    public List<Document> loadingFile(File document) {
        String fileName = document.getName();
        String extension = "";
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            extension = fileName.substring(lastDotIndex + 1);
        }
        IReader reader = null;
        if (document.length() <= MAX_FILE_SIZE) {
            reader = new TikaReader();
        } else {
            reader = switch (extension) {
                case "pdf" -> new PdfReader();
                case "docx", "doc" -> new WordReader();
                case "json" -> new JsonReader();
                case "txt" -> new TextReader();
                case "md" -> new MarkdownReader();
                case "html" -> new JSoupCustomerReader();
                case "xlsx", "xls" -> new ExcelReader();
                default -> throw new DocumentException("File extension not supported: " + extension);
            };
        }
        return reader.loadDocument(document);
    }
}
