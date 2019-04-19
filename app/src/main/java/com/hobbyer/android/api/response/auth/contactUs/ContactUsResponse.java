package com.hobbyer.android.api.response.auth.contactUs;

import com.hobbyer.android.api.BaseResponse;

public class ContactUsResponse extends BaseResponse{


 private ContactUsResult result;

    public ContactUsResult getResult() {
        return result;
    }

    public void setResult(ContactUsResult result) {
        this.result = result;
    }
}
