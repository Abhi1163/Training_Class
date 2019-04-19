package com.hobbyer.android.api.response.auth.classdetails;

import com.hobbyer.android.api.BaseResponse;

public class ClassDetailResponse extends BaseResponse {

    private ClassDetailResult result;

    public ClassDetailResult getResult() {
        return result;
    }

    public void setResult(ClassDetailResult result) {
        this.result = result;
    }
}
