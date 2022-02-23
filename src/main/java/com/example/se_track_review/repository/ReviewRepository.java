package com.example.se_track_review.repository;

import com.example.se_track_review.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, Long> {
}
