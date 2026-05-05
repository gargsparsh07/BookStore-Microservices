package com.bookstore.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

    private Long id;
    private Long productId;
    private Long userId;
    private String comment;
    private int rating;
    private LocalDateTime createdAt;
}