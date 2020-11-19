package com.akaene.flagship.dto;

import java.io.Serializable;
import java.net.URI;

public class ReportDto implements Serializable {

    private URI uri;

    private String title;

    private String description;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ReportDto{'" + title + "' " + "<" + uri + '}';
    }
}
