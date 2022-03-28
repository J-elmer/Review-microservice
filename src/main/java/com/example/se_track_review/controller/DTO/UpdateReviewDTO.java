package com.example.se_track_review.controller.DTO;

import javax.validation.constraints.NotEmpty;

public final class UpdateReviewDTO {

    @NotEmpty(message = "reviewId is mandatory")
    private String reviewId;
    private long concertId;
    private String authorName;
    private int numberOfStars;
    private String reviewText;

    public UpdateReviewDTO(String reviewId, long concertId, String authorName, int numberOfStars, String reviewText) {
        this.reviewId = reviewId;
        this.concertId = concertId;
        this.authorName = authorName;
        this.numberOfStars = numberOfStars;
        this.reviewText = reviewText;
    }

    public String getReviewId() {
        return reviewId;
    }

    public long getConcertId() {
        return concertId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public int getNumberOfStars() {
        return numberOfStars;
    }

    public String getReviewText() {
        return reviewText;
    }
}
