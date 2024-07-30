package com.microservices.orderService.consumer;



import com.fasterxml.jackson.databind.ObjectMapper;

import com.microservices.orderService.entity.OrderEntity;
import com.microservices.orderService.entity.Product;
import com.microservices.orderService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ProductConsumer {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private OrderRepository orderRepository;
    @KafkaListener(topics = "saving_data", groupId = "1234")
    public void consume(String message) {
        try {
            Product product = objectMapper.readValue(message, Product.class);
            OrderEntity orderEntity = new OrderEntity();
            orderEntity.setProduct(product.getName());
            orderEntity.setCustomerName("Random");
            orderEntity.setPrice(product.getPrice());
            orderEntity.setQuantity(5);
            orderRepository.save(orderEntity);
            System.out.println("Consumed product: " + product);
        } catch (Exception e) {
            System.err.println("Failed to deserialize product: " + e.getMessage());
        }
    }
}
