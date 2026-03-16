package com.embedding.module.repository;

import com.embedding.module.entity.Embedding;
import org.springframework.stereotype.Repository;

@Repository
public interface EmbeddingRepository extends BaseRepository<Embedding, Long> {
    void findByVector(String chunkTex);
}
