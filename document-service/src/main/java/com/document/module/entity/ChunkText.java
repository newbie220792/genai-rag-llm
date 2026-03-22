package com.document.module.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Vector;

@Entity
public class ChunkText {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "vector(768)")
    private List<Float> embedding;

    @Column(columnDefinition = "json")
    private String metadata;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Float> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Float> vector) {
        this.embedding = vector;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
