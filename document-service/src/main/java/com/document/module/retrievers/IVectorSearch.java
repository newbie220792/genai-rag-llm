package com.document.module.retrievers;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;

import java.util.List;

public interface IVectorSearch {
    List<Document> search(SearchRequest searchRequest);

    List<Document> search(String query);
}
