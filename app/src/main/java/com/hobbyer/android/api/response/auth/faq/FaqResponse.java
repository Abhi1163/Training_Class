package com.hobbyer.android.api.response.auth.faq;

import com.hobbyer.android.api.BaseResponse;

public class FaqResponse extends BaseResponse {

   private FaqResult result;

    public FaqResult getResult() {
        return result;
    }

    public void setResult(FaqResult result) {
        this.result = result;
    }
}
