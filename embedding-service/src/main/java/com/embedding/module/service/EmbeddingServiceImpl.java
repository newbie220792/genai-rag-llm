package com.embedding.module.service;

import com.embedding.module.entity.Embedding;
import com.embedding.module.exception.EmbeddingException;
import com.embedding.module.repository.EmbeddingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmbeddingServiceImpl implements IEmbeddingService {
    @Autowired
    private EmbeddingRepository embeddingRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${embedding.service.url:http://localhost:8080/api/embeddings}")
    private String embeddingServiceUrl;


    /**
     * @param chunkTex String
     */
    @Override
    public void saveChunk(String chunkTex) {
        try {
            // Create and save embedding entity with vector representation
            Embedding embedding = new Embedding();
            embedding.setTitle(chunkTex.substring(0, Math.min(chunkTex.length(), 255))); // Truncate to fit column length
            embedding.setContent(chunkTex);

            // Convert List<Float> to vector string format: [0.1,0.2,0.3,...]
            List<String> vector = new ArrayList<>();
            vector.add(chunkTex);

            embedding.setEmbedding(vector);
            embedding.setMetadata("{\"chunk_length\":" + chunkTex.length() + ",\"vector_dimension\":" + vector.size() + "}");

            embeddingRepository.save(embedding);
        } catch (Exception e) {
            throw new EmbeddingException("Failed to save chunk as vector: " + e.getMessage(), e);
        }
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