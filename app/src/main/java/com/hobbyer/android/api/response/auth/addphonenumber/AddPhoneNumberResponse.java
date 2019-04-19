package com.hobbyer.android.api.response.auth.addphonenumber;

import com.hobbyer.android.api.BaseResponse;

public class AddPhoneNumberResponse extends BaseResponse {

    private AddPhoneNumberResult result;


    public AddPhoneNumberResult getResult() {
        return result;
    }

    public void setResult(AddPhoneNumberResult result) {
        this.result = result;
    }
}
