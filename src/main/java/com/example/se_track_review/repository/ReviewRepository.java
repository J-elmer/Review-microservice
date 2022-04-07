package com.example.se_track_review.repository;

import com.example.se_track_review.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByNumberOfStars(int numberOfStars);

    List<Review> findByNumberOfStarsGreaterThanEqual(int numberOfStars);

    List<Review> findByNumberOfStarsLessThanEqual(int numberOfStars);

    List<Review> findByPerformerId(long performerId);

    List<Review> findByConcertId(long concertId);

    List<Review> findReviewByReviewTextContainsIgnoreCase(String term);
}
