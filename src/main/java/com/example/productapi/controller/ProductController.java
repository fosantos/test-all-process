package com.example.productapi.controller;

import com.example.productapi.model.Product;
import com.example.productapi.repository.ProductRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@Tag(name = "Product", description = "Product management APIs")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // GET /products - get all products
    @Operation(summary = "Get all products", description = "Retrieve a list of all products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class)))
    })
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // GET /products/{id} - get a product by id
    @Operation(summary = "Get product by ID", description = "Retrieve a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved product",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@Parameter(description = "ID of the product to be retrieved", required = true) @PathVariable UUID id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok().body(product))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /products - create a new product
    @Operation(summary = "Create a new product", description = "Create a new product with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Product> createProduct(@Parameter(description = "Product object to be created", required = true) @RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.created(URI.create("/products/" + savedProduct.getId()))
                .body(savedProduct);
    }

    // PUT /products/{id} - update an existing product
    @Operation(summary = "Update an existing product", description = "Update an existing product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@Parameter(description = "ID of the product to be updated", required = true) @PathVariable UUID id,
                                                 @Parameter(description = "Updated product details", required = true) @RequestBody Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    Product updatedProduct = productRepository.save(product);
                    return ResponseEntity.ok().body(updatedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /products/{id} - delete a product
    @Operation(summary = "Delete a product", description = "Delete a product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@Parameter(description = "ID of the product to be deleted", required = true) @PathVariable UUID id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}