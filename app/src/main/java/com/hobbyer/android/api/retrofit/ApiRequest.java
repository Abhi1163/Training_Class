package com.hobbyer.android.api.retrofit;

import com.google.gson.annotations.SerializedName;

public class ApiRequest {
    @SerializedName("stripe_token")
    private String stripeToken;

    public String getStripeToken() {
        return stripeToken;
    }

    public void setStripeToken(String stripeToken) {
        this.stripeToken = stripeToken;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    @SerializedName("card_type")
    private String cardType;

}
