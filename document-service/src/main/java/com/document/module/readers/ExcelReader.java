package com.document.module.readers;

import org.springframework.ai.document.Document;

import java.io.File;
import java.util.List;

public class ExcelReader implements IReader {
    /**
     * @param document File
     * @return List<Document>
     */
    @Override
    public List<Document> loadDocument(File document) {
        return List.of();
    }
}
