package com.document.module.services;

import com.document.module.entity.ChunkText;
import com.document.module.exception.DocumentException;
import com.document.module.models.ChunkTextReq;
import com.document.module.models.EmbeddingModelRes;
import com.document.module.responsitory.ChunkTextRepository;
import com.document.module.retrievers.IVectorSearch;
import com.document.module.retrievers.NativeRetriever;
import com.document.module.tokenizer.TokenSectionSplitter;
import com.document.module.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DocumentServiceImpl implements IDocumentService {
    private static final Logger log = LogManager.getLogger(DocumentServiceImpl.class);

    private final RestTemplate restTemplate;
    private final ChunkTextRepository chunkTextRepository;
    private final IVectorSearch vectorStore;

    public DocumentServiceImpl(RestTemplate restTemplate,
                               ChunkTextRepository chunkTextRepository,
                               EmbeddingModel embeddingModel,
                               JdbcTemplate jdbcTemplate) {
        this.restTemplate = restTemplate;
        this.chunkTextRepository = chunkTextRepository;
        this.vectorStore = new NativeRetriever(jdbcTemplate, embeddingModel);
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
     * @param document the {@link File} object representing the document to be loaded.
     *                 Must be a valid, existing file.
     * @throws IllegalArgumentException if the document does not exist or is not a file
     * @throws Exception                if any error occurs during document loading
     */
    @Override
    public void loadingDocument(MultipartFile document) throws Exception {
        if (document.isEmpty()) {
            throw new DocumentException("The document is not a valid file or is not a regular file (not supported by the OS).");
        }
        try {
            // 1. loading a document and convert to csv
            List<Document> documents = loadingFile(document);

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

            // 3. call embedding model service
            ResponseEntity<String> res = restTemplate.postForEntity(embeddingServiceUrl + "/api/v1/embedding-document",
                    Map.of("documents", chunkTextReqs),
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

            List<ChunkText> chunkTexts = new ArrayList<>();
            documents.forEach(doc -> {
                EmbeddingModelRes embeddingModelRes = map.get(doc.getId());
                ChunkText chunkText = new ChunkText();
                chunkText.setContent(doc.getText());
                chunkText.setEmbedding(embeddingModelRes.getResult().getOutput());
                chunkText.setTitle(doc.getId());
                chunkText.setMetadata(GsonUtils.toJson(embeddingModelRes.getMetadata()));
                chunkTexts.add(chunkText);
            });

            // 4. save chunks db
            chunkTextRepository.saveAll(chunkTexts);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    /**
     * @param userPrompt String
     * @return docs List<Document>
     */
    @Override
    public List<Document> searchVector(String userPrompt) {
        return vectorStore.search(userPrompt);
    }

    private List<Document> loadingFile(MultipartFile document) {
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(document.getResource(),
                ExtractedTextFormatter.builder()
                        .withLeftAlignment(true)
                        .build());
        return tikaDocumentReader.read();
    }
}
