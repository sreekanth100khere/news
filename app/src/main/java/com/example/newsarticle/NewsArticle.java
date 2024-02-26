package com.example.newsarticle;

public class NewsArticle {
    private String publishedAt;
    private String title;
    private String url;
    private String urlToImage;

    // Constructor, getters, and setters
    NewsArticle(String iPublishedAt, String iTitle, String iUrl, String iUrlToImage){
        publishedAt = iPublishedAt;
        title       = iTitle;
        url         = iUrl;
        urlToImage  = iUrlToImage;
    }


    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
}