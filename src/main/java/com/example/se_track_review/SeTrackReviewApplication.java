package com.example.se_track_review;

import com.example.se_track_review.model.Review;
import com.example.se_track_review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class SeTrackReviewApplication {

    @Autowired
    private ReviewRepository reviewRepository;

    public static void main(String[] args) {
        SpringApplication.run(SeTrackReviewApplication.class, args);
    }
    
}
