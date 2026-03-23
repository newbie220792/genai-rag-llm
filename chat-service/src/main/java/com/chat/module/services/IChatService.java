package com.chat.module.services;

import org.springframework.ai.ollama.api.OllamaApi;

public interface IChatService {
    OllamaApi.Message postMessage(String userId, String message);
}
