package com.microservices.orderService.service;


import com.microservices.orderService.entity.OrderEntity;
import com.microservices.orderService.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;

//    private static final String TOPIC = "order-topic";

    public List<OrderEntity> findAll() {
        return orderRepository.findAll();
    }

    public OrderEntity findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public OrderEntity save(OrderEntity order) {
        // Save or update the order
        OrderEntity savedOrder = orderRepository.save(order);
        // Send a Kafka message with the saved order details
//        kafkaTemplate.send(TOPIC, savedOrder);
        return savedOrder;
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
