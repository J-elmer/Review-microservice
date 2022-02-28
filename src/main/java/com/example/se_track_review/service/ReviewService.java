package com.example.se_track_review.service;

import com.example.se_track_review.exception.InvalidConcertIdException;
import com.example.se_track_review.model.Review;
import com.example.se_track_review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<Review> getAllReviews() {
        return this.reviewRepository.findAll();
    }

    public List<Review> findReviewsByNrOfStarts(int numberOfStars) {
        return this.reviewRepository.findByNumberOfStars(numberOfStars);
    }

    public List<Review> findReviewsWithMinStars(int numberOfStars) {
        return this.reviewRepository.findByNumberOfStarsGreaterThanEqual(numberOfStars);
    }

    public List<Review> findReviewsWithMaxStars(int numberOfStars) {
        return this.reviewRepository.findByNumberOfStarsLessThanEqual(numberOfStars);
    }

    public void addReview(Review review) throws InvalidConcertIdException {
        try {
            this.connectToApi("http://localhost:9090/concert/" + review.getConcertId());
        } catch (HttpClientErrorException e) {
            throw new InvalidConcertIdException();
        }
        this.reviewRepository.save(review);
    }

    private String connectToApi(String apiUri) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(apiUri, String.class);
    }
}
