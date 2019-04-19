package com.hobbyer.android.api.response.auth.forgotpassword;

import com.hobbyer.android.api.BaseResponse;

public class ForgotPasswordResponse extends BaseResponse {

    private ForgotPasswordResult result;

    public ForgotPasswordResult getResult() {
        return result;
    }

    public void setResult(ForgotPasswordResult result) {
        this.result = result;
    }

}
