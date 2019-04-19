package com.hobbyer.android.api.response.auth.searchfriendlist;

import com.hobbyer.android.api.BaseResponse;

public class SearchFriendViewResponse extends BaseResponse {


    private SearchFriendViewResult result;

    public SearchFriendViewResult getResult() {
        return result;
    }

    public void setResult(SearchFriendViewResult result) {
        this.result = result;
    }
}
