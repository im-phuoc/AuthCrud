package com.spring.authcrud.payload.response;

public class PostResponse {
    private String title;
    private String description;
    private String content;
    private String author;

    public PostResponse(String title, String description, String content, String author) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.author = author;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
