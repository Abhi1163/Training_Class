package com.hobbyer.android.api.response.auth.referearn;

import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.request.ReferEarnRequest;

public class ReferEarnResponse extends BaseResponse {

    private ReferEarnResult result;

    public ReferEarnResult getRequest() {
        return result;
    }

    public void setRequest(ReferEarnResult request) {
        this.result = request;
    }
}
