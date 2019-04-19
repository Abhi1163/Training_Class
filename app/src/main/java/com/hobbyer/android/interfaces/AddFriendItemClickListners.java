package com.hobbyer.android.interfaces;

import com.hobbyer.android.api.response.auth.searchfriendlist.SearchFriendViewResponseContentList;

import java.util.List;

public interface AddFriendItemClickListners {
    public void  onAddfriendClick(int postion, List<SearchFriendViewResponseContentList> listData);
}
