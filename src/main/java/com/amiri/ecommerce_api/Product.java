package com.amiri.ecommerce_api;

public record Product(
        String id,
        String name,
        String description,
        double price,
        String imageUrl,
        double rating,
        int reviewCount,
        boolean inStock,
        String category
) {
}
