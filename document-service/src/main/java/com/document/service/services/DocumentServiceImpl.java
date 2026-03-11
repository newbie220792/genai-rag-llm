package com.document.service.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DocumentServiceImpl implements IDocumentService {
    private static final Logger log = LogManager.getLogger(DocumentServiceImpl.class);

    /**
     * Loads a document from the file system.
     * <p>
     * This method validates the existence and type of the provided document file
     * before attempting to load it. If the file does not exist or is not a regular
     * file, an exception will be thrown.
     * </p>
     *
     * @param document the {@link File} object representing the document to be loaded.
     *                 Must be a valid, existing file.
     * @throws IllegalArgumentException if the document does not exist or is not a file
     * @throws Exception                if any error occurs during document loading
     */
    @Override
    public void loadingDocument(File document) throws Exception {
        int chunkSize = 500;
        int overlap;
        if (document.exists() && document.isFile()) {
            /*
             * 1. loading a document and convert to csv
             * 2. chunking
             * 3. call embedding model service
             */
            List<String> chunks = new ArrayList<>();
            StringBuilder buffer = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(document))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append(" ");

                    if (buffer.length() >= chunkSize) {
                        chunks.add(buffer.toString());
                        buffer.setLength(0);
                    }
                }

                if (!buffer.isEmpty()) {
                    chunks.add(buffer.toString());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                throw e;
            }
            log.info(chunks);
        } else {
            // throw exception
            log.error("The document is not a valid file or is not a regular file (not supported by the OS).");
            throw new Exception("The document is not a valid file or is not a regular file (not supported by the OS).");
        }
    }
}
