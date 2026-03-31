package com.example.productapi.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void product_ShouldHaveCorrectGettersAndSetters() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "Test Product";
        Product product = new Product();

        // When
        product.setId(id);
        product.setName(name);

        // Then
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
    }

    @Test
    void product_Constructor_ShouldSetFieldsCorrectly() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "Test Product";

        // When
        Product product = new Product(id, name);

        // Then
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
    }

    @Test
    void product_DefaultConstructor_ShouldCreateEmptyProduct() {
        // When
        Product product = new Product();

        // Then
        assertNotNull(product);
        assertNull(product.getId());
        assertNull(product.getName());
    }

    @Test
    void product_toString_ShouldReturnCorrectFormat() {
        // Given
        UUID id = UUID.randomUUID();
        String name = "Test Product";
        Product product = new Product(id, name);

        // When
        String result = product.toString();

        // Then
        assertTrue(result.contains("Product"));
        assertTrue(result.contains("id=" + id.toString()));
        assertTrue(result.contains("name='Test Product'"));
    }

    @Test
    void product_Equality_ShouldCompareByIdAndName() {
        // Given
        UUID id = UUID.randomUUID();
        Product product1 = new Product(id, "Product");
        Product product2 = new Product(id, "Product");
        Product product3 = new Product(UUID.randomUUID(), "Product");

        // Then
        assertEquals(product1, product2);
        assertNotEquals(product1, product3);
    }

    @Test
    void product_NullValues_ShouldHandleCorrectly() {
        // Given
        Product product = new Product();

        // When
        product.setId(null);
        product.setName(null);

        // Then
        assertNull(product.getId());
        assertNull(product.getName());
    }

    @Test
    void product_Name_ShouldAcceptEmptyString() {
        // Given
        Product product = new Product();

        // When
        product.setName("");

        // Then
        assertEquals("", product.getName());
    }

    @Test
    void product_Name_ShouldAcceptLongString() {
        // Given
        String longName = "A".repeat(1000);
        Product product = new Product();

        // When
        product.setName(longName);

        // Then
        assertEquals(longName, product.getName());
    }
}
