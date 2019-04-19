package com.hobbyer.android.api.response.auth.searchcity;

import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.response.auth.signup.SignUpData;

public class CityResponse extends BaseResponse {

    private CityResult result;

    public CityResult getResult() {
        return result;
    }

    public void setResult(CityResult result) {
        this.result = result;
    }
}
