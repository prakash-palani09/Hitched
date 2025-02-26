package com.example.get_hitched;

public class SpinnerItem {
        private String name;
        private double cost;
        private int imageResId; // Resource ID for the image

        // Constructor
        public SpinnerItem(String name, double cost) {
            this.name = name;
            this.cost = cost;
        }

        // Getter for name
        public String getName() {
            return name;
        }

        // Getter for cost
        public double getCost() {
            return cost;
        }

        // Getter for image resource ID

        @Override
        public String toString() {
            return name; // This will be shown in the ListView
        }
}
