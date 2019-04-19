package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class GetPrivacysSettingRequest {
    @SerializedName("user_id")
    public String user_id;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
