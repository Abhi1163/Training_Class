package com.hobbyer.android.api.response.auth.countpoint;

import com.google.gson.annotations.SerializedName;

public class CountPointData {

    @SerializedName("friend_count")
    public String friend_count;

    @SerializedName("favourite_studios")
    public String favourite_studios;

    @SerializedName("user_point")
    public int user_point;

    @SerializedName("upcoming_class")
    public String upcoming_class;

    @SerializedName("completed_class")
    public String completed_class;


    public String getFriend_count() {
        return friend_count;
    }

    public void setFriend_count(String friend_count) {
        this.friend_count = friend_count;
    }

    public String getFavourite_studios() {
        return favourite_studios;
    }

    public void setFavourite_studios(String favourite_studios) {
        this.favourite_studios = favourite_studios;
    }

    public int getUser_point() {
        return user_point;
    }

    public void setUser_point(int user_point) {
        this.user_point = user_point;
    }

    public String getUpcoming_class() {
        return upcoming_class;
    }

    public void setUpcoming_class(String upcoming_class) {
        this.upcoming_class = upcoming_class;
    }

    public String getCompleted_class() {
        return completed_class;
    }

    public void setCompleted_class(String completed_class) {
        this.completed_class = completed_class;
    }
}
