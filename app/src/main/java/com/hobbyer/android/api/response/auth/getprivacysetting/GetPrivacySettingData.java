package com.hobbyer.android.api.response.auth.getprivacysetting;

import android.content.Intent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPrivacySettingData {

    @SerializedName("past_class")
    @Expose
    private Integer pastClass;
    @SerializedName("upcoming_class")
    @Expose
    private Integer upcomingClass;
    @SerializedName("favourite_class")
    @Expose
    private Integer favouriteClass;


    public Integer getPastClass() {
        return pastClass;
    }

    public void setPastClass(Integer pastClass) {
        this.pastClass = pastClass;
    }

    public Integer getUpcomingClass() {
        return upcomingClass;
    }

    public void setUpcomingClass(Integer upcomingClass) {
        this.upcomingClass = upcomingClass;
    }

    public Integer getFavouriteClass() {
        return favouriteClass;
    }

    public void setFavouriteClass(Integer favouriteClass) {
        this.favouriteClass = favouriteClass;
    }
}
