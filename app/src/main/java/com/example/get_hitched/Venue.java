package com.example.get_hitched;

import java.io.Serializable;

public class Venue implements Serializable {
    private String id;
    private String name;
    private String location;
    private String contact;
    private String image_url;
    private int capacity;
    private int price_per_plate;
    private double rating;

    // Default constructor for Firebase
    public Venue() {}

    // Constructor with parameters
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

    // Getters and Setters for each field
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getPrice_per_plate() {
        return price_per_plate;
    }

    public void setPrice_per_plate(int price_per_plate) {
        this.price_per_plate = price_per_plate;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
