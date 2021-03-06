package com.example.se_track_review.controller;

import com.example.se_track_review.controller.DTO.JsonResponseDTO;
import com.example.se_track_review.controller.DTO.NewReviewDTO;
import com.example.se_track_review.controller.DTO.UpdateReviewDTO;
import com.example.se_track_review.exception.ConcertNotPerformedException;
import com.example.se_track_review.exception.InvalidConcertIdException;
import com.example.se_track_review.exception.InvalidStarsException;
import com.example.se_track_review.exception.ReviewNotFoundException;
import com.example.se_track_review.model.Review;
import com.example.se_track_review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("review")
@CrossOrigin
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * endpoint to get all reviews
     * @return list of reviews in db
     */
    @GetMapping(value = "/all")
    public List<Review> getReviewsWithNrOfStars() {
        return this.reviewService.getAllReviews();
    }

    /**
     * endpoint to return reviews with a certain amount of stars
     * @param stars number of stars as search criteria
     * @return list of reviews
     */
    @GetMapping(value = "/stars")
    public List<Review> getReviewsWithNrOfStars(@RequestParam int stars) {
        return this.reviewService.findReviewsByNrOfStarts(stars);
    }

    /**
     * endpoint to get reviews that have a maximum number of stars
     * @param stars number of stars as search criteria
     * @return list of reviews
     */
    @GetMapping(value = "/max-stars")
    public List<Review> getReviewsWithMaxStars(@RequestParam int stars) {
        return this.reviewService.findReviewsWithMaxStars(stars);
    }

    /**
     * endpoint to get reviews that have a minimum number of stars
     * @param stars number of stars as search criteria
     * @return list of reviews
     */
    @GetMapping(value = "/min-stars")
    public List<Review> getReviewsWithMinStars(@RequestParam int stars) {
        return this.reviewService.findReviewsWithMinStars(stars);
    }

    /**
     * endpoint to get average number of stars by concert id
     * @param concertId of concert
     * @return average number of stars
     */
    @GetMapping(value="average-stars")
    public ResponseEntity<?> getAverageStarsOfConcert(@RequestParam long concertId) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.reviewService.getAverageStarsOfConcert(concertId));
        } catch (ReviewNotFoundException e) {
            return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseDTO("No reviews found of concert with id " + concertId));
        }
    }

    /**
     * endpoint to create new review
     * @param newReviewDTO dto with necessary information
     * @return HttpStatus 201 if everything went well, else 400
     */
    @PostMapping(value="new")
    public ResponseEntity<?> newReview(@Validated @RequestBody NewReviewDTO newReviewDTO) {
        try {
            Review returnedReview = this.reviewService.newReview(newReviewDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new JsonResponseDTO(returnedReview.getId()));
        } catch (InvalidConcertIdException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseDTO("Invalid concertId"));
        } catch (ConcertNotPerformedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseDTO("Cannot review concert in the future"));
        }
    }

    /**
     * endpoint to update a review
     * @param updateReviewDTO with necessary information
     * @return HttpStatus 200 if everything went well, else 400
     */
    @PutMapping(value="update")
    public ResponseEntity<?> updateReview(@Validated @RequestBody UpdateReviewDTO updateReviewDTO) {
        try {
            this.reviewService.updateReview(updateReviewDTO);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (InvalidConcertIdException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseDTO("Invalid concertId"));
        } catch (InvalidStarsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseDTO("Number of stars must be between 1 and 5"));
        } catch (ConcertNotPerformedException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseDTO("Cannot review concert in the future"));
        }
    }

    /**
     * endpoint to delete a review
     * @param reviewId of review
     * @return 200 if everything went well, else 400
     */
    @DeleteMapping(value="delete")
    public ResponseEntity<?> deleteReview(@RequestParam String reviewId) {
        try {
            this.reviewService.deleteReview(reviewId);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (InvalidConcertIdException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseDTO("Invalid concertId"));
        }
    }

    /**
     * get Reviews by performer id
     * @param performerId of performer
     * @return List of reviews
     */
    @GetMapping(value="by-performer")
    public List<Review> getReviewsByPerformerId(@RequestParam long performerId) {
        return this.reviewService.findReviewByPerformerId(performerId);
    }

    /**
     * get Reviewids by performer id
     * @param performerId of performer
     * @return list of strings containing review ids
     */
    @GetMapping(value="id-by-performer")
    public List<String> getReviewIdsByPerformerId(@RequestParam long performerId) {
        List<Review> reviews = this.reviewService.findReviewByPerformerId(performerId);
        List<String> reviewIds = new ArrayList<>();
        for (Review review: reviews) {
            reviewIds.add(review.getId());
        }
        return reviewIds;
    }

    /**
     * get Reviews by concert id
     * @param concertId of concert
     * @return list of Reviews
     */
    @GetMapping(value="review-by-concert")
    public List<Review> getReviewsByConcertId(@RequestParam long concertId) {
        return this.reviewService.findReviewByConcertId(concertId);
    }

    @GetMapping(value="/search")
    public List<Review> searchReviewTextsWithTerm(@RequestParam String term) {
        return this.reviewService.searchReviewTexts(term);
    }
}
