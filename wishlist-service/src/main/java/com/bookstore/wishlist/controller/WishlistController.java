package com.bookstore.wishlist.controller;

import com.bookstore.wishlist.entity.Wishlist;
import com.bookstore.wishlist.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "Wishlist management APIs")
public class WishlistController {

    private final WishlistService wishlistService;

    @Operation(summary = "Get authenticated user's wishlist")
    @GetMapping
    public ResponseEntity<Wishlist> getWishlist(
            @RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(wishlistService.getWishlist(userId));
    }

    @Operation(summary = "Add product to wishlist")
    @PostMapping("/add/{productId}")
    public ResponseEntity<Wishlist> addItem(
            @RequestHeader("userId") Long userId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.addItem(userId, productId));
    }

    @Operation(summary = "Remove product from wishlist")
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Wishlist> removeItem(
            @RequestHeader("userId") Long userId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.removeItem(userId, productId));
    }

    @Operation(summary = "Clear entire wishlist")
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearWishlist(
            @RequestHeader("userId") Long userId) {
        wishlistService.clearWishlist(userId);
        return ResponseEntity.ok("Wishlist cleared successfully");
    }
}