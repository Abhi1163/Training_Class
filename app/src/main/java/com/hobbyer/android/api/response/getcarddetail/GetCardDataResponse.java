package com.hobbyer.android.api.response.getcarddetail;

import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.response.auth.verifyotp.VerifyOtpResult;

public class GetCardDataResponse  extends BaseResponse {

    private CardDataResult result;

    public CardDataResult getResult() {
        return result;
    }

    public void setResult(CardDataResult result) {
        this.result = result;
    }
}


