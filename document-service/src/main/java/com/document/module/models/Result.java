package com.document.module.models;

import java.util.List;

public class Result {
    private int index;
    private String metadata;
    private List<Float> output;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public List<Float> getOutput() {
        return output;
    }

    public void setOutput(List<Float> output) {
        this.output = output;
    }
}
