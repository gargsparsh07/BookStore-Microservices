package com.bookstore.cart.service;

import com.bookstore.cart.exception.ResourceNotFoundException;
import com.bookstore.cart.model.Cart;
import com.bookstore.cart.model.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CART_KEY = "cart:";

    public Cart getCart(String userId) {
        Cart cart = (Cart) redisTemplate.opsForValue()
                .get(CART_KEY + userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
        }
        return cart;
    }

    public Cart addItem(String userId, CartItem item) {
        Cart cart = getCart(userId);
        List<CartItem> items = cart.getItems();
        boolean found = false;
        for (CartItem existing : items) {
            if (existing.getProductId().equals(item.getProductId())) {
                existing.setQuantity(existing.getQuantity() + item.getQuantity());
                found = true;
                break;
            }
        }
        if (!found) {
            items.add(item);
        }
        cart.setItems(items);
        cart.setTotalAmount(calculateTotal(items));
        redisTemplate.opsForValue().set(CART_KEY + userId, cart);
        return cart;
    }

    public Cart updateItem(String userId, Long productId, int quantity) {
        Cart cart = getCart(userId);
        List<CartItem> items = cart.getItems();
        items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .ifPresent(i -> i.setQuantity(quantity));
        cart.setTotalAmount(calculateTotal(items));
        redisTemplate.opsForValue().set(CART_KEY + userId, cart);
        return cart;
    }

    public Cart removeItem(String userId, Long productId) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(i -> i.getProductId().equals(productId));
        cart.setTotalAmount(calculateTotal(cart.getItems()));
        redisTemplate.opsForValue().set(CART_KEY + userId, cart);
        return cart;
    }

    public void clearCart(String userId) {
        redisTemplate.delete(CART_KEY + userId);
    }

    public BigDecimal getTotal(String userId) {
        return getCart(userId).getTotalAmount();
    }

    private BigDecimal calculateTotal(List<CartItem> items) {
        return items.stream()
                .map(i -> i.getUnitPrice()
                        .multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}