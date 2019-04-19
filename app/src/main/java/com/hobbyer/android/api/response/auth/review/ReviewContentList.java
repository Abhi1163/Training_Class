package com.hobbyer.android.api.response.auth.review;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewContentList {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("studio_name")
    @Expose
    private String studioName;
    @SerializedName("feedback")
    @Expose
    private String feedback;
    @SerializedName("rating")
    @Expose
    private int rating;
    @SerializedName("count")
    @Expose
    private int count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStudioName() {
        return studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
