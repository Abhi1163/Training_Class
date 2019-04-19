package com.hobbyer.android.api.response.auth.savefile;

import com.hobbyer.android.api.BaseResponse;


public class SaveFileResponse extends BaseResponse{


 private SaveFileResult result;


    public SaveFileResult getResult() {
        return result;
    }

    public void setResult(SaveFileResult result) {
        this.result = result;
    }
}
