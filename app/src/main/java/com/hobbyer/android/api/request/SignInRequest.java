package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class SignInRequest {

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("device_token")
    public String deviceToken;

    @SerializedName("device_type")
    public int deviceType;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
