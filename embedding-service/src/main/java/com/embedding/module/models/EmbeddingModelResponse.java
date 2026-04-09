package com.embedding.module.models;

import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingResponse;

import java.util.List;

public class EmbeddingModelResponse extends EmbeddingResponse {
    private String content;

    /**
     * Creates a new {@link EmbeddingModelResponse} instance with empty metadata.
     *
     * @param embeddings the embedding data.
     */
    public EmbeddingModelResponse(List<Embedding> embeddings) {
        super(embeddings);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
