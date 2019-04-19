package com.hobbyer.android.api.response.auth.forgotpassword;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPasswordData {

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("otp")
    @Expose
    private long otp;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getOtp() {
        return otp;
    }

    public void setOtp(long otp) {
        this.otp = otp;
    }

}
