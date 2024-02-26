package com.example.newsarticle;

public class NewsItem {
    private String title;
    private String publishedDate;
    private String imageUrl;

    public NewsItem(String iTitle, String iPublishedDate, String iImageUrl) {
        this.title         = iTitle;
        this.publishedDate = iPublishedDate;
        this.imageUrl      = iImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
