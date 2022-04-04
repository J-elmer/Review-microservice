package com.example.se_track_review.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.Objects;

public class Review {

    @Id
    private String id;
    private long concertId;
    private String authorName;
    private LocalDate dateOfReview;
    private int numberOfStars;
    private String reviewText;
    private long performerId;

    public Review(long concertId, String authorName, int numberOfStars, String reviewText, long performerId) {
        this.concertId = concertId;
        this.authorName = authorName;
        this.dateOfReview = LocalDate.now();
        this.numberOfStars = numberOfStars;
        this.reviewText = reviewText;
        this.performerId = performerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getConcertId() {
        return concertId;
    }

    public void setConcertId(long concertId) {
        this.concertId = concertId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public LocalDate getDateOfReview() {
        return dateOfReview;
    }

    public void setDateOfReview(LocalDate dateOfReview) {
        this.dateOfReview = dateOfReview;
    }

    public int getNumberOfStars() {
        return numberOfStars;
    }

    public void setNumberOfStars(int numberOfStars) {
        this.numberOfStars = numberOfStars;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(long performerId) {
        this.performerId = performerId;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", concertId=" + concertId +
                ", authorName='" + authorName + '\'' +
                ", dateTimeofReview=" + dateOfReview +
                ", numberOfStars=" + numberOfStars +
                ", reviewText='" + reviewText + '\'' +
                ", performerId=" + performerId +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Review review = (Review) obj;
        return id.equals(review.getId());
    }
}
