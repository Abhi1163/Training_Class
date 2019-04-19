package com.hobbyer.android.api.response.auth.getfriends;

import com.hobbyer.android.api.BaseResponse;

public class GetUserFriendViewResponse extends BaseResponse {


    private GetUserFriendViewResult result;

    public GetUserFriendViewResult getResult() {
        return result;
    }

    public void setResult(GetUserFriendViewResult result) {
        this.result = result;
    }
}
