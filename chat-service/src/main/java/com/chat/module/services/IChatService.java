package com.chat.module.services;

import com.chat.module.models.MessageRequest;
import org.springframework.ai.ollama.api.OllamaApi;

public interface IChatService {
    OllamaApi.Message postMessage(MessageRequest messageRequest);
}
