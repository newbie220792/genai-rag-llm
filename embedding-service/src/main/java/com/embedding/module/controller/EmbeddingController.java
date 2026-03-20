package com.embedding.module.controller;

import com.embedding.module.service.IEmbeddingService;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController("/api/v1")
public class EmbeddingController {
    private final IEmbeddingService embeddingService;

    public EmbeddingController(IEmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @GetMapping("/embedding")
    public ResponseEntity<EmbeddingResponse> embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        EmbeddingResponse embeddingResponse = this.embeddingService.embeddingText(message);
        return ResponseEntity.ok(embeddingResponse);
    }
}
