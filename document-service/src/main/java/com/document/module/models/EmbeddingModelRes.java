package com.document.module.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;


public class EmbeddingModelRes {
    @SerializedName("metadata")
    private Metadata metadata;
    @SerializedName("result")
    private Result result;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public static class Metadata {
        @SerializedName("model")
        private String model;
        @SerializedName("usage")
        private Usage usage;
        @SerializedName("empty")
        private boolean empty;

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public Usage getUsage() {
            return usage;
        }

        public void setUsage(Usage usage) {
            this.usage = usage;
        }

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }
    }

    public static class Usage {
        @SerializedName("prompt_tokens")
        private int promptTokens;
        @SerializedName("completion_tokens")
        private int completionTokens;
        @SerializedName("total_tokens")
        private int totalTokens;

        public int getPromptTokens() {
            return promptTokens;
        }

        public void setPromptTokens(int promptTokens) {
            this.promptTokens = promptTokens;
        }

        public int getCompletionTokens() {
            return completionTokens;
        }

        public void setCompletionTokens(int completionTokens) {
            this.completionTokens = completionTokens;
        }

        public int getTotalTokens() {
            return totalTokens;
        }

        public void setTotalTokens(int totalTokens) {
            this.totalTokens = totalTokens;
        }
    }

    public static class Result {
        @SerializedName("index")
        private int index;
        @SerializedName("metadata")
        private ResultMetadata metadata;
        @SerializedName("output")
        private List<Float> output;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public ResultMetadata getMetadata() {
            return metadata;
        }

        public void setMetadata(ResultMetadata metadata) {
            this.metadata = metadata;
        }

        public List<Float> getOutput() {
            return output;
        }

        public void setOutput(List<Float> output) {
            this.output = output;
        }
    }

    public static class ResultMetadata {
        @SerializedName("modality_type")
        private String modalityType;
        @SerializedName("document_id")
        private String documentId;
        @SerializedName("mime_type")
        private MimeType mimeType;
        @SerializedName("document_data")
        private Object documentData;

        public String getModalityType() {
            return modalityType;
        }

        public void setModalityType(String modalityType) {
            this.modalityType = modalityType;
        }

        public String getDocumentId() {
            return documentId;
        }

        public void setDocumentId(String documentId) {
            this.documentId = documentId;
        }

        public MimeType getMimeType() {
            return mimeType;
        }

        public void setMimeType(MimeType mimeType) {
            this.mimeType = mimeType;
        }

        public Object getDocumentData() {
            return documentData;
        }

        public void setDocumentData(Object documentData) {
            this.documentData = documentData;
        }
    }

    public static class MimeType {
        @SerializedName("type")
        private String type;
        @SerializedName("subtype")
        private String subtype;
        @SerializedName("parameters")
        private Map<String, Object> parameters;
        @SerializedName("charset")
        private String charset;
        @SerializedName("concrete")
        private boolean concrete;
        @SerializedName("subtype_suffix")
        private String subtypeSuffix;
        @SerializedName("wildcard_type")
        private boolean wildcardType;
        @SerializedName("wildcard_subtype")
        private boolean wildcardSubtype;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSubtype() {
            return subtype;
        }

        public void setSubtype(String subtype) {
            this.subtype = subtype;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public boolean isConcrete() {
            return concrete;
        }

        public void setConcrete(boolean concrete) {
            this.concrete = concrete;
        }

        public String getSubtypeSuffix() {
            return subtypeSuffix;
        }

        public void setSubtypeSuffix(String subtypeSuffix) {
            this.subtypeSuffix = subtypeSuffix;
        }

        public boolean isWildcardType() {
            return wildcardType;
        }

        public void setWildcardType(boolean wildcardType) {
            this.wildcardType = wildcardType;
        }

        public boolean isWildcardSubtype() {
            return wildcardSubtype;
        }

        public void setWildcardSubtype(boolean wildcardSubtype) {
            this.wildcardSubtype = wildcardSubtype;
        }
    }
}
