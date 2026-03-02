package com.document.service.controller;

import com.document.service.models.DocumentModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class DocumentController {

    @PostMapping("/document/upload")
    public ResponseEntity<DocumentModel> uploadDocument(@RequestBody DocumentModel document) {
        return ResponseEntity.ok().body(document);
    }
}
