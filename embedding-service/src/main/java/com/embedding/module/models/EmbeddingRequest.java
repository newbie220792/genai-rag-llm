package com.embedding.module.models;

import java.util.List;

public class EmbeddingRequest {
    List<String> messages;

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
