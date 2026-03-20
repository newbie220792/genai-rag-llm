package com.embedding.module.service;

import org.springframework.ai.embedding.EmbeddingResponse;

public interface IEmbeddingService {
    public void saveChunk(String chunkTex);
    public EmbeddingResponse embeddingText(String chunkTex);
}