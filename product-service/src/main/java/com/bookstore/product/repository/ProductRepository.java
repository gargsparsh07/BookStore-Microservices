package com.bookstore.product.repository;

import com.bookstore.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Product> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title, String author, Pageable pageable);
}