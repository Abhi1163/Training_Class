package com.hobbyer.android.api.response.auth.acceptfriendrequest;

import com.hobbyer.android.api.response.Pagination;

import java.util.ArrayList;

public class ConfirmFriendViewResult {

    private ArrayList<ConfirmFriendViewResponseContentList>contentList;


    public ArrayList<ConfirmFriendViewResponseContentList> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<ConfirmFriendViewResponseContentList> contentList) {
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
