package com.hobbyer.android.interfaces;

import com.hobbyer.android.api.response.auth.blockfriendlist.BlockFriendViewResponseContentList;

public interface OnUnblockFriendItemClickListners {
    public void  onUnBlock(int postion, BlockFriendViewResponseContentList listData);
}
