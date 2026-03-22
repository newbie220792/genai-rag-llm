package com.document.module.tokenize;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;

import java.util.*;
import java.util.regex.Pattern;

/**
 * TokenSectionSplitter is a custom TextSplitter implementation designed for
 * Retrieval-Augmented Generation (RAG) systems.
 *
 * <p>
 * This splitter performs a two-stage chunking strategy:
 * </p>
 *
 * <ol>
 *     <li><b>Section-based splitting</b>:
 *         <ul>
 *             <li>Splits documents using structured section headers (e.g., "1. PURPOSE").</li>
 *             <li>Preserves semantic boundaries of the document.</li>
 *         </ul>
 *     </li>
 *     <li><b>Token-based splitting (fallback)</b>:
 *         <ul>
 *             <li>If a section exceeds the maximum token limit, it is further split.</li>
 *             <li>Uses a sliding window with overlap to preserve context.</li>
 *         </ul>
 *     </li>
 * </ol>
 *
 * <p>
 * This approach ensures:
 * </p>
 * <ul>
 *     <li>High-quality semantic chunks for vector search.</li>
 *     <li>Improved retrieval accuracy in RAG pipelines.</li>
 *     <li>Compatibility with embedding model constraints.</li>
 * </ul>
 *
 * <p>
 * Recommended usage:
 * </p>
 * <pre>
 * {@code
 * TokenSectionSplitter splitter = new TokenSectionSplitter(400, 50);
 * List<Document> chunks = splitter.split(rawDocuments);
 * vectorStore.add(chunks);
 * }
 * </pre>
 *
 * <p>
 * Notes:
 * </p>
 * <ul>
 *     <li>Each chunk includes metadata field "section".</li>
 *     <li>Section title is injected into content for better embedding quality.</li>
 *     <li>Token estimation is approximate (word-based) and can be replaced by a real tokenizer.</li>
 * </ul>
 */
public class TokenSectionSplitter extends TextSplitter {
    /**
     * Maximum number of tokens per chunk.
     */
    private final int maxTokens;

    /**
     * Number of overlapping tokens between adjacent chunks.
     */
    private final int overlap;

    /**
     * Regex pattern to detect section headers such as:
     * "1. PURPOSE", "2. EMBEDDING MODEL STRATEGY".
     */
    private static final Pattern SECTION_PATTERN =
            Pattern.compile("(?=\\n?\\d+\\.\\s+[A-Z][A-Z ]+)");

    /**
     * Constructs a TokenSectionSplitter.
     *
     * @param maxTokens maximum tokens per chunk
     * @param overlap   number of overlapping tokens between chunks
     */
    public TokenSectionSplitter(int maxTokens, int overlap) {
        this.maxTokens = maxTokens;
        this.overlap = overlap;
    }

    /**
     * Splits a list of documents into smaller chunks.
     *
     * @param documents list of input documents
     * @return list of chunked documents
     */
    @Override
    public List<Document> split(List<Document> documents) {
        List<Document> results = new ArrayList<>();

        for (Document doc : documents) {
            results.addAll(splitBySection(doc));
        }

        return results;
    }

    /**
     * Splits a single document into sections, then applies token-based splitting if needed.
     *
     * @param doc input document
     * @return list of section-aware chunks
     */
    private List<Document> splitBySection(Document doc) {
        String content = doc.getText();
        List<Document> sectionDocs = new ArrayList<>();

        String[] sections = SECTION_PATTERN.split(content);

        for (String section : sections) {
            if (section.isBlank()) continue;

            String title = extractTitle(section);

            Map<String, Object> metadata = new HashMap<>(doc.getMetadata());
            metadata.put("section", title);

            String enrichedContent = "[SECTION: " + title + "]\n" + section.trim();

            if (estimateTokens(enrichedContent) <= maxTokens) {
                sectionDocs.add(new Document(enrichedContent, metadata));
            } else {
                sectionDocs.addAll(splitByToken(enrichedContent, metadata));
            }
        }

        return sectionDocs;
    }

    /**
     * Splits a long section into smaller chunks using token-based sliding window.
     *
     * @param text     input text
     * @param metadata metadata to attach to each chunk
     * @return list of token-based chunks
     */
    private List<Document> splitByToken(String text, Map<String, Object> metadata) {
        List<Document> chunks = new ArrayList<>();

        String[] words = text.split("\\s+");
        int start = 0;

        while (start < words.length) {
            int end = Math.min(start + maxTokens, words.length);

            String chunk = String.join(" ", Arrays.copyOfRange(words, start, end));
            chunks.add(new Document(chunk, metadata));

            start = end - overlap;
            if (start < 0) start = 0;
        }

        return chunks;
    }

    /**
     * Extracts the section title from a section block.
     *
     * @param section section text
     * @return extracted title or "UNKNOWN" if not found
     */
    private String extractTitle(String section) {
        String[] lines = section.strip().split("\n");
        return lines.length > 0 ? lines[0].trim() : "UNKNOWN";
    }

    /**
     * Estimates token count using a simple whitespace-based approach.
     *
     * <p>
     * NOTE: This is an approximation. For production systems, replace with
     * a tokenizer aligned with your embedding model.
     * </p>
     *
     * @param text input text
     * @return estimated token count
     */
    private int estimateTokens(String text) {
        return text.split("\\s+").length;
    }

    @Override
    protected List<String> splitText(String s) {
        if (s.isEmpty() ) return new ArrayList<>();
        String[] strings = SECTION_PATTERN.split(s);
        List<String> result = new ArrayList<>();
        for (String string : strings) {
            if (string.isBlank()) continue;
            result.add(string);
        }
        return result;
    }
}
