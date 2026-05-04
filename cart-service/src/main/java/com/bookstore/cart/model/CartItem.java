package com.bookstore.cart.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItem implements Serializable {

    private Long productId;
    private String productTitle;
    private int quantity;
    private BigDecimal unitPrice;
}