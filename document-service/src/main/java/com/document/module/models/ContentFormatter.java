package com.document.module.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContentFormatter {
    @SerializedName("metadata_template")
    private String metadataTemplate;
    @SerializedName("metadata_separator")
    private String metadataSeparator;
    @SerializedName("text_template")
    private String textTemplate;
    @SerializedName("excluded_inference_metadata_keys")
    private List<String> excludedInferenceMetadataKeys;
    @SerializedName("excluded_embed_metadata_keys")
    private List<String> excludedEmbedMetadataKeys;

    public String getMetadataTemplate() {
        return metadataTemplate;
    }

    public void setMetadataTemplate(String metadataTemplate) {
        this.metadataTemplate = metadataTemplate;
    }

    public String getMetadataSeparator() {
        return metadataSeparator;
    }

    public void setMetadataSeparator(String metadataSeparator) {
        this.metadataSeparator = metadataSeparator;
    }

    public String getTextTemplate() {
        return textTemplate;
    }

    public void setTextTemplate(String textTemplate) {
        this.textTemplate = textTemplate;
    }

    public List<String> getExcludedInferenceMetadataKeys() {
        return excludedInferenceMetadataKeys;
    }

    public void setExcludedInferenceMetadataKeys(List<String> excludedInferenceMetadataKeys) {
        this.excludedInferenceMetadataKeys = excludedInferenceMetadataKeys;
    }

    public List<String> getExcludedEmbedMetadataKeys() {
        return excludedEmbedMetadataKeys;
    }

    public void setExcludedEmbedMetadataKeys(List<String> excludedEmbedMetadataKeys) {
        this.excludedEmbedMetadataKeys = excludedEmbedMetadataKeys;
    }
}
