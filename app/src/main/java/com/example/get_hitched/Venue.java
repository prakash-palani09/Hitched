package com.example.get_hitched;

public class Venue {
    public String id;
    public String name;
    public String location;
    public String contact;
    public String image_url;
    public int capacity;
    public int price_per_plate;
    public double rating;

    public Venue() {} // Default constructor for Firebase

    public Venue(String id, String name, String location, String contact,
                 String image_url, int capacity, int price_per_plate, double rating) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.contact = contact;
        this.image_url = image_url;
        this.capacity = capacity;
        this.price_per_plate = price_per_plate;
        this.rating = rating;
    }
}
