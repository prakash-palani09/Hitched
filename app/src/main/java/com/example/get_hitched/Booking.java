package com.example.get_hitched;

import java.io.Serializable;

public class Booking implements Serializable {
    public String name;
    public String location;
    public String date;
    public String price;
    public String userId;

    // No-argument constructor (required for Firestore deserialization)
    public Booking() {
        // This constructor is required for Firestore to map documents to objects
    }

    public Booking(String name, String location, String price, String date, String userId) {
        this.name = name;
        this.location = location;
        this.price = price;
        this.date = date;
        this.userId = userId;
    }
}
