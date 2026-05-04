package com.bookstore.product.service;

import com.bookstore.product.dto.*;
import com.bookstore.product.entity.Category;
import com.bookstore.product.entity.Product;
import com.bookstore.product.exception.ResourceNotFoundException;
import com.bookstore.product.repository.CategoryRepository;
import com.bookstore.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::mapToResponse);
    }

    public ProductResponse getProductById(Long id) {
        return mapToResponse(findProduct(id));
    }

    public Page<ProductResponse> searchProducts(String query, Pageable pageable) {
        return productRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                        query, query, pageable)
                .map(this::mapToResponse);
    }

    public Page<ProductResponse> getProductsByCategory(Long categoryId,
                                                       Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(this::mapToResponse);
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        mapToEntity(request, product);
        return mapToResponse(productRepository.save(product));
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findProduct(id);
        mapToEntity(request, product);
        return mapToResponse(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        findProduct(id);
        productRepository.deleteById(id);
    }

    public Category createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category already exists");
        }
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        return categoryRepository.save(category);
    }

    public java.util.List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    private void mapToEntity(ProductRequest request, Product product) {
        product.setTitle(request.getTitle());
        product.setAuthor(request.getAuthor());
        product.setIsbn(request.getIsbn());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setDescription(request.getDescription());
        if (request.getCategoryId() != null) {
            Category category = categoryRepository
                    .findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found"));
            product.setCategory(category);
        }
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + id));
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setTitle(product.getTitle());
        response.setAuthor(product.getAuthor());
        response.setIsbn(product.getIsbn());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setImageUrl(product.getImageUrl());
        response.setDescription(product.getDescription());
        if (product.getCategory() != null) {
            response.setCategoryName(product.getCategory().getName());
        }
        return response;
    }
}