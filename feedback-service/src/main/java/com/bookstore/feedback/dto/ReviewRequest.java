package com.bookstore.feedback.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Comment is required")
    private String comment;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;
}