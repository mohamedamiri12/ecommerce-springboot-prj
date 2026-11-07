package com.amiri.ecommerce_api;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Product {

    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private double rating;
    private int reviewCount;
    private boolean inStock;
    private String category;

    protected Product() {
    }

    public Product(String id, String name, String description, double price, String imageUrl,
                   double rating, int reviewCount, boolean inStock, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.inStock = inStock;
        this.category = category;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public double getRating() { return rating; }
    public int getReviewCount() { return reviewCount; }
    public boolean isInStock() { return inStock; }
    public String getCategory() { return category; }
}
