package com.chat.module.services;

import com.chat.module.exception.ChatException;
import com.chat.module.models.ChunkSearch;
import com.chat.module.models.MessageRequest;
import com.chat.module.utils.GsonUtils;
import com.google.gson.reflect.TypeToken;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements IChatService {
    @Value("${document.service.url}")
    private String documentServiceUrl;

    @Value("${chat.model}")
    private String chatModelName;

    @Value("${ollama.base.url}")
    private String ollamaBaseUrl;

    private final RestTemplate restTemplate;

    public ChatServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public OllamaApi.Message postMessage(MessageRequest messageRequest) {
        // 1. embedding model
        // 2. search vector
        ResponseEntity<String> res = restTemplate.getForEntity(documentServiceUrl + "/api/v1/document/search-vector?userPrompt=" + messageRequest.getMessage(),
                String.class);
        String body = res.getBody();
        if (body == null) {
            throw new ChatException("Body in invalid format.");
        }
        List<ChunkSearch> docs = GsonUtils.fromJson(body, new TypeToken<List<ChunkSearch>>() {
        }.getType());

        if (docs == null || docs.isEmpty()) {
            throw new ChatException("No results found. Please try again with a different question.");
        }

        String systemPrompt = """
                You are a helpful assistant. Use the following context to answer the user's question.
                If you don't know the answer based on the context, say so.
                """;

        // 3. build prompt template
        String context = docs.stream()
                .map(ChunkSearch::getText)
                .collect(Collectors.joining("\n\n"));

        // 4. send prompt to LLM
        OllamaApi ollamaApi = OllamaApi.builder()
                .baseUrl(ollamaBaseUrl)
                .build();

        OllamaApi.ChatRequest request = OllamaApi.ChatRequest.builder(chatModelName)
                .stream(false) // not streaming
                .messages(List.of(
                        OllamaApi.Message.builder(OllamaApi.Message.Role.SYSTEM)
                                .content(systemPrompt)
                                .build(),
                        OllamaApi.Message.builder(OllamaApi.Message.Role.ASSISTANT)
                                .content(context).build(),
                        OllamaApi.Message.builder(OllamaApi.Message.Role.USER)
                                .content(messageRequest.getMessage())
                                .build()))
                .options(OllamaChatOptions.builder().temperature(0.9).build())
                .build();

        OllamaApi.ChatResponse response = ollamaApi.chat(request);
        // 5. send a message back to the user
        return response.message();
    }
}
