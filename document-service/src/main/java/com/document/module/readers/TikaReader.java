package com.document.module.readers;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.tika.TikaDocumentReader;

import java.io.File;
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
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(document.getAbsolutePath(),
                ExtractedTextFormatter.builder()
                        .withLeftAlignment(true)
                        .build());
        return tikaDocumentReader.read();
    }
}
