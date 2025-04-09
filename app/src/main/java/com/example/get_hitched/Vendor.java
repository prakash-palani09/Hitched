package com.example.get_hitched;

public class Vendor {
    public String id;
    public String name;
    public String type;
    public String city;
    public String contact;
    public String image_url;
    public double rating;

    public Vendor() {} // Default constructor for Firebase

    public Vendor(String id, String name, String type, String city, String contact, String image_url, double rating) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.city = city;
        this.contact = contact;
        this.image_url = image_url;
        this.rating = rating;
    }
}
