package com.hobbyer.android.api.response.auth.editProfile;

import com.hobbyer.android.api.BaseResponse;

public class UpdateProfileResponse extends BaseResponse{

  private UpdateProfileResult result;

    public UpdateProfileResult getResult() {
        return result;
    }

    public void setResult(UpdateProfileResult result) {
        this.result = result;
    }
}
