package com.embedding.module.models;

import java.util.List;

public class EmbeddingRequest {
    private List<ChunkTextReq> documents;

    public List<ChunkTextReq> getDocuments() {
        return documents;
    }

    public void setDocuments(List<ChunkTextReq> documents) {
        this.documents = documents;
    }
}
