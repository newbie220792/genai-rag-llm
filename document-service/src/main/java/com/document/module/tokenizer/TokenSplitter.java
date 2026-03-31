package com.document.module.tokenizer;

import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;

import java.util.List;

public interface TokenSplitter extends DocumentTransformer {
    /**
     * Applies this function to the given argument.
     *
     * @param documents the function argument
     * @return the function result
     */
    @Override
    public List<Document> apply(List<Document> documents);
}
