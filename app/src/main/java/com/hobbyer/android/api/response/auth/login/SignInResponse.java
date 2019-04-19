package com.hobbyer.android.api.response.auth.login;

import com.hobbyer.android.api.BaseResponse;

public class SignInResponse extends BaseResponse {

    private SignInResult result;

    public SignInResult getResult() {
        return result;
    }

    public void setResult(SignInResult result) {
        this.result = result;
    }

}
