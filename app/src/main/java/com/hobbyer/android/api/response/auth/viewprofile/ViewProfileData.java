package com.hobbyer.android.api.response.auth.viewprofile;

import com.google.gson.annotations.SerializedName;
import com.hobbyer.android.api.response.auth.UserModel;

public class ViewProfileData {


    @SerializedName("user_point")
    private String user_point;
    private ViewProfileUser user;

    public ViewProfileUser getUser() {
        return user;
    }

    public void setUser(ViewProfileUser user) {
        this.user = user;
    }

    public String getUser_point() {
        return user_point;
    }

    public void setUser_point(String user_point) {
        this.user_point = user_point;
    }
}
