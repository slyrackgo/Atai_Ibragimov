package com.example.Atai_Ibragimov.model;

public class Advert {
    private String title;
    private String price;
    private String city;
    private String date;
    private String imageUrl;
    private String url;

    public Advert() {}

    public Advert(String title, String price, String city, String date, String imageUrl, String url) {
        this.title = title;
        this.price = price;
        this.city = city;
        this.date = date;
        this.imageUrl = imageUrl;
        this.url = url;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    @Override
    public String toString() {
        return String.format("Advert{title='%s', price='%s', city='%s', date='%s'}",
                title, price, city, date);
    }
}