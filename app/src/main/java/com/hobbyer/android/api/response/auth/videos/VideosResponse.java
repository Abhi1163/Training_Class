package com.hobbyer.android.api.response.auth.videos;

import com.hobbyer.android.api.BaseResponse;

public class VideosResponse extends BaseResponse {

    private VideosResult result;

    public VideosResult getResult() {
        return result;
    }

    public void setResult(VideosResult result) {
        this.result = result;
    }
}
