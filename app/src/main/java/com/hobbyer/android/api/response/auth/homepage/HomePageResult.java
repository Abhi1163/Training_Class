package com.hobbyer.android.api.response.auth.homepage;

import com.hobbyer.android.api.response.Pagination;

import java.util.ArrayList;

public class HomePageResult {

    private ArrayList<HomePageContentList> contentList;

    public ArrayList<HomePageContentList> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<HomePageContentList> contentList) {
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
