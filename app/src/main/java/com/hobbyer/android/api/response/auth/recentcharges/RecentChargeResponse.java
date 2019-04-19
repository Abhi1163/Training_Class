package com.hobbyer.android.api.response.auth.recentcharges;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hobbyer.android.api.BaseResponse;

public class RecentChargeResponse extends BaseResponse {


    @SerializedName("result")
    @Expose
    private RecentChargeResult result;



    public RecentChargeResult getResult() {
        return result;
    }

    public void setResult(RecentChargeResult result) {
        this.result = result;
    }




}
