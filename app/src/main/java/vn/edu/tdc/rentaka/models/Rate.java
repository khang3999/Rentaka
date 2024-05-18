package vn.edu.tdc.rentaka.models;

import java.time.LocalDate;

public class Rate {
    private String id;
    private String recipientID;
    private String reviewerID;
    private Integer rating;
    private String comment;
    private Date createdAt;
    private Date updatedAt;


    public enum RateProperties{
        id,
        recipientID,
        reviewerID,
        rating,
        comment
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public String getReviewerID() {
        return reviewerID;
    }

    public void setReviewerID(String reviewerID) {
        this.reviewerID = reviewerID;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Rate(){}

    public Rate(String recipientID, String reviewerID, Integer rating, String comment) {
        if(rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.recipientID = recipientID;
        this.reviewerID = reviewerID;
        this.comment = comment;
        this.createdAt = new Date(LocalDate.now());
        this.updatedAt = null;
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "id='" + id + '\'' +
                ", recipientID='" + recipientID + '\'' +
                ", reviewerID='" + reviewerID + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
