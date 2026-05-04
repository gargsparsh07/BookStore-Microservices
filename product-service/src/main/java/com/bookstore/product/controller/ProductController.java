package com.bookstore.product.controller;

import com.bookstore.product.dto.*;
import com.bookstore.product.entity.Category;
import com.bookstore.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management APIs")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get all products paginated")
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Search products by title or author")
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestParam String q, Pageable pageable) {
        return ResponseEntity.ok(productService.searchProducts(q, pageable));
    }

    @Operation(summary = "Get products by category")
    @GetMapping("/category/{id}")
    public ResponseEntity<Page<ProductResponse>> getByCategory(
            @PathVariable Long id, Pageable pageable) {
        return ResponseEntity.ok(
                productService.getProductsByCategory(id, pageable));
    }

    @Operation(summary = "Create a new product")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }

    @Operation(summary = "Update a product")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @Operation(summary = "Delete a product")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @Operation(summary = "Get all categories")
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(productService.getAllCategories());
    }

    @Operation(summary = "Create a new category")
    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(productService.createCategory(request));
    }
}