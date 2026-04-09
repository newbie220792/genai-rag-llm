package com.document.module.tokenizer;

import org.springframework.ai.document.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class WordSplitter implements TokenSplitter {
    private static final Pattern WORD_PATTERN =
            Pattern.compile("\\s+");

    /**
     * Applies this function to the given argument.
     *
     * @param documents the function argument
     * @return the function result
     */
    @Override
    public List<Document> apply(List<Document> documents) {
        return documents.stream()
                .flatMap(doc -> splitDocument(doc).stream())
                .toList();
    }

    private List<Document> splitDocument(Document document) {
        String[] word = WORD_PATTERN.split(document.getText());
        List<Document> documents = new ArrayList<>();
        for (String w : word) {
            documents.add(new Document(w));
        }
        return documents;
    }

}
