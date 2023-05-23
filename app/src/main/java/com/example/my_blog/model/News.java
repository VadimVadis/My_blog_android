package com.example.my_blog.model;

public class News {
    private String title;
    private String pictureUrl;
    private int category_id;
    private String content;
    private String name;

    public News() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    private static final String[] categories_name = {"Развлечение", "Мир", "Наши новости", "Компьютерные технологии", "Для детей"};
    public String getCategory() {
        if (category_id < 1 || category_id >= categories_name.length){
            return "-------";
        }
        return categories_name[category_id-1];
    }

    public void setCategory(int category_id) {
        this.category_id = category_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
