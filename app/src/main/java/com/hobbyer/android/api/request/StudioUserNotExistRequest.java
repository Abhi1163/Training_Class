package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class StudioUserNotExistRequest {

    @SerializedName("email")
    public String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
