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

    public void log(String message, LoadDocStatus status, String startTime, String endTime) {
        LoadDocumentLog loadDocumentLog = new LoadDocumentLog();
        loadDocumentLog.setMessage(message);
        loadDocumentLog.setStartTime(startTime);
        loadDocumentLog.setEndTime(endTime);
        loadDocumentLog.setStatus(status.name());
        loadingDocumentLogRepository.save(loadDocumentLog);
    }
}
