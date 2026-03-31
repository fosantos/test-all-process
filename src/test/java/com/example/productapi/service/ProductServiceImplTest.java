package com.example.productapi.service;

import com.example.productapi.model.Product;
import com.example.productapi.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Given
        Product product1 = new Product(UUID.randomUUID(), "Product 1");
        Product product2 = new Product(UUID.randomUUID(), "Product 2");
        List<Product> products = Arrays.asList(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        // When
        List<Product> result = productService.getAllProducts();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        assertEquals("Product 2", result.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        // Given
        UUID id = UUID.randomUUID();
        Product product = new Product(id, "Test Product");
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        // When
        Product result = productService.getProductById(id);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Test Product", result.getName());
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void getProductById_WhenProductDoesNotExist_ShouldReturnNull() {
        // Given
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Product result = productService.getProductById(id);

        // Then
        assertNull(result);
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    void createProduct_ShouldSaveAndReturnProduct() {
        // Given
        Product product = new Product(null, "New Product");
        Product savedProduct = new Product(UUID.randomUUID(), "New Product");

        when(productRepository.save(product)).thenReturn(savedProduct);

        // When
        Product result = productService.createProduct(product);

        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("New Product", result.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void updateProduct_WhenProductExists_ShouldUpdateAndReturnProduct() {
        // Given
        UUID id = UUID.randomUUID();
        Product existingProduct = new Product(id, "Old Name");
        Product productDetails = new Product(null, "Updated Name");

        when(productRepository.findById(id)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        // When
        Product result = productService.updateProduct(id, productDetails);

        // Then
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Updated Name", result.getName());
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    void updateProduct_WhenProductDoesNotExist_ShouldReturnNull() {
        // Given
        UUID id = UUID.randomUUID();
        Product productDetails = new Product(null, "Updated Name");

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Product result = productService.updateProduct(id, productDetails);

        // Then
        assertNull(result);
        verify(productRepository, times(1)).findById(id);
        verify(productRepository, never()).save(any());
    }

    @Test
    void deleteProduct_ShouldCallDeleteById() {
        // Given
        UUID id = UUID.randomUUID();

        // When
        productService.deleteProduct(id);

        // Then
        verify(productRepository, times(1)).deleteById(id);
    }
}
