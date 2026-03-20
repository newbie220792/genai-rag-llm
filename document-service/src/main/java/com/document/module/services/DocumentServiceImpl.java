package com.document.module.services;

import com.document.module.exception.DocumentException;
import com.document.module.models.EmbeddingModelRes;
import com.document.module.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@Service
public class DocumentServiceImpl implements IDocumentService {
    private static final Logger log = LogManager.getLogger(DocumentServiceImpl.class);

    private final RestTemplate restTemplate;

    public DocumentServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

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

            // 2. chunking
            TokenTextSplitter tokenTextSplitter = new TokenTextSplitter(
                    20,
                    400,
                    10,
                    5000,
                    true,
                    List.of('.', '?', '!', '\n', '-'));
            documents = tokenTextSplitter.apply(documents);

            // 3. call embedding model service
            ResponseEntity<String> res = restTemplate.postForEntity("http://localhost:8080/api/v1/embedding",
                    Map.of("messages", List.of("test")),
                    String.class,
                    Map.of("Content-Type", "application/json"));
            Map<String, EmbeddingModelRes> map = GsonUtils.fromJson(res.getBody(), new TypeToken<Map<String, EmbeddingModelRes>>() {
            }.getType());
            EmbeddingModelRes embeddingModelRes = map.get("test");

            // 4. save chunks db

        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private List<Document> loadingFile(MultipartFile document) {
        TextReader textReader = new TextReader(document.getResource());
        textReader.getCustomMetadata().put("filename", document.getOriginalFilename());
        return textReader.read();
    }
}
