package com.example.se_track_review.controller.DTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public final class NewReviewDTO {

    @Positive(message = "ConcertId is mandatory")
    private long concertId;
    @NotEmpty(message = "Name of reviewer is mandatory")
    private String authorName;
    @Min(value=1)
    @Max(value=5)
    private int numberOfStars;
    @NotEmpty(message = "Review text is mandatory")
    private String reviewText;

    public NewReviewDTO(long concertId, String authorName, int numberOfStars, String reviewText) {
        this.concertId = concertId;
        this.authorName = authorName;
        this.numberOfStars = numberOfStars;
        this.reviewText = reviewText;
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
