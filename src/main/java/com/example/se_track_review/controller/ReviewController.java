package com.example.se_track_review.controller;

import com.example.se_track_review.exception.InvalidConcertIdException;
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

    @GetMapping(value="test")
    public ResponseEntity<String> addReview(@RequestBody Review review) {
        try {
            this.reviewService.addReview(review);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (InvalidConcertIdException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid concertId");
        }
    }
}
