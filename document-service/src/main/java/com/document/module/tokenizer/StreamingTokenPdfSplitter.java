package com.document.module.tokenizer;

import org.springframework.ai.document.Document;

import java.util.List;

public class StreamingTokenPdfSplitter extends BaseTokenSplitter {
    /**
     * Applies this function to the given argument.
     *
     * @param documents the function argument
     * @return the function result
     */
    @Override
    public List<Document> apply(List<Document> documents) {
        return List.of();
    }
}
