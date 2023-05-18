package com.example.my_blog.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.example.my_blog.R;
import com.example.my_blog.model.News;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter <NewsAdapter.NewsViewHolder>{

    private Context context;
    private ArrayList <News> news;

    public NewsAdapter(Context context, ArrayList <News> news){
        this.context = context;
        this.news = news;


    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News currentnews = news.get(position);
        String title = currentnews.getTitle();
        String name = currentnews.getName();
        String pictureUrl = currentnews.getPictureUrl();
        String category = currentnews.getCategory();
        String instructions = currentnews.getContent();

        holder.titleTextView.setText(title);
        holder.nameTextView.setText(name);
        Picasso.get().load(pictureUrl).fit().centerInside().into(holder.pictureImageView);
        holder.categoryTextView.setText(category);
        holder.contentTextView.setText(instructions);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    public  class NewsViewHolder extends RecyclerView.ViewHolder{

        ImageView pictureImageView;
        TextView titleTextView;
        TextView nameTextView;
        TextView categoryTextView;
        TextView contentTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            pictureImageView = itemView.findViewById(R.id.pictureImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
        }
    }
}
