package com.example.productapi.service;

import com.example.productapi.model.Product;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<Product> getAllProducts();

    Product getProductById(UUID id);

    Product createProduct(Product product);

    Product updateProduct(UUID id, Product productDetails);

    void deleteProduct(UUID id);
}