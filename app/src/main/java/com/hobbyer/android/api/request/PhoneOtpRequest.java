package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class PhoneOtpRequest {
    @SerializedName("email")
    public String email;

    @SerializedName("phone_number")
    public String phone_number;

    @SerializedName("user_id")
    public String user_id;

    @SerializedName("type")
    public String type;

    @SerializedName("otp")
    public String otp;
    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    @SerializedName("phone_code")
    private String country_code;



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
