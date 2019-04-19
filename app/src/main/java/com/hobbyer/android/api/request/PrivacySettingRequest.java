package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class PrivacySettingRequest {

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("past_class")
    public boolean past_class;
    @SerializedName("upcoming_class")
    public boolean upcoming_class;
    @SerializedName("favourite_studio")
    public boolean favourite_studio;
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isPast_class() {
        return past_class;
    }

    public void setPast_class(boolean past_class) {
        this.past_class = past_class;
    }

    public boolean isUpcoming_class() {
        return upcoming_class;
    }

    public void setUpcoming_class(boolean upcoming_class) {
        this.upcoming_class = upcoming_class;
    }

    public boolean isFavourite_studio() {
        return favourite_studio;
    }

    public void setFavourite_studio(boolean favourite_studio) {
        this.favourite_studio = favourite_studio;
    }
}
