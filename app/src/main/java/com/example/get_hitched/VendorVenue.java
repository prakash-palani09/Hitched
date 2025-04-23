package com.example.get_hitched;

public class VendorVenue {
    public String name, location, city, price, description, imageUrl;
    public float rating;

    public VendorVenue() {}

    public VendorVenue(String name, String location, String city, String price, String description, String imageUrl, float rating) {
        this.name = name;
        this.location = location;
        this.city = city;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rating = rating;
    }
}

