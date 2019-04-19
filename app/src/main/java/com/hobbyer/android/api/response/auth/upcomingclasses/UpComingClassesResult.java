package com.hobbyer.android.api.response.auth.upcomingclasses;

import com.hobbyer.android.api.response.Pagination;

import java.util.ArrayList;

public class UpComingClassesResult {


    private ArrayList<UpComingClassesContentList> contentList;

    public ArrayList<UpComingClassesContentList> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<UpComingClassesContentList> contentList) {
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
