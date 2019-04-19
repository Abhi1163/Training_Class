package com.hobbyer.android.api.response.auth.verifyotp;

import com.hobbyer.android.api.BaseResponse;

public class VerifyOtpResponse extends BaseResponse {

    private VerifyOtpResult result;

    public VerifyOtpResult getResult() {
        return result;
    }

    public void setResult(VerifyOtpResult result) {
        this.result = result;
    }
}
