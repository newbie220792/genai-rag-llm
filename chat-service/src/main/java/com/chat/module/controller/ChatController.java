package com.chat.module.controller;

import com.chat.module.services.IChatService;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ChatController {

    private final IChatService chatService;

    public ChatController(IChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("chat")
    public ResponseEntity<OllamaApi.Message> postMessage(@RequestParam String userId, @RequestParam String message) {
        return ResponseEntity.ok(chatService.postMessage(userId, message));
    }
}
