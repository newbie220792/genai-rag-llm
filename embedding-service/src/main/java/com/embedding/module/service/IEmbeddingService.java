package com.embedding.module.service;

import org.springframework.ai.embedding.EmbeddingResponse;

public interface IEmbeddingService {
    public EmbeddingResponse embeddingText(String chunkText);
}