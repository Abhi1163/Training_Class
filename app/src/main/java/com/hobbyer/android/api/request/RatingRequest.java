package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class RatingRequest {

    @SerializedName("user_id")
    public String Userid;

    @SerializedName("studio_id")
    public int studioId;

    @SerializedName("rating")
    public int rating;

    @SerializedName("feedback")
    public String feedback;

    public int getStudioId() {
        return studioId;
    }

    public void setStudioId(int studioId) {
        this.studioId = studioId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }
}
