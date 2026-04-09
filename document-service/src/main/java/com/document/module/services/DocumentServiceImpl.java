package com.document.module.services;

import com.document.module.entity.ChunkText;
import com.document.module.exception.DocumentException;
import com.document.module.models.ChunkTextReq;
import com.document.module.models.EmbeddingModelRes;
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
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class DocumentServiceImpl implements IDocumentService {
    private static final Logger log = LogManager.getLogger(DocumentServiceImpl.class);

    private final RestTemplate restTemplate;
    private final ChunkTextRepository chunkTextRepository;
    private final ISearch vectorStore;
    private final int MAX_CHUNK_TEXT_SIZE = 100;

    public DocumentServiceImpl(RestTemplate restTemplate,
                               ChunkTextRepository chunkTextRepository,
                               EmbeddingModel embeddingModel,
                               JdbcTemplate jdbcTemplate) {
        this.restTemplate = restTemplate;
        this.chunkTextRepository = chunkTextRepository;
        this.vectorStore = new SemanticSearch(jdbcTemplate, embeddingModel);
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
            ExecutorService executorService = Executors.newCachedThreadPool();

            List<ChunkText> chunkTexts = new CopyOnWriteArrayList<>();

            for (int i = 0; i < chunkTextReqs.size(); i += MAX_CHUNK_TEXT_SIZE) {
                int finalI = i;
                Future<List<ChunkText>> futures = executorService.submit(() ->
                        getChunkTextReqs(chunkTextReqs.subList(finalI, finalI + MAX_CHUNK_TEXT_SIZE)));
                chunkTexts.addAll(futures.get());
            }

            // 4. save chunks db
            chunkTextRepository.saveAll(chunkTexts);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private List<ChunkText> getChunkTextReqs(List<ChunkTextReq> chunkTextReqs) {
        List<ChunkText> chunkTexts = new ArrayList<>();
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

        map.forEach((k, v) -> {
            ChunkText chunkText = new ChunkText();
            chunkText.setContent(v.getResult().toString());
            chunkText.setEmbedding(v.getResult().getOutput());
            chunkText.setTitle(k);
            chunkText.setMetadata(GsonUtils.toJson(v.getMetadata()));
            chunkTexts.add(chunkText);
        });
        return chunkTexts;
    }

    /**
     * @param userPrompt String
     * @return docs List<Document>
     */
    @Override
    public List<Document> searchVector(String userPrompt) {
        return vectorStore.search(userPrompt);
    }

    @Override
    public List<Document> hybridSearch(String userPrompt) {
        ISearch hybridSearch = new HybridSearch(chunkTextRepository);
        return hybridSearch.search(userPrompt);
    }

    private List<Document> loadingFile(MultipartFile document) {
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(document.getResource(),
                ExtractedTextFormatter.builder()
                        .withLeftAlignment(true)
                        .build());
        return tikaDocumentReader.read();
    }

    private List<Document> loadingLargeFile(File document) throws FileNotFoundException {
        try (FileInputStream fis = new FileInputStream(document)) {
//            BufferedInputStream bis = new BufferedInputStream(fis);
            List<Document> documents = new ArrayList<>(MAX_CHUNK_TEXT_SIZE);
            for (int i = 0; i < document.length(); i++) {

                documents.add(new Document(new String(fis.read)))
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
