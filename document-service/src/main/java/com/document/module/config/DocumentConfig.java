package com.document.module.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class DocumentConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

//    @Bean
//    public VectorStore vectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
//        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
//                .dimensions(1536)                    // Optional: defaults to model dimensions or 1536
//                .distanceType(COSINE_DISTANCE)       // Optional: defaults to COSINE_DISTANCE
//                .indexType(HNSW)                     // Optional: defaults to HNSW
//                .initializeSchema(true)              // Optional: defaults to false
//                .schemaName("public")                // Optional: defaults to "public"
//                .vectorTableName("vector_store")     // Optional: defaults to "vector_store"
//                .maxDocumentBatchSize(10000)         // Optional: defaults to 10000
//                .build();
//    }
}
