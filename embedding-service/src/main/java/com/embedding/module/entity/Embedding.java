package com.embedding.module.entity;

import jakarta.persistence.*;

import java.util.Vector;

@Entity
public class Embedding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "vector(768)")
    private Vector<String> embedding;

    @Column(columnDefinition = "json")
    private String metadata;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public Vector<String> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(Vector<String> embedding) {
        this.embedding = embedding;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

}
