package com.amiri.ecommerce_api;

import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String userId;
    private double total;
    private String paymentStatus;

    @ElementCollection
    private List<OrderItem> items;

    protected Order() {
    }

    public Order(String userId, List<OrderItem> items) {
        this.userId = userId;
        this.items = items;
        this.paymentStatus = "success";
        this.total = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public double getTotal() { return total; }
    public String getPaymentStatus() { return paymentStatus; }
    public List<OrderItem> getItems() { return items; }
}
