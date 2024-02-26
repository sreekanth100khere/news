package com.example.newsarticle;

import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// 4. Implement ViewModel to manage UI-related data
public class NewsViewModel extends AndroidViewModel {
    private MutableLiveData<List<NewsArticle>> mNewsArticlesLiveData;
    private NewsRepository mNewsRepository;


    public NewsViewModel (@NonNull Application iApplication) {

        super(iApplication);
        mNewsArticlesLiveData = new MutableLiveData<>();
        mNewsRepository       = new NewsRepositoryImpl(loadNewsFromJson().getValue());
        mNewsArticlesLiveData.setValue(getmNewsRepository().getmNewsArticlesList());
    }

    private NewsRepository getmNewsRepository() {
        return mNewsRepository;
    }

    public LiveData<List<NewsArticle>> getmNewsArticlesLiveData() {
        return mNewsArticlesLiveData;
    }

    // Method to load news articles from JSON file
    private MutableLiveData<List<NewsArticle>> loadNewsFromJson() {
        try {
            String json = loadJSONFromAsset("news.json");
            JSONObject jsonObject = new JSONObject(json);
            JSONArray articlesArray = jsonObject.getJSONArray("articles");
            for (int i = 0; i < articlesArray.length(); i++) {
                JSONObject articleObject    = articlesArray.getJSONObject(i);
                String publishedAt          = articleObject.getString("publishedAt");
                String title                = articleObject.getString("title");
                String url                  = articleObject.getString("url");
                String urlToImage           = articleObject.getString("urlToImage");
                NewsArticle newsArticle     = new NewsArticle(publishedAt, title, url, urlToImage);

                List<NewsArticle> currentArticles = mNewsArticlesLiveData.getValue();
                if (currentArticles == null) {
                    currentArticles = new ArrayList<>(); // Initialize the list if it's null
                }

                currentArticles.add(newsArticle);
                mNewsArticlesLiveData.setValue(currentArticles);

            }

        } catch (JSONException e) {
            Log.e("TAG", "Error parsing JSON", e);
        }
        return mNewsArticlesLiveData;
    }


    private String loadJSONFromAsset(String iFileName) {
        String json;
        try {
            InputStream inputStream = getApplication().getAssets().open(iFileName);
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