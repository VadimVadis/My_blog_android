package com.example.my_blog.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String url = "http://193.168.49.71:5000/api/news_category/";

    public boolean isRegistered = false;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private ArrayList<News> news;
    private String name;
    private RequestQueue requestQueue;
    private Button dropdownButton;
    private PopupWindow popupWindow;
    private static final String[] categories_name = {"Все", "Развлечение", "Мир", "Наши новости", "Компьютерные технологии", "Для детей"};


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycleView);
        this.getSupportActionBar().hide();
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        news = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        getNews(0);
        dropdownButton = findViewById(R.id.dropdown_button);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");

        Button createNewsButton = findViewById(R.id.create_button);
        createNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateNewsActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
            }
        });

        dropdownButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, android.view.MotionEvent event) {
                showPopupWindow();
                return false;
            }
        });
    }

    private void showPopupWindow() {
        if (popupWindow == null) {
            // Создание списка кнопок
            List<Button> buttonList = createButtonList();

            // Создание контейнера для списка кнопок
            LinearLayout buttonContainer = new LinearLayout(this);
            buttonContainer.setOrientation(LinearLayout.VERTICAL);
            buttonContainer.setBackgroundResource(R.drawable.popup_background);
            buttonContainer.setPadding(16, 16, 16, 16);

            // Добавление кнопок в контейнер
            for (Button button : buttonList) {
                buttonContainer.addView(button);
            }

            // Создание всплывающего окна
            popupWindow = new PopupWindow(buttonContainer, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
        }

        // Показ всплывающего окна рядом с кнопкой
        popupWindow.showAsDropDown(dropdownButton, 0, -dropdownButton.getHeight(), Gravity.END);
    }

    private List<Button> createButtonList() {
        List<Button> buttonList = new ArrayList<>();
        // Создание кнопок списка
        for (int i = 0; i < categories_name.length; i++) {
            Button button = new Button(this);
            button.setText(categories_name[i]);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Нажата кнопка " + button.getText(), Toast.LENGTH_SHORT).show();
                    for (int i = 0; i <= categories_name.length; i++) {
                        if (categories_name[i].equals(button.getText())) {
                            System.out.println(categories_name[i]);
                            getNews(i);
                            break;
                        }
                    }

                }
            });

            buttonList.add(button);
        }

        return buttonList;
    }
    private void getNews(Integer category) {

        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter != null) {
            int itemCount = adapter.getItemCount();
            adapter.notifyItemRangeRemoved(0, itemCount);
        }
        news.clear();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url + "" + category, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("news");
                            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String title = jsonObject.getString("title");
                                String name = jsonObject.getJSONObject("user").getString("name");
                                String pictureUrl = jsonObject.getString("img");
                                int category_id = jsonObject.getInt("category_id");
                                String content = jsonObject.getString("content");

                                News new1 = new News();
                                new1.setTitle(title);
                                new1.setCategory(category_id);
                                if (!pictureUrl.isEmpty()){
                                    new1.setPictureUrl("http://193.168.49.71:5000/static/img_news/" + pictureUrl);
                                    System.out.println(new1.getPictureUrl());
                                }
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