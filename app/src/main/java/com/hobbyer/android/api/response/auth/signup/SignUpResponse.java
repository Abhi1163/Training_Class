package com.hobbyer.android.api.response.auth.signup;

import com.hobbyer.android.api.BaseResponse;

public class SignUpResponse extends BaseResponse {

    private SignUpResult result;

    public SignUpResult getResult() {
        return result;
    }

    public void setResult(SignUpResult result) {
        this.result = result;
    }
}
