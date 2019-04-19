package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class LogoutRequest {

    @SerializedName("user_id")
    public String userId;

    @SerializedName("device_token")
    public String deviceToken;

    @SerializedName("device_type")
    public int deviceType;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }
}
