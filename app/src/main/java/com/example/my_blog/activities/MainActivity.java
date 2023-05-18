package com.example.my_blog.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.my_blog.R;
import com.example.my_blog.data.NewsAdapter;
import com.example.my_blog.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String url = "http://193.168.49.71:5000/api/news";

    public boolean isRegistered = false;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private ArrayList<News> news;
    private String category = "";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        news = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        getNews(category);


    }

    private void getNews(String category) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + category, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("news");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String title = jsonObject.getString("title");
                                String name = jsonObject.getJSONObject("user").getString("name");
                                String pictureUrl = jsonObject.getString("img");
                                String category = jsonObject.getString("category_id");
                                String content = jsonObject.getString("content");

                                News new1 = new News();
                                new1.setTitle(title);
                                if (!pictureUrl.isEmpty()){
                                    new1.setPictureUrl("http://193.168.49.71:5000/static/img_news/" + pictureUrl);
                                    System.out.println(new1.getPictureUrl());
                                }
                                switch (category) {
                                    case  ("1"):
                                        new1.setCategory("Развлечение");
                                        break;
                                    case ("2"):
                                        new1.setCategory("Мир");
                                        break;
                                    case  ("3"):
                                        new1.setCategory("Наши новости");
                                        break;
                                    case ("4"):
                                        new1.setCategory("Компьютерные технологии");
                                        break;
                                    case ("5"):
                                        new1.setCategory("Для детей");
                                        break;}
                                new1.setName(name);
                                new1.setContent(content);

                                news.add(new1);

                            }
                            newsAdapter = new NewsAdapter(MainActivity.this, news);
                            recyclerView.setAdapter(newsAdapter);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }
}