package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class CountPointRequest {
    @SerializedName("user_id")
    public String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
