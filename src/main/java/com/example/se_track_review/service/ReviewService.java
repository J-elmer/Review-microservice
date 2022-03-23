package com.example.se_track_review.service;

import com.example.se_track_review.controller.NewReviewDTO;
import com.example.se_track_review.controller.UpdateReviewDTO;
import com.example.se_track_review.controller.ValidReviewDTO;
import com.example.se_track_review.exception.ConcertNotPerformedException;
import com.example.se_track_review.exception.InvalidConcertIdException;
import com.example.se_track_review.exception.InvalidStarsException;
import com.example.se_track_review.model.Review;
import com.example.se_track_review.repository.ReviewRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

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

    public List<Review> findReviewByPerformerId(long performerId) {
       return this.reviewRepository.findByPerformerId(performerId);
    }

    public Review findById(String id) {
        return this.reviewRepository.findById(id).orElseThrow();
    }

    /**
     * method to create new review
     * @param newReviewDTO dto containing the necessary information
     * @throws InvalidConcertIdException if the given concertId is invalid
     * @return Review created
     */
    public Review newReview(NewReviewDTO newReviewDTO) throws InvalidConcertIdException, ConcertNotPerformedException {
        ValidReviewDTO validReview = this.checkIfConcertIdIsValid(newReviewDTO.getConcertId());
        if (validReview.isCanBeReviewed()) {
            Review reviewToBeSaved = new Review(newReviewDTO.getConcertId(), newReviewDTO.getAuthorName(), newReviewDTO.getNumberOfStars(), newReviewDTO.getReviewText(), validReview.getPerformerId());
            return this.reviewRepository.save(reviewToBeSaved);
        }
        return null;
    }

    /**
     * Method to update, does various checks to make sure no invalid values are inserted
     * @param updateReviewDTO contains information needed to update
     * @throws InvalidConcertIdException if concertId is not valid
     * @throws InvalidStarsException if the number of stars is less than 1 or greater than 5
     */
    public void updateReview(UpdateReviewDTO updateReviewDTO) throws InvalidConcertIdException, InvalidStarsException, ConcertNotPerformedException {
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
        if (updateReviewDTO.getReviewText() != null && !updateReviewDTO.getReviewText().equals(reviewToUpdate.getReviewText())) {
            reviewToUpdate.setReviewText(updateReviewDTO.getReviewText());
        }
        this.reviewRepository.save(reviewToUpdate);
    }

    public void deleteReview(String reviewId) throws InvalidConcertIdException {
        this.reviewRepository.deleteById(reviewId);
    }

    /**
     * Method to check if the given concertId is valid, i.e. if it is in the database of the other application
     * @param concertID to check
     * @return true if everything went well
     * @throws InvalidConcertIdException if the id is invalid, this is thrown
     */
    public ValidReviewDTO checkIfConcertIdIsValid(long concertID) throws InvalidConcertIdException, ConcertNotPerformedException {
        WebClient client = WebClient.create();
        ResponseEntity<ValidReviewDTO> response = client.get().uri("http://host.docker.internal:9090/concert/valid-review?id=" + concertID)
                .retrieve()
                .onStatus(
                        status -> status.value() == 400,
                        clientResponse -> Mono.empty()
                )
                .onStatus(
                        status -> status.value() == 404,
                        clientResponse -> Mono.empty()
                )
                .toEntity(ValidReviewDTO.class).block();
        if (response != null && response.getStatusCodeValue() == 404) {
            throw new InvalidConcertIdException();
        }
        if (response != null && response.getStatusCodeValue() == 400) {
            throw new ConcertNotPerformedException();
        }
        assert response != null;
        return response.getBody();
    }
}
