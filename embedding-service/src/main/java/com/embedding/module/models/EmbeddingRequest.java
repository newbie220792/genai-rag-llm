package com.embedding.module.models;

import org.springframework.ai.document.Document;

import java.util.List;

public class EmbeddingRequest {
    List<Document> documents;

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
