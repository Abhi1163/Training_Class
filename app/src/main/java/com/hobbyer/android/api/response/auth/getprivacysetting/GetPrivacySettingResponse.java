package com.hobbyer.android.api.response.auth.getprivacysetting;

import com.hobbyer.android.api.BaseResponse;

public class GetPrivacySettingResponse extends BaseResponse {

    private GetPrivacySettingResult result;


    public GetPrivacySettingResult getResult() {
        return result;
    }

    public void setResult(GetPrivacySettingResult result) {
        this.result = result;
    }
}
