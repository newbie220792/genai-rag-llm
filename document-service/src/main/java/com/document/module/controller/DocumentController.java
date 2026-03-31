package com.document.module.controller;

import com.document.module.services.IDocumentService;
import com.document.module.utils.GsonUtils;
import org.springframework.ai.document.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/api/v1/document")
public class DocumentController {

    private final IDocumentService documentService;

    public DocumentController(IDocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(value = "load", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadDocument(@RequestPart("document") MultipartFile document) {
        try {
            documentService.loadingDocument(document);
            return ResponseEntity.ok().body("Success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(GsonUtils.toJson(e.getMessage()));
        }
    }

    @GetMapping(value = "search-vector")
    public ResponseEntity<?> searchVector(@RequestParam String userPrompt) {
        try {
            List<Document> docs = documentService.searchVector(userPrompt);
            return ResponseEntity.ok().body(docs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(GsonUtils.toJson(e.getMessage()));
        }
    }

    @GetMapping(value = "hybrid-search")
    public ResponseEntity<?> hybridSearch(@RequestParam String userPrompt) {
        try {
            List<Document> docs = documentService.hybridSearch(userPrompt);
            return ResponseEntity.ok().body(docs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(GsonUtils.toJson(e.getMessage()));
        }
    }
}
