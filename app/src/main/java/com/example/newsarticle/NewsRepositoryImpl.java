package com.example.newsarticle;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NewsRepositoryImpl implements NewsRepository {

    private static final String TAG = "NewsRepositoryImpl";
    private List<NewsArticle> mNewsArticlesList;
    private Context mContext;

    public NewsRepositoryImpl(Context iContext) {
        this.mContext = iContext;
        mNewsArticlesList = new ArrayList<>();
        loadNewsFromJson();
    }


    public NewsRepositoryImpl(List<NewsArticle> mNewsArticlesList) {
        this.mNewsArticlesList = mNewsArticlesList;
    }

    @Override
    public List<NewsArticle> getmNewsArticlesList() {
        return mNewsArticlesList;
    }

    private void loadNewsFromJson() {
        try {
            String json = loadJSONFromAsset("news_feed.json");
            JSONObject jsonObject   = new JSONObject(json);
            JSONArray articlesArray = jsonObject.getJSONArray("articles");
            for (int i = 0; i < articlesArray.length(); i++) {
                JSONObject articleObject = articlesArray.getJSONObject(i);
                String publishedAt = articleObject.getString("publishedAt");
                String title = articleObject.getString("title");
                String url = articleObject.getString("url");
                String urlToImage = articleObject.getString("urlToImage");
                NewsArticle newsArticle = new NewsArticle(publishedAt, title, url, urlToImage);
                mNewsArticlesList.add(newsArticle);
            }
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing JSON", e);
        }
    }

    private String loadJSONFromAsset(String iFileName) {
        String json;
        try {
            InputStream inputStream = mContext.getAssets().open(iFileName);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Log.e("TAG", "Error reading JSON file", e);
            return null;
        }
        return json;
    }



}
