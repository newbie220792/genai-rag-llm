package com.document.module.controller;

import com.document.module.services.IDocumentService;
import com.document.module.utils.GsonUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

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
}
