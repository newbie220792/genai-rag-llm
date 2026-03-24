package com.document.module.models;

import com.google.gson.annotations.SerializedName;

public class Metadata {
    @SerializedName("charset")
    private String charset;
    @SerializedName("chunk_index")
    private int chunkIndex;
    @SerializedName("filename")
    private String filename;
    @SerializedName("parent_document_id")
    private String parentDocumentId;
    @SerializedName("source")
    private String source;
    @SerializedName("total_chunks")
    private int totalChunks;

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    public void setChunkIndex(int chunkIndex) {
        this.chunkIndex = chunkIndex;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getParentDocumentId() {
        return parentDocumentId;
    }

    public void setParentDocumentId(String parentDocumentId) {
        this.parentDocumentId = parentDocumentId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(int totalChunks) {
        this.totalChunks = totalChunks;
    }
}
