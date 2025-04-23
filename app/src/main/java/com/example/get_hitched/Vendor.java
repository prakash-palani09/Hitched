package com.example.get_hitched;

import java.io.Serializable;

public class Vendor implements Serializable {
    private String id;
    private String name;
    private String type;
    private String city;
    private String contact;
    private String image_url;
    private double rating;

    public Vendor() {}

    // Constructor with parameters
    public Vendor(String id, String name, String type, String city, String contact, String image_url, double rating) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.city = city;
        this.contact = contact;
        this.image_url = image_url;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
