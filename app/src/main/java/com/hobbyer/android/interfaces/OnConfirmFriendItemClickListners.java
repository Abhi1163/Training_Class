package com.hobbyer.android.interfaces;

import com.hobbyer.android.api.response.auth.acceptfriendrequest.ConfirmFriendViewResponseContentList;

public interface OnConfirmFriendItemClickListners {
    public void  onIgnore(int postion, ConfirmFriendViewResponseContentList listData);
    public void  onBlock(int postion, ConfirmFriendViewResponseContentList listData);
    public void  onAccept(int postion, ConfirmFriendViewResponseContentList listData);
}
