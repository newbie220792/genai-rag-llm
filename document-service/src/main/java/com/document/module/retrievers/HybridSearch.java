package com.document.module.retrievers;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;

import java.util.ArrayList;
import java.util.List;

public class HybridSearch implements ISearch {

    /**
     * @param searchRequest SearchRequest
     * @return documents List<Document>
     */
    @Override
    public List<Document> search(SearchRequest searchRequest) {
        List<Document> keywordDocs = searchKeywork(searchRequest.getQuery());

        List<Document> vectorDocs = similaritySearch(searchRequest);
        return List.of();
    }

    /**
     * @param query String
     * @return documents List<Document>
     */
    @Override
    public List<Document> search(String query) {
        return new ArrayList<Document>();
    }

    private List<Document> searchKeywork(String query) {
    }

    private List<Document> merchAndReranking(Document... document) {

    }

}
