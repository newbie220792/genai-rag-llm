package com.document.module.readers;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class TikaReader implements IReader {

    /**
     * Should load a file if the file size lowers 20Mb
     *
     * @param document File
     * @return List<Document>
     */
    @Override
    public List<Document> loadDocument(File document) {
        Resource resource = new AbstractResource() {
            @Override
            public String getDescription() {
                return document.getName();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new FileInputStream(document);
            }
        };
        
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource,
                ExtractedTextFormatter.builder()
                        .withLeftAlignment(true)
                        .build());
        return tikaDocumentReader.read();
    }
}
