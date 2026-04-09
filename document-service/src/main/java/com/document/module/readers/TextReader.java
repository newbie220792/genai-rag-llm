package com.document.module.readers;

import com.document.module.exception.DocumentException;
import org.springframework.ai.document.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TextReader implements IReader {
    /**
     * @param document File
     * @return List<Document>
     */
    @Override
    public List<Document> loadDocument(File document) {
        List<Document> documents = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(document))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                documents.add(new Document(line));
            }
        } catch (Exception e) {
            throw new DocumentException(e);
        }
        return documents;
    }
}
