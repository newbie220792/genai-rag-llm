package com.document.module.models;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class ChunkTextReq {
    @SerializedName("id")
    private String id;
    @SerializedName("text")
    private String text;
    @SerializedName("metadata")
    private Map<String, Object> metadata;
    @SerializedName("contentFormatter")
    private ContentFormatter contentFormatter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public ContentFormatter getContentFormatter() {
        return contentFormatter;
    }

    public void setContentFormatter(ContentFormatter contentFormatter) {
        this.contentFormatter = contentFormatter;
    }
}
