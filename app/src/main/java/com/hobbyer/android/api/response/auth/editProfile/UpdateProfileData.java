package com.hobbyer.android.api.response.auth.editProfile;

import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.login.SignInData;

public class UpdateProfileData {

    private SignInData user;

    private String user_id;
    private String user_point;

    public SignInData getUser() {
        return user;
    }

    public void setUser(SignInData user) {
        this.user = user;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_point() {
        return user_point;
    }

    public void setUser_point(String user_point) {
        this.user_point = user_point;
    }
}
