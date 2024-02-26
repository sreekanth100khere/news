package com.example.newsarticle;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jsonexample.R;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// MainActivity.java
public class MainActivity extends AppCompatActivity implements ViewModelStoreOwner, LifecycleOwner {
    private RecyclerView mRecyclerView;
    private NewsAdapter mNewsAdapter;
    
    private NewsViewModel newsViewModel;
    private EditText mSearchEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.example.jsonexample.R.layout.activity_main);


        mRecyclerView   = findViewById(R.id.id_rv);
        mSearchEt       = findViewById(R.id.id_search_et);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mNewsAdapter = new NewsAdapter(null);
        mRecyclerView.setAdapter(mNewsAdapter);

        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);

        newsViewModel.getmNewsArticlesLiveData().observe(this, newsArticles -> {
            List<NewsItem> newsItemList = new ArrayList<NewsItem>();
            for(int i=0;i<newsArticles.size();i++){
                newsItemList.add(new NewsItem(newsArticles.get(i).getTitle(),newsArticles.get(i).getPublishedAt(),newsArticles.get(i).getUrlToImage()));
            }
            mNewsAdapter.setmNewsItemList(newsItemList);
            sortNewsByDate();
        });

        mSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterNews(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void sortNewsByDate() {
        // Comparator for comparing news items based on published date
        Comparator<NewsItem> dateComparator = new Comparator<NewsItem>() {
            SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

            @Override
            public int compare(NewsItem item1, NewsItem item2) {
                try {
                    Date date1 = sdf.parse(item1.getPublishedDate());
                    Date date2 = sdf.parse(item2.getPublishedDate());
                    // Descending order (most recent first)
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        };

        // Sort the news items using the comparator
        Collections.sort(mNewsAdapter.getmNewsItemList(), dateComparator);
    }

    private void filterNews(String searchText) {
        ArrayList<NewsItem> filteredList = new ArrayList<NewsItem>();
        List<NewsArticle> newsArticles = newsViewModel.getmNewsArticlesLiveData().getValue();
        ArrayList<Integer> order            = new ArrayList<Integer>();
        if (newsArticles != null) {
            for (NewsArticle article : newsArticles) {
                if (article.getTitle().toLowerCase().contains(searchText.toLowerCase())) {
                    CharSequence articleCharSeq        = (CharSequence) article.getTitle(); // Conversion is implicit
                    CharSequence searchTextCharSeq     = (CharSequence) searchText;
                    int occurance                      = StringUtils.countMatches(articleCharSeq, searchTextCharSeq);
                    order.add(occurance);
                    filteredList.add(new NewsItem(article.getTitle(),article.getPublishedAt(),article.getUrlToImage()));
                }
            }
        }
        Comparator<NewsItem> comparator = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            comparator = Comparator.comparingInt(newsItem -> order.get(filteredList.indexOf(newsItem)));
            filteredList.sort(comparator);
        }
        Collections.reverse(filteredList);
        mNewsAdapter.setmNewsItemList(filteredList);
        mRecyclerView.setAdapter(mNewsAdapter);
    }

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return super.getViewModelStore();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return super.getLifecycle();
    }
}
