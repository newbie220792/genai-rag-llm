package com.embedding.module.service;

import org.springframework.ai.embedding.EmbeddingResponse;

import java.util.List;

public interface IEmbeddingService {
    public EmbeddingResponse embeddingText(String chunkText);

    public EmbeddingResponse embeddingText(List<String> chunkTexts);
}