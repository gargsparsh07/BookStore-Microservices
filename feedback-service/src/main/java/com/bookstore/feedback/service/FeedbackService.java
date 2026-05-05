package com.bookstore.feedback.service;

import com.bookstore.feedback.dto.*;
import com.bookstore.feedback.entity.Review;
import com.bookstore.feedback.exception.ResourceNotFoundException;
import com.bookstore.feedback.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final ReviewRepository reviewRepository;

    public ReviewResponse submitReview(Long userId, ReviewRequest request) {
        Review review = new Review();
        review.setUserId(userId);
        review.setProductId(request.getProductId());
        review.setComment(request.getComment());
        review.setRating(request.getRating());
        return mapToResponse(reviewRepository.save(review));
    }

    public List<ReviewResponse> getProductReviews(Long productId) {
        return reviewRepository.findByProductId(productId)
                .stream().map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Double getAverageRating(Long productId) {
        Double avg = reviewRepository
                .findAverageRatingByProductId(productId);
        return avg != null ? avg : 0.0;
    }

    public ReviewResponse updateReview(Long id, Long userId,
                                       ReviewRequest request) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Review not found with id: " + id));
        if (!review.getUserId().equals(userId)) {
            throw new RuntimeException(
                    "You can only edit your own reviews");
        }
        review.setComment(request.getComment());
        review.setRating(request.getRating());
        return mapToResponse(reviewRepository.save(review));
    }

    public void deleteReview(Long id) {
        reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Review not found with id: " + id));
        reviewRepository.deleteById(id);
    }

    private ReviewResponse mapToResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getProductId(),
                review.getUserId(),
                review.getComment(),
                review.getRating(),
                review.getCreatedAt());
    }
}