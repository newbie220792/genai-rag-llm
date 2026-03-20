package com.embedding.module.service;

import com.embedding.module.entity.Embedding;
import com.embedding.module.exception.EmbeddingException;
import com.embedding.module.repository.EmbeddingRepository;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaEmbeddingOptions;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmbeddingServiceImpl implements IEmbeddingService {

    /**
     * @param chunkText String
     */
    @Override
    public void saveChunk(String chunkText) {
        try {
            // Create and save embedding entity with vector representation
            Embedding embedding = new Embedding();
            embedding.setTitle(chunkText.substring(0, Math.min(chunkText.length(), 255))); // Truncate to fit column length
            embedding.setContent(chunkText);

            // Convert List<Float> to vector string format: [0.1,0.2,0.3,...]
            List<String> vector = new ArrayList<>();
            vector.add(chunkText);

//            embedding.setEmbedding(vector);
            embedding.setMetadata("{\"chunk_length\":" + chunkText.length() + ",\"vector_dimension\":" + vector.size() + "}");

//            embeddingRepository.save(embedding);
        } catch (Exception e) {
            throw new EmbeddingException("Failed to save chunk as vector: " + e.getMessage(), e);
        }
    }

    @Override
    public EmbeddingResponse embeddingText(String chunkTex) {
        OllamaApi ollamaApi = OllamaApi.builder().build();
        EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                        .ollamaApi(ollamaApi).defaultOptions(OllamaEmbeddingOptions.builder().model(OllamaModel.MISTRAL).build()).build();

        EmbeddingResponse embeddingsResponse = embeddingModel.call(new EmbeddingRequest(List.of(""),
                OllamaEmbeddingOptions.builder()
                        .model("")
                        .truncate(false)
                        .build()));
        return embeddingsResponse;
    }

    private List<Float> parseVectorFromResponse(String response) {
        // Simple JSON array parser for vector data
        // This is a basic implementation - consider using Gson for production
        String[] values = response.replace("[", "").replace("]", "").split(",");
        List<Float> vector = new ArrayList<>();
        for (String value : values) {
            vector.add(Float.parseFloat(value.trim()));
        }
        return vector;
    }
}