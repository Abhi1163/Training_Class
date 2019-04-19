package com.hobbyer.android.api.response.auth.viewprofile;

import com.hobbyer.android.api.BaseResponse;

public class ViewProfileResponse extends BaseResponse {

    private ViewProfileResult result;

    public ViewProfileResult getResult() {
        return result;
    }

    public void setResult(ViewProfileResult result) {
        this.result = result;
    }
}
