package com.hobbyer.android.api.response.auth.homepage;

import com.hobbyer.android.api.BaseResponse;

public class HomePageResponse extends BaseResponse {
    private HomePageResult result;

    public HomePageResult getResult() {
        return result;
    }

    public void setResult(HomePageResult result) {
        this.result = result;
    }

}
