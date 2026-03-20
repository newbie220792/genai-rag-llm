package com.embedding.module.enums;

import org.springframework.ai.model.ChatModelDescription;

public enum CustomOllamaModel implements ChatModelDescription {
    LLAMA3_8B("llama3:8b");

    private final String id;

    private CustomOllamaModel(String id) {
        this.id = id;
    }

    public String id() {
        return this.id;
    }

    public String getName() {
        return this.id;
    }
}
