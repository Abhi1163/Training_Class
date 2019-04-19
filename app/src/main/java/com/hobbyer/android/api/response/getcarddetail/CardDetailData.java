package com.hobbyer.android.api.response.getcarddetail;

import com.google.gson.annotations.SerializedName;

public class CardDetailData {

    @SerializedName("id")
    private int id;

    @SerializedName("uid")
    private int uid;

    @SerializedName("token")
    private String token;

    @SerializedName("customer_id")
    private String customerId;

    @SerializedName("name")
    private String name;

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    @SerializedName("card_number")
    private int cardNumber;
    @SerializedName("exp_month")
    private String expMonth;

    @SerializedName("exp_year")
    private String exp_year;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getExp_year() {
        return exp_year;
    }

    public void setExp_year(String exp_year) {
        this.exp_year = exp_year;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @SerializedName("created_at")
    private String created_at;
    @SerializedName("updated_at")
    private String updated_at;

}
