package com.hobbyer.android.api.response.auth.pastprofile;

import com.hobbyer.android.api.BaseResponse;

public class PastUserViewResponse extends BaseResponse {
    private PastUserViewResult result;

    public PastUserViewResult getResult() {
        return result;
    }

    public void setResult(PastUserViewResult result) {
        this.result = result;
    }
}
