package com.hobbyer.android.api.response.auth.addphonenumber;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddPhoneNumberData {


    @SerializedName("otp")
    @Expose
    private long otp;

    public long getOtp() {
        return otp;
    }

    public void setOtp(long otp) {
        this.otp = otp;
    }
}
