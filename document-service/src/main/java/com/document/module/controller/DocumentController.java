package com.document.module.controller;

import com.document.module.models.DocumentModel;
import com.document.module.services.IDocumentService;
import com.document.module.utils.GsonUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class DocumentController {

    private final IDocumentService documentService;

    public DocumentController(IDocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/document/upload")
    public ResponseEntity<String> uploadDocument(@RequestBody DocumentModel document) {
        try {
            documentService.loadingDocument(document.getFile());
            return ResponseEntity.ok().body("Success");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(GsonUtils.toJson(e.getMessage()));
        }
    }
}
