package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class FavouriteStudioRequest {

    @SerializedName("studio_id")
    public String studio_id;

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("favourite_key")
    public String favourite_key;

    public String getStudio_id() {
        return studio_id;
    }

    public void setStudio_id(String studio_id) {
        this.studio_id = studio_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFavourite_key() {
        return favourite_key;
    }

    public void setFavourite_key(String favourite_key) {
        this.favourite_key = favourite_key;
    }
}
