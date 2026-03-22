package com.embedding.module.controller;

import com.embedding.module.models.EmbeddingRequest;
import com.embedding.module.service.IEmbeddingService;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1")
public class EmbeddingController {
    private final IEmbeddingService embeddingService;

    public EmbeddingController(IEmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @PostMapping("embedding-document")
    public ResponseEntity<Map<String, Object>> embed(@RequestBody EmbeddingRequest embeddingRequest) {
        Map<String, Object> response = new HashMap<>();
        List<Document> documents = embeddingRequest.getDocuments();
        for (Document doc : documents) {
            response.put(doc.getId(), this.embeddingService.embeddingText(doc.getText()));
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("embedding-text")
    public ResponseEntity<EmbeddingResponse> embedTest(@RequestParam String message) {
        EmbeddingResponse embeddingResponse = this.embeddingService.embeddingText(message);
        return ResponseEntity.ok(embeddingResponse);
    }
}
