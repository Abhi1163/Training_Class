package com.hobbyer.android.api.response.auth.getfriends;

import com.hobbyer.android.api.response.Pagination;

import java.util.ArrayList;

public class GetUserFriendViewResult {

    private ArrayList<GetUserFriendViewResponseContentList>contentList;


    public ArrayList<GetUserFriendViewResponseContentList> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<GetUserFriendViewResponseContentList> contentList) {
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
