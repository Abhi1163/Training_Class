package com.hobbyer.android.api.response.auth.topratingstudios;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hobbyer.android.api.BaseResponse;

public class GetTopRatingResponse extends BaseResponse {

    public GetTopRatingResult getResult() {
        return result;
    }

    public void setResult(GetTopRatingResult result) {
        this.result = result;
    }

    @SerializedName("result")
    @Expose
    private GetTopRatingResult result;
}
