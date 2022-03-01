package com.example.se_track_review.service;

import com.example.se_track_review.controller.NewReviewDTO;
import com.example.se_track_review.controller.UpdateReviewDTO;
import com.example.se_track_review.exception.InvalidConcertIdException;
import com.example.se_track_review.exception.InvalidStarsException;
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

    public void newReview(NewReviewDTO newReviewDTO) throws InvalidConcertIdException {
        try {
            this.checkIfConcertIdIsValid(newReviewDTO.getConcertId());
        } catch (HttpClientErrorException e) {
            throw new InvalidConcertIdException();
        }
        Review reviewToBeSaved = new Review(newReviewDTO.getConcertId(), newReviewDTO.getAuthorName(), newReviewDTO.getNumberOfStars());
        this.reviewRepository.save(reviewToBeSaved);
    }

    public void updateReview(UpdateReviewDTO updateReviewDTO) throws InvalidConcertIdException, InvalidStarsException {
        Review reviewToUpdate = this.reviewRepository.findById(updateReviewDTO.getReviewId()).orElseThrow();
        if (updateReviewDTO.getConcertId() < 0) {
            throw new InvalidConcertIdException();
        }
        if (updateReviewDTO.getConcertId() > 0) {
            this.checkIfConcertIdIsValid(updateReviewDTO.getConcertId());
        }
        if (updateReviewDTO.getNumberOfStars() < 0 || updateReviewDTO.getNumberOfStars() > 5) {
            throw new InvalidStarsException();
        }
        if (updateReviewDTO.getConcertId() > 0 && updateReviewDTO.getConcertId() != reviewToUpdate.getConcertId()) {
            reviewToUpdate.setConcertId(updateReviewDTO.getConcertId());
        }
        if (updateReviewDTO.getAuthorName() != null && !updateReviewDTO.getAuthorName().equals(reviewToUpdate.getAuthorName())) {
            reviewToUpdate.setAuthorName(updateReviewDTO.getAuthorName());
        }
        if (updateReviewDTO.getNumberOfStars() > 0 && updateReviewDTO.getNumberOfStars() != reviewToUpdate.getNumberOfStars()) {
            reviewToUpdate.setNumberOfStars(updateReviewDTO.getNumberOfStars());
        }
        this.reviewRepository.save(reviewToUpdate);
    }

    public void deleteReview(String reviewId) throws InvalidConcertIdException {
        this.reviewRepository.deleteById(reviewId);
    }

    private boolean checkIfConcertIdIsValid(long concertID) throws InvalidConcertIdException {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getForObject("http://localhost:9090/concert/" + concertID, String.class);
        } catch (HttpClientErrorException e) {
            throw new InvalidConcertIdException();
        }
        return true;
    }
}
