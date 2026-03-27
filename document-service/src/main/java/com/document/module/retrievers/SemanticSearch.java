package com.document.module.retrievers;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgDistanceType.COSINE_DISTANCE;
import static org.springframework.ai.vectorstore.pgvector.PgVectorStore.PgIndexType.HNSW;

public class SemanticSearch extends PgVectorStore implements ISearch {

    public SemanticSearch(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
        super(PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .dimensions(768)                    // Optional: defaults to model dimensions or 1536
                .distanceType(COSINE_DISTANCE)       // Optional: defaults to COSINE_DISTANCE
                .indexType(HNSW)                     // Optional: defaults to HNSW
                .initializeSchema(true)              // Optional: defaults to false
                .schemaName("public")                // Optional: defaults to "public"
                .vectorTableName("documents")        // Optional: defaults to "vector_store"
                .maxDocumentBatchSize(10000));
    }

    /**
     * @param searchRequest SearchRequest
     * @return documents List<Document>
     */
    @Override
    public List<Document> search(SearchRequest searchRequest) {
        return doSimilaritySearch(searchRequest);
    }

    /**
     * @param query String
     * @return documents List<Document>
     */
    @Override
    public List<Document> search(String query) {
        return search(SearchRequest.builder().query(query).topK(5).build());
    }
}
