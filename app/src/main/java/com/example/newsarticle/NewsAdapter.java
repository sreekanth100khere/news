package com.example.newsarticle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import android.graphics.drawable.ColorDrawable;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.text.ParseException;
import java.util.Date;
import com.example.jsonexample.R;





import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsItem> mNewsItemList;

    public void setmNewsItemList(List<NewsItem> iNewsItemList) {
        this.mNewsItemList = iNewsItemList;
    }

    public  List<NewsItem> getmNewsItemList(){
        return mNewsItemList;
    }

    public NewsAdapter(List<NewsItem> iNewsItemList) {
        this.mNewsItemList = iNewsItemList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem newsItem = mNewsItemList.get(position);
        holder.titleTextView.setText(newsItem.getTitle());
        holder.dateTextView.setText(convertToDisplayableFormat(newsItem.getPublishedDate())+" GMT");
        // Load image using Picasso/Glide if imageUrl is available
        Glide.with(holder.imageView.getContext()).load(newsItem.getImageUrl())
                .placeholder(new ColorDrawable(ContextCompat.getColor(holder.imageView.getContext(), R.color.placeholder_gray)))
                .error(new ColorDrawable(ContextCompat.getColor(holder.imageView.getContext(), R.color.placeholder_gray)))
                .into(holder.imageView);
    }

    public String convertToDisplayableFormat(String iDateString){
        // Define the pattern for parsing
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Set timezone to UTC

        // Parse the string into a Date object
        try {
            Date date = inputFormat.parse(iDateString);

            // Define the pattern for display
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            // Convert Date object to displayable format
            String displayableDate = outputFormat.format(date);

            return displayableDate;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    @Override
    public int getItemCount() {
        return mNewsItemList.size();
    }

    public void filterList(List<NewsItem> filteredList) {
        mNewsItemList = filteredList;
        notifyDataSetChanged();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;
        ImageView imageView;

        NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView   = itemView.findViewById(R.id.id_title_tv);
            dateTextView    = itemView.findViewById(R.id.id_published_date_tv);
            imageView       = itemView.findViewById(R.id.id_news_iv);
        }
    }
}
