package com.document.module.tokenizer;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class WordSplitter implements TokenSplitter {

    private static final Pattern WORD_PATTERN =
            Pattern.compile("(?=\\n\\d+(\\.\\d+)*\\s+[A-Z][^\\n]+)");

    /**
     * Applies this function to the given argument.
     *
     * @param documents the function argument
     * @return the function result
     */
    @Override
    public List<Document> apply(List<Document> documents) {

        return List.of();
    }

    private List<Document> splitDocument(Document document) {
     String[] word =    WORD_PATTERN.split(document.getText());
     List<Document> documents = new ArrayList<>();
        for (String w : word) {
            documents.add(new Document(w, Map.of()));
        }
        return documents;
    }

}
