package com.embedding.module.models;

import java.util.List;

public class EmbeddingRequest {
    private List<ChunkTextReq> documents;
    private String documentId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public List<ChunkTextReq> getDocuments() {
        return documents;
    }

    public void setDocuments(List<ChunkTextReq> documents) {
        this.documents = documents;
    }
}
