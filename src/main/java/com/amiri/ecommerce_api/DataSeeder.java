package com.amiri.ecommerce_api;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            return;
        }

        productRepository.saveAll(List.of(
                new Product("1", "Red Lipstick", "A bold red lipstick", 12.99,
                        "https://placehold.co/300", 4.5, 10, true, "beauty"),
                new Product("2", "Wooden Chair", "A simple wooden chair", 49.5,
                        "https://placehold.co/300", 4.1, 3, true, "furniture"),
                new Product("3", "Desk Lamp", "A bright desk lamp", 24.0,
                        "https://placehold.co/300", 3.9, 7, false, "home-decoration")
        ));
    }
}
