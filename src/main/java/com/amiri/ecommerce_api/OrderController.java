package com.amiri.ecommerce_api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    record OrderItemRequest(String productId, int quantity) {}
    record OrderRequest(String userId, List<OrderItemRequest> items) {}

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order placeOrder(@RequestBody OrderRequest request) {
        if (request.items() == null || request.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order has no items");
        }

        List<OrderItem> items = request.items().stream().map(item -> {
            if (item.quantity() < 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be at least 1");
            }
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown product"));
            if (!product.isInStock()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Product out of stock");
            }
            return new OrderItem(product.getId(), item.quantity(), product.getPrice());
        }).toList();

        return orderRepository.save(new Order(request.userId(), items));
    }
}
