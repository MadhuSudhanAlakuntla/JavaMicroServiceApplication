package com.microservices.productService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservices.productService.entity.Product;
import com.microservices.productService.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String TOPIC = "saving_data";

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product save(Product product) {
        Product savedProduct = productRepository.save(product);
        try {
            String productJson = objectMapper.writeValueAsString(savedProduct);
            kafkaTemplate.send(TOPIC, productJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize product", e);
        }
        return savedProduct;
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
