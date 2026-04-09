package com.document.module.scheduled;


import com.document.module.exception.DocumentException;
import com.document.module.services.DocumentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

@Component
public class EmbeddingModelScheduled {
    private static final Logger log = LoggerFactory.getLogger(EmbeddingModelScheduled.class);
    @Value("${document.folder.path}")
    private String documentFolderPath;
    @Value("${schedule.enable}")
    private String isEnabled;
    private final DocumentServiceImpl documentService;

    public EmbeddingModelScheduled(DocumentServiceImpl documentService) {
        this.documentService = documentService;
    }

    @Scheduled(fixedDelay = 1000)
    public void embeddingModelScheduled() {
        if (!isEnabled.equals("true")) {
            return;
        }
        File folder = new File(documentFolderPath);
        if (folder.exists() && folder.isDirectory()) {
            // load file one by one
            for (File file : Objects.requireNonNull(folder.listFiles())) {
                try {
                    documentService.loadingDocument(file);
                } catch (Exception e) {
                    log.error("Error while processing the file: {}", e.getMessage());
                    throw new DocumentException(e.getMessage());
                }
            }
        } else {
            log.warn("Acknowledge is not available for embedding model");
        }
    }
}
