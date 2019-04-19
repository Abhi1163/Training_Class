package com.hobbyer.android.api.response.auth.blockfriendlist;

import com.hobbyer.android.api.BaseResponse;

public class BlockFriendViewResponse extends BaseResponse {


    private BlockFriendViewResult result;

    public BlockFriendViewResult getResult() {
        return result;
    }

    public void setResult(BlockFriendViewResult result) {
        this.result = result;
    }
}
