package com.hobbyer.android.interfaces;

import com.hobbyer.android.api.response.auth.getfriends.GetUserFriendViewResponseContentList;

public interface OnFriendItemClickListners {
    public void  onUnfriendClick(int postion, GetUserFriendViewResponseContentList listData);
    public void  onBlockClick(int postion, GetUserFriendViewResponseContentList listData);
}
