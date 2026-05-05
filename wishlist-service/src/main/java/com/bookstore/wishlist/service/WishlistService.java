package com.bookstore.wishlist.service;

import com.bookstore.wishlist.entity.Wishlist;
import com.bookstore.wishlist.entity.WishlistItem;
import com.bookstore.wishlist.exception.ResourceNotFoundException;
import com.bookstore.wishlist.repository.WishlistItemRepository;
import com.bookstore.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;

    public Wishlist getWishlist(Long userId) {
        return wishlistRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Wishlist wishlist = new Wishlist();
                    wishlist.setUserId(userId);
                    return wishlistRepository.save(wishlist);
                });
    }

    public Wishlist addItem(Long userId, Long productId) {
        Wishlist wishlist = getWishlist(userId);
        boolean exists = wishlist.getItems().stream()
                .anyMatch(i -> i.getProductId().equals(productId));
        if (!exists) {
            WishlistItem item = new WishlistItem();
            item.setProductId(productId);
            item.setWishlist(wishlist);
            wishlist.getItems().add(item);
            wishlistRepository.save(wishlist);
        }
        return wishlist;
    }

    @Transactional
    public Wishlist removeItem(Long userId, Long productId) {
        Wishlist wishlist = getWishlist(userId);
        wishlistItemRepository.deleteByWishlistIdAndProductId(
                wishlist.getId(), productId);
        return getWishlist(userId);
    }

    @Transactional
    public void clearWishlist(Long userId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Wishlist not found"));
        wishlist.getItems().clear();
        wishlistRepository.save(wishlist);
    }
}