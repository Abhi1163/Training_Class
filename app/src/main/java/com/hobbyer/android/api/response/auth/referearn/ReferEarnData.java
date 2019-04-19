package com.hobbyer.android.api.response.auth.referearn;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReferEarnData {
    @SerializedName("refer_earn_code")
    @Expose
    private String refer_earn_code;


    public String getRefer_earn_code() {
        return refer_earn_code;
    }

    public void setRefer_earn_code(String refer_earn_code) {
        this.refer_earn_code = refer_earn_code;
    }
}
