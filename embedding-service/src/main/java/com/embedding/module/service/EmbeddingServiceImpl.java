package com.embedding.module.service;

import com.embedding.module.exception.EmbeddingException;
import jakarta.annotation.PostConstruct;
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
    private EmbeddingModel embeddingModel;

    private final Long MAX_TOKENS = 768L;

    public EmbeddingServiceImpl() {
    }

    @PostConstruct
    private void init() {
        OllamaApi ollamaApi = OllamaApi.builder()
                .baseUrl(ollamaBaseUrl)
                .build();
        embeddingModel = OllamaEmbeddingModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(
                        OllamaEmbeddingOptions.builder()
                                .model(modelName)
                                .dimensions(768)
                                .build())
                .build();
    }

    @Override
    public EmbeddingResponse embeddingText(String chunkText) {
        boolean isValid = isContextValid(chunkText);
        if (!isValid) {
            throw new EmbeddingException("Chunk Text is invalid length");
        }
        return embeddingModel.call(new EmbeddingRequest(List.of(chunkText),
                OllamaEmbeddingOptions.builder()
                        .model(modelName)
                        .dimensions(768)
                        .truncate(false)
                        .build()));
    }

    /**
     * @param chunkTexts List<String>
     * @return EmbeddingResponse
     */
    @Override
    public EmbeddingResponse embeddingText(List<String> chunkTexts) {
        boolean isValid = chunkTexts.stream().allMatch(this::isContextValid);
        if (!isValid) {
            throw new EmbeddingException("Chunk Text is invalid length");
        }
        return embeddingModel.call(new EmbeddingRequest(chunkTexts,
                OllamaEmbeddingOptions.builder()
                        .model(modelName)
                        .dimensions(768)
                        .truncate(false)
                        .build()));
    }

    private boolean isContextValid(String chunkText) {
        int approxTokens = chunkText.length() / 4;
        return approxTokens < MAX_TOKENS;
    }
}