package com.document.module.readers;

import org.springframework.ai.document.Document;

import java.io.File;
import java.util.List;

public class JsonReader implements IReader {
    /**
     * @param document
     * @return
     */
    @Override
    public List<Document> loadDocument(File document) {
        return List.of();
    }
}
