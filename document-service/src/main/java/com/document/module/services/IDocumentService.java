package com.document.module.services;

import java.io.File;

/**
 * Service interface for document operations.
 * <p>
 * This interface provides methods for loading and processing documents
 * in the document service module.
 * </p>
 *
 * @author Document Service Team
 * @version 1.0
 * @since 1.0
 */
public interface IDocumentService {

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

    public void loadingDocument(File document) throws Exception;
}
