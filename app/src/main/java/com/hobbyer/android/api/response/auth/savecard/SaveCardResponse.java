package com.hobbyer.android.api.response.auth.savecard;

import com.google.gson.annotations.SerializedName;
import com.hobbyer.android.api.BaseResponse;

public class SaveCardResponse extends BaseResponse {
    @SerializedName("stripe_token")
    private String stripeToken;


    @SerializedName("user_id")
    private int userId;
    @SerializedName("user_email")
    private String userEmail;

    public String getStripeToken() {
        return stripeToken;
    }

    public void setStripeToken(String stripeToken) {
        this.stripeToken = stripeToken;
    }





    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
