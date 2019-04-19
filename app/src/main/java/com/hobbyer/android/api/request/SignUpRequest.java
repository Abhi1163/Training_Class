package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class SignUpRequest  {

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("last_name")
    public String lastName;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("confirm")
    public String confirm;

    @SerializedName("term_condition")
    public int termsCondition;

    @SerializedName("device_token")
    public String deviceToken;

    @SerializedName("device_type")
    public int deviceType;

    @SerializedName("city")
    public String city;

    @SerializedName("country")
    public String country;

    @SerializedName("latitude")
    public String latitude;

    @SerializedName("longitude")
    public String longitude;


    @SerializedName("isLogin")
    public  int Islogin;



    public int getIslogin() {
        return Islogin;
    }

    public void setIslogin(int islogin) {
        Islogin = islogin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

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

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public int getTermsCondition() {
        return termsCondition;
    }

    public void setTermsCondition(int termsCondition) {
        this.termsCondition = termsCondition;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
