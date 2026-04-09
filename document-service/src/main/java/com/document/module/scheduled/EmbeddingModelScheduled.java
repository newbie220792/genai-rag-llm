package com.document.module.scheduled;


import com.document.module.entity.ChunkText;
import com.document.module.exception.DocumentException;
import com.document.module.models.ChunkTextReq;
import com.document.module.models.EmbeddingModelRes;
import com.document.module.services.DocumentServiceImpl;
import com.document.module.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class EmbeddingModelScheduled {
    private static final Logger log = LoggerFactory.getLogger(EmbeddingModelScheduled.class);
    @Value("${document.folder.path}")
    private String documentFolderPath;
    @Value("${schedule.enable}")
    private String isEnabled;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${embedding.service.url}")
    private String embeddingServiceUrl;

    @Autowired
    private DocumentServiceImpl documentService;


    @Scheduled(fixedRate = 60000)
    public void embeddingModelScheduled() throws FileNotFoundException {
        if (!isEnabled.equals("true")) {
            return;
        }
        File folder = new File(documentFolderPath);
        if (folder.exists() && folder.isDirectory()) {
            // load file one by one
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                try (BufferedReader br = Files.newBufferedReader(Path.of(file.getAbsolutePath()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        // tokenize
                        DocumentTransformer splitter = new TokenTextSplitter();
                        List<Document> documents = splitter.apply(List.of(new Document(line)));

                        // save
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        } else {
            log.warn("Acknowledge is not available for embedding model");
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
}
