package com.hobbyer.android.api.response.auth.searchfriendlist;

import com.hobbyer.android.api.response.Pagination;

import java.util.ArrayList;

public class SearchFriendViewResult {

    private ArrayList<SearchFriendViewResponseContentList>contentList;


    public ArrayList<SearchFriendViewResponseContentList> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<SearchFriendViewResponseContentList> contentList) {
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
