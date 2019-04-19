package com.hobbyer.android.api.response.auth.editProfile;

import com.hobbyer.android.api.response.auth.login.SignInData;

public class UpdateProfileResult {
    private SignInData data;

    public SignInData getData() {
        return data;
    }

    public void setData(SignInData data) {
        this.data = data;
    }
}
