package com.example.selectionofperformers.Entity;

public class Review {
    private String idReview;
    private String reviewRating;
    private String reviewComment;

    public Review(String idReview, String reviewRating, String reviewComment) {
        this.idReview = idReview;
        this.reviewRating = reviewRating;
        this.reviewComment = reviewComment;

    }

    public String getReviewId() {
        return idReview;
    }

    public String getReviewRating() {
        return reviewRating;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewId() {
        this.idReview = idReview;
    }

    public void setReviewRating() {
        this.reviewRating = reviewRating;
    }

    public void setReviewComment() {
        this.reviewComment = reviewComment;
    }
}
