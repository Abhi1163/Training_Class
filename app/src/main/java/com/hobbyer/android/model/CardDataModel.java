package com.hobbyer.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CardDataModel {

    @SerializedName("card_number")
    @Expose
    private String  cardNumber;

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    @SerializedName("cardName")
    @Expose
    private String  cardName;

    @SerializedName("cvv")
    @Expose
    private String  cvv;

    @SerializedName("user_email")
    @Expose
    private String  email;

    @SerializedName("expiry")
    @Expose
    private String  expiry;

    @SerializedName("exp_year")
    @Expose
    private int  expYear;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public int getExpYear() {
        return expYear;
    }

    public void setExpYear(int expYear) {
        this.expYear = expYear;
    }




}
