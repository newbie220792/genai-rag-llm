package com.document.module.services;

import com.document.module.entity.LoadDocumentLog;
import com.document.module.enums.LoadDocStatus;
import com.document.module.respository.LoadingDocumentLogRepository;
import org.springframework.stereotype.Service;

@Service
public class LoadDocLogService {
    private final LoadingDocumentLogRepository loadingDocumentLogRepository;

    public LoadDocLogService(LoadingDocumentLogRepository loadingDocumentLogRepository) {
        this.loadingDocumentLogRepository = loadingDocumentLogRepository;
    }

    private void log(String docId, String message, LoadDocStatus status, String fileName, String filePath) {
        LoadDocumentLog loadDocumentLog = new LoadDocumentLog();
        loadDocumentLog.setMessage(message);
        loadDocumentLog.setStatus(status.name());
        loadDocumentLog.setDocId(docId);
        loadDocumentLog.setFileName(fileName);
        loadDocumentLog.setFilePath(filePath);
        loadingDocumentLogRepository.saveAndFlush(loadDocumentLog);
    }

    private void logInfo(String docId, String message, String fileName, String filePath) {
        LoadDocumentLog loadDocumentLog = loadingDocumentLogRepository.findLoadDocumentLogByDocId(docId);
        if (loadDocumentLog == null) {
            log(docId, message, LoadDocStatus.IN_PROGRESS, fileName, filePath);
        } else {
            loadDocumentLog.setStatus(LoadDocStatus.IN_PROGRESS.name());
        }
        loadingDocumentLogRepository.saveAndFlush(loadDocumentLog);
    }
}
