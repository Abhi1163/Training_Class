package com.hobbyer.android.api.response.getcarddetail;

import com.google.gson.annotations.SerializedName;
import com.hobbyer.android.api.response.auth.verifyotp.VerifyOtpData;

public class CardDataResult {

    public CardDetailData getData() {
        return data;
    }

    public void setData(CardDetailData data) {
        this.data = data;
    }

    private CardDetailData data;




}
