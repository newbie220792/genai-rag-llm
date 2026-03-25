package com.document.module.tokenizer;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;

import java.util.List;

/**
 * A custom PDF text splitter that extends Spring AI's TextSplitter.
 * This class is responsible for splitting PDF text content into smaller chunks
 * for processing in a RAG (Retrieval-Augmented Generation) system.
 *
 * @author Document Service
 * @version 1.0
 * @see TextSplitter
 */
public class TokenPdfSplitter extends BaseTokenSplitter {

    /**
     * Number of tokens per chunk.
     */
    private final int chunkSize;

    /**
     * Number of overlapping tokens between chunks to preserve context.
     */
    private final int chunkOverlap;

    /**
     * Maximum number of characters parsed from the PDF.
     */
    private final int maxChars;

    /**
     * Creates a new TokenPdfSplitter instance.
     *
     * @param chunkSize    the maximum number of tokens in each chunk
     * @param chunkOverlap the number of overlapping tokens between consecutive chunks
     * @param maxChars     the maximum number of characters allowed to be parsed from the PDF
     */
    public TokenPdfSplitter(int chunkSize, int chunkOverlap, int maxChars) {
        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
        this.maxChars = maxChars;
    }

    public TokenPdfSplitter() {
        this.chunkSize = 500;
        this.chunkOverlap = 50;
        this.maxChars = 500;
    }

    /**
     * @param documents List<Document>
     * @return List<String> lst
     */
    @Override
    public List<Document> apply(List<Document> documents) {
        return super.apply(documents);
    }
}
