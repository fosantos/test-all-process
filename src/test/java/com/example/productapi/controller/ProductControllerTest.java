package com.example.productapi.controller;

import com.example.productapi.model.Product;
import com.example.productapi.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getAllProducts_ShouldReturnListOfProducts() throws Exception {
        // Given
        Product product1 = new Product(UUID.randomUUID(), "Product 1");
        Product product2 = new Product(UUID.randomUUID(), "Product 2");
        List<Product> products = Arrays.asList(product1, product2);

        when(productService.getAllProducts()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].name").value("Product 2"));
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        Product product = new Product(id, "Test Product");

        when(productService.getProductById(id)).thenReturn(product);

        // When & Then
        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturn404() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        when(productService.getProductById(id)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/products/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void createProduct_ShouldReturnCreatedProduct() throws Exception {
        // Given
        Product product = new Product(null, "New Product");
        Product savedProduct = new Product(UUID.randomUUID(), "New Product");

        when(productService.createProduct(any(Product.class))).thenReturn(savedProduct);

        // When & Then
        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Product\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/products/" + savedProduct.getId().toString()))
                .andExpect(jsonPath("$.name").value("New Product"));
    }

    @Test
    void updateProduct_WhenProductExists_ShouldReturnUpdatedProduct() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        Product productDetails = new Product(null, "Updated Product");
        Product updatedProduct = new Product(id, "Updated Product");

        when(productService.updateProduct(eq(id), any(Product.class))).thenReturn(updatedProduct);

        // When & Then
        mockMvc.perform(put("/products/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Product\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldReturn404() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        Product productDetails = new Product(null, "Updated Product");

        when(productService.updateProduct(id, productDetails)).thenReturn(null);

        // When & Then
        mockMvc.perform(put("/products/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Product\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_WhenProductExists_ShouldReturn204() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        Product product = new Product(id, "Test Product");

        when(productService.getProductById(id)).thenReturn(product);

        // When & Then
        mockMvc.perform(delete("/products/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_WhenProductDoesNotExist_ShouldReturn404() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        when(productService.getProductById(id)).thenReturn(null);

        // When & Then
        mockMvc.perform(delete("/products/{id}", id))
                .andExpect(status().isNotFound());
    }
}
