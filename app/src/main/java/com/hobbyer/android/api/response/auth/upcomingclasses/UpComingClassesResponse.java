package com.hobbyer.android.api.response.auth.upcomingclasses;

import com.hobbyer.android.api.BaseResponse;

public class UpComingClassesResponse extends BaseResponse {

    private UpComingClassesResult result;
    public UpComingClassesResult getResult() {
        return result;
    }
    public void setResult(UpComingClassesResult result) {
        this.result = result;
    }
}
