package com.hobbyer.android.api.response.auth.reserve;

import com.hobbyer.android.api.BaseResponse;

public class ReserveResponse  extends BaseResponse {

    private ReserveResult result;

    public ReserveResult getResult() {
        return result;
    }

    public void setResult(ReserveResult result) {
        this.result = result;
    }
}
