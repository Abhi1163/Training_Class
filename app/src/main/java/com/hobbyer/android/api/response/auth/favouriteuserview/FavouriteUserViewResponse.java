package com.hobbyer.android.api.response.auth.favouriteuserview;

import com.hobbyer.android.api.BaseResponse;

public class FavouriteUserViewResponse extends BaseResponse {


    private FavouriteUserViewResult result;

    public FavouriteUserViewResult getResult() {
        return result;
    }

    public void setResult(FavouriteUserViewResult result) {
        this.result = result;
    }
}
