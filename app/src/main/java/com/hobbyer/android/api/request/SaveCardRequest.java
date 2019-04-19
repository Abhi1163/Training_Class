package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class SaveCardRequest {

    @SerializedName("stripe_token")
    private String stripeToken;
    @SerializedName("user_id")
    private String userId;

    @SerializedName("user_email")
    private String userEmail;


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStripeToken() {
        return stripeToken;
    }

    public void setStripeToken(String stripeToken) {
        this.stripeToken = stripeToken;
    }





}
