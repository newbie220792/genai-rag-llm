package com.chat.module.models;

import com.google.gson.annotations.SerializedName;

public class ChunkSearch {
    @SerializedName("id")
    private String id;
    @SerializedName("text")
    private String text;
    @SerializedName("media")
    private String media;
    @SerializedName("metadata")
    private Metadata metadata;
    @SerializedName("score")
    private double score;

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

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public static class Metadata {
        @SerializedName("model")
        private String model;
        @SerializedName("distance")
        private double distance;
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

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
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
        private int prompt_tokens;
        @SerializedName("completion_tokens")
        private int completion_tokens;
        @SerializedName("total_tokens")
        private int total_tokens;

        public int getPrompt_tokens() {
            return prompt_tokens;
        }

        public void setPrompt_tokens(int prompt_tokens) {
            this.prompt_tokens = prompt_tokens;
        }

        public int getCompletion_tokens() {
            return completion_tokens;
        }

        public void setCompletion_tokens(int completion_tokens) {
            this.completion_tokens = completion_tokens;
        }

        public int getTotal_tokens() {
            return total_tokens;
        }

        public void setTotal_tokens(int total_tokens) {
            this.total_tokens = total_tokens;
        }
    }
}
