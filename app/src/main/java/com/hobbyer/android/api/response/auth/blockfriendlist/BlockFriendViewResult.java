package com.hobbyer.android.api.response.auth.blockfriendlist;

import com.hobbyer.android.api.response.Pagination;

import java.util.ArrayList;

public class BlockFriendViewResult {

    private ArrayList<BlockFriendViewResponseContentList>contentList;


    public ArrayList<BlockFriendViewResponseContentList> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<BlockFriendViewResponseContentList> contentList) {
        this.contentList = contentList;
    }

    private Pagination pagination;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
