package com.document.module.readers;

import org.springframework.ai.document.Document;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface IReader {
    List<Document> loadDocument(File document);

    default String getDocId() {
        return UUID.randomUUID().toString();
    }
}
