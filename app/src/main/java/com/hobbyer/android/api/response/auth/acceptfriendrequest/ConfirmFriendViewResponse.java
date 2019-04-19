package com.hobbyer.android.api.response.auth.acceptfriendrequest;

import com.hobbyer.android.api.BaseResponse;

public class ConfirmFriendViewResponse extends BaseResponse {


    private ConfirmFriendViewResult result;

    public ConfirmFriendViewResult getResult() {
        return result;
    }

    public void setResult(ConfirmFriendViewResult result) {
        this.result = result;
    }
}
