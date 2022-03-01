package com.example.se_track_review.controller;

import com.example.se_track_review.exception.InvalidConcertIdException;
import com.example.se_track_review.exception.InvalidStarsException;
import com.example.se_track_review.model.Review;
import com.example.se_track_review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("review")
public class ReviewController {

    private ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping(value = "/all")
    public List<Review> getAllReviews() {
        return this.reviewService.getAllReviews();
    }

    @GetMapping(value = "/stars")
    public List<Review> getAllReviews(@RequestParam int stars) {
        return this.reviewService.findReviewsByNrOfStarts(stars);
    }

    @GetMapping(value = "/max-stars")
    public List<Review> getReviewsWithMaxStars(@RequestParam int stars) {
        return this.reviewService.findReviewsWithMaxStars(stars);
    }

    @GetMapping(value = "/min-stars")
    public List<Review> getReviewsWithMinStars(@RequestParam int stars) {
        return this.reviewService.findReviewsWithMinStars(stars);
    }

    @PostMapping(value="new")
    public ResponseEntity<?> newReview(@RequestBody NewReviewDTO newReviewDTO) {
        try {
            this.reviewService.newReview(newReviewDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (InvalidConcertIdException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseDTO("Invalid concertId"));
        }
    }

    @PutMapping(value="update")
    public ResponseEntity<?> updateReview(@RequestBody UpdateReviewDTO updateReviewDTO) {
        try {
            this.reviewService.updateReview(updateReviewDTO);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (InvalidConcertIdException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseDTO("Invalid concertId"));
        } catch (InvalidStarsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseDTO("Number of stars must be between 1 and 5"));
        }
    }

    @DeleteMapping(value="delete")
    public ResponseEntity<?> deleteReview(@RequestBody String reviewId) {
        try {
            this.reviewService.deleteReview(reviewId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (InvalidConcertIdException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseDTO("Invalid concertId"));
        }
    }
}
