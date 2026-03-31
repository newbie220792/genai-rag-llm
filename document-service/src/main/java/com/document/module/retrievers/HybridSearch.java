package com.document.module.retrievers;

import com.document.module.entity.ChunkText;
import com.document.module.respository.ChunkTextRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;

import java.util.*;


public class HybridSearch implements ISearch {

    private final ChunkTextRepository chunkTextRepository;

    public HybridSearch(ChunkTextRepository chunkTextRepository) {
        this.chunkTextRepository = chunkTextRepository;
    }

    /**
     * @param searchRequest SearchRequest
     * @return documents List<Document>
     */
    @Override
    public List<Document> search(SearchRequest searchRequest) {
        List<Document> keywordDocs = searchKeywork(searchRequest);
        List<Document> vectorDocs = new ArrayList<>();
        return merchAndReranking(keywordDocs, vectorDocs);
    }

    /**
     * @param query String
     * @return documents List<Document>
     */
    @Override
    public List<Document> search(String query) {
        return search(SearchRequest.builder().query(query).topK(5).build());
    }

    /**
     * Performs keyword-based search to retrieve relevant documents.
     * Queries the repository using the search term and retrieves the top-k matching chunks,
     * then converts them into Document objects.
     *
     * @param searchRequest SearchRequest containing the query string and top-k parameter
     * @return List of Document objects created from the matching chunk texts
     */
    private List<Document> searchKeywork(SearchRequest searchRequest) {
        List<ChunkText> chunkTexts = chunkTextRepository.getTopDocument(searchRequest.getQuery(), searchRequest.getTopK());
        List<Document> docs = new ArrayList<>();
        chunkTexts.forEach(c -> docs.add(new Document(c.getContent())));
        return docs;
    }

    /**
     * Merges and reranks documents from keyword and vector search results using Reciprocal Rank Fusion (RRF).
     * Vector documents are prioritized first, then keyword documents are added if not already present.
     * Documents are scored using RRF algorithm with k=60, and the top 10 results are returned.
     *
     * @param keywordDocs List of documents from keyword-based search
     * @param vectorDocs  List of documents from vector-based search
     * @return List of top 10 reranked documents based on combined scores
     */
    private List<Document> merchAndReranking(List<Document> keywordDocs, List<Document> vectorDocs) {
        Map<String, Document> merged = new LinkedHashMap<>();
        vectorDocs.forEach(d -> merged.put(d.getId(), d));
        keywordDocs.forEach(d -> merged.putIfAbsent(d.getId(), d));

        List<Document> documents = new ArrayList<>(merged.values());
        Map<String, Double> scores = new HashMap<>();
        for (int rank = 0; rank < documents.size(); rank++) {
            String docId = documents.get(rank).getId();
            double score = 1.0 / (60 + rank + 1);
            scores.merge(docId, score, Double::sum);
        }
        List<String> result =
                scores.entrySet()
                        .stream()
                        .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                        .map(Map.Entry::getKey)
                        .limit(10)
                        .toList();

        return documents.stream().filter(d -> result.contains(d.getId())).toList();
    }

}
