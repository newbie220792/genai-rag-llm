package com.chat.module.controller;

import com.chat.module.models.MessageRequest;
import com.chat.module.services.IChatService;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final IChatService chatService;

    public ChatController(IChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Curl test examples:
     * <p>
     * Basic test:
     * curl -X GET "http://localhost:8080/api/v1/chat?userId=user123&message=Hello"
     * <p>
     * With URL encoded message:
     * curl -X GET "http://localhost:8080/api/v1/chat?userId=user123&message=What%20is%20AI%3F"
     * <p>
     * With verbose output:
     * curl -v -X GET "http://localhost:8080/api/v1/chat?userId=user123&message=Hello"
     * <p>
     * With custom headers:
     * curl -X GET "http://localhost:8080/api/v1/chat?userId=user123&message=Hello" \
     * -H "Content-Type: application/json" \
     * -H "Accept: application/json"
     */
    @PostMapping("post-message")
    public ResponseEntity<OllamaApi.Message> postMessage(@RequestBody MessageRequest messageRequest) {
        return ResponseEntity.ok(chatService.postMessage(messageRequest));
    }

    @GetMapping("/")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test");
    }
}
