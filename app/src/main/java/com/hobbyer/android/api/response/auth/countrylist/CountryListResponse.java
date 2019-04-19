package com.hobbyer.android.api.response.auth.countrylist;

import com.hobbyer.android.api.BaseResponse;

public class CountryListResponse extends BaseResponse {

    private CountryListResult result;

    public CountryListResult getResult() {
        return result;
    }

    public void setResult(CountryListResult result) {
        this.result = result;
    }
}
