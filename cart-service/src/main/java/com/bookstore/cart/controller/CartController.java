package com.bookstore.cart.controller;

import com.bookstore.cart.model.Cart;
import com.bookstore.cart.model.CartItem;
import com.bookstore.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Cart management APIs")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "Get current user's cart")
    @GetMapping
    public ResponseEntity<Cart> getCart(
            @RequestHeader("userId") String userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @Operation(summary = "Add item to cart")
    @PostMapping("/add")
    public ResponseEntity<Cart> addItem(
            @RequestHeader("userId") String userId,
            @RequestBody CartItem item) {
        return ResponseEntity.ok(cartService.addItem(userId, item));
    }

    @Operation(summary = "Update item quantity")
    @PutMapping("/update")
    public ResponseEntity<Cart> updateItem(
            @RequestHeader("userId") String userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        return ResponseEntity.ok(
                cartService.updateItem(userId, productId, quantity));
    }

    @Operation(summary = "Remove item from cart")
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> removeItem(
            @RequestHeader("userId") String userId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeItem(userId, productId));
    }

    @Operation(summary = "Clear entire cart")
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(
            @RequestHeader("userId") String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }

    @Operation(summary = "Get cart total price")
    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotal(
            @RequestHeader("userId") String userId) {
        return ResponseEntity.ok(cartService.getTotal(userId));
    }
}