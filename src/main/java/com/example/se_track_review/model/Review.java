package com.example.se_track_review.model;

import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;
import java.util.Objects;

public class Review {

    @Id
    private String id;
    private long concertId;
    private String authorName;
    private LocalDateTime dateTimeofReview;
    private int numberOfStars;
    private String reviewText;

    public Review(long concertId, String authorName, int numberOfStars, String reviewText) {
        this.concertId = concertId;
        this.authorName = authorName;
        this.dateTimeofReview = LocalDateTime.now();
        this.numberOfStars = numberOfStars;
        this.reviewText = reviewText;
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

    public LocalDateTime getDateTimeofReview() {
        return dateTimeofReview;
    }

    public void setDateTimeofReview(LocalDateTime dateTimeofReview) {
        this.dateTimeofReview = dateTimeofReview;
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

    @Override
    public String toString() {
        return "Review{" +
                "id='" + id + '\'' +
                ", concertId=" + concertId +
                ", authorName='" + authorName + '\'' +
                ", dateTimeofReview=" + dateTimeofReview +
                ", numberOfStars=" + numberOfStars +
                ", reviewText='" + reviewText + '\'' +
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
