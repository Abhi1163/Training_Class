package com.hobbyer.android.api.response.auth.mangeplan;

import com.hobbyer.android.api.BaseResponse;

public class ManagePlanResponse extends BaseResponse {
    private ManagePlanResult result;

    public ManagePlanResult getResult() {
        return result;
    }

    public void setResult(ManagePlanResult result) {
        this.result = result;
    }
}
