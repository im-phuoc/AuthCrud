package com.spring.authcrud.payload.request;

public class PostRequest {
    private String title;
    private String description;
    private String content;
    private boolean published;

    public PostRequest(String title, String description, String content, boolean published) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.published = published;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }
}
