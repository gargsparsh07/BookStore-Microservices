package com.bookstore.feedback.controller;

import com.bookstore.feedback.dto.*;
import com.bookstore.feedback.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Product feedback and review APIs")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Operation(summary = "Submit a product review")
    @PostMapping
    public ResponseEntity<ReviewResponse> submitReview(
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(
                feedbackService.submitReview(userId, request));
    }

    @Operation(summary = "Get all reviews for a product")
    @GetMapping("/product/{id}")
    public ResponseEntity<List<ReviewResponse>> getProductReviews(
            @PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getProductReviews(id));
    }

    @Operation(summary = "Get average rating for a product")
    @GetMapping("/product/{id}/rating")
    public ResponseEntity<Double> getAverageRating(
            @PathVariable Long id) {
        return ResponseEntity.ok(feedbackService.getAverageRating(id));
    }

    @Operation(summary = "Edit own review")
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long id,
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(
                feedbackService.updateReview(id, userId, request));
    }

    @Operation(summary = "Delete a review")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        feedbackService.deleteReview(id);
        return ResponseEntity.ok("Review deleted successfully");
    }
}