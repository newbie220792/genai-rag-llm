package com.document.service.models;

import java.io.File;

public class DocumentModel {
    private String title;
    private File file;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
