package com.ruhi.newsapp.models;

public class NewsArticle {
    private String title;
    private String url;
    private String source;
    private String content;
    private String description;
    private long timestamp;

    public NewsArticle(String title, String url, String source, String content, String description) {
        this.title = title;
        this.url = url;
        this.source = source;
        this.content = content;
        this.description = description;
        this.timestamp = System.currentTimeMillis();
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getSource() {
        return source;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

