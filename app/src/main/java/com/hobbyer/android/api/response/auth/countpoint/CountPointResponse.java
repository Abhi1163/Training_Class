package com.hobbyer.android.api.response.auth.countpoint;

import com.hobbyer.android.api.BaseResponse;

public class CountPointResponse extends BaseResponse {
    private CountPointResult result;

    public CountPointResult getResult() {
        return result;
    }

    public void setResult(CountPointResult result) {
        this.result = result;
    }


}
