package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class RecentChargesRequest {
    @SerializedName("user_id")
    public String userId;

    @SerializedName("page_number")
    public int page_number;

    @SerializedName("page_size")
    public int page_size;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPage_number() {
        return page_number;
    }

    public void setPage_number(int page_number) {
        this.page_number = page_number;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }
}
