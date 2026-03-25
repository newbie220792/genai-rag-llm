package com.embedding.module.service;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaEmbeddingOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingServiceImpl implements IEmbeddingService {

    @Value("${ollama.base.url}")
    private String ollamaBaseUrl;

    @Value("${embedding.model.name}")
    private String modelName;

    @Override
    public EmbeddingResponse embeddingText(String chunkText) {
        OllamaApi ollamaApi = OllamaApi.builder()
                .baseUrl(ollamaBaseUrl)
                .build();
        EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(OllamaEmbeddingOptions.builder().model(modelName)
                        .build())
                .build();

        return embeddingModel.call(new EmbeddingRequest(List.of(chunkText),
                OllamaEmbeddingOptions.builder()
                        .model(modelName)
                        .dimensions(768)
                        .truncate(false)
                        .build()));
    }
}