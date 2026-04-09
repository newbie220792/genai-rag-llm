package com.embedding.module.controller;

import com.embedding.module.models.ChunkTextReq;
import com.embedding.module.models.EmbeddingRequest;
import com.embedding.module.service.IEmbeddingService;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1")
public class EmbeddingController {
    private final IEmbeddingService embeddingService;

    public EmbeddingController(IEmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @PostMapping(value = "embedding-document")
    public ResponseEntity<Map<String, EmbeddingResponse>> embed(@RequestBody EmbeddingRequest embeddingRequest) {
        List<ChunkTextReq> documents = embeddingRequest.getDocuments();
        List<String> texts = documents.stream().map(ChunkTextReq::getText).toList();
        return ResponseEntity.ok(Map.of(embeddingRequest.getDocumentId(), this.embeddingService.embeddingText(texts)));
    }

    @GetMapping("embedding-text")
    public ResponseEntity<EmbeddingResponse> embedTest(@RequestParam String message) {
        return ResponseEntity.ok(this.embeddingService.embeddingText(message));
    }

    @GetMapping("embedding-test")
    public ResponseEntity<String> embedTest() {
        return ResponseEntity.ok("Embedding model test...");
    }
}
