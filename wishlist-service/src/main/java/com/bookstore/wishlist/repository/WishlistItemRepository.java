package com.bookstore.wishlist.repository;

import com.bookstore.wishlist.entity.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistItemRepository
        extends JpaRepository<WishlistItem, Long> {
    void deleteByWishlistIdAndProductId(Long wishlistId, Long productId);
}