package com.hobbyer.android.api.response.auth.videos;

import com.hobbyer.android.api.response.Pagination;

import java.util.ArrayList;

public class VideosResult {
 private ArrayList<VideosContentList> contentList;
    private Pagination pagination;

    public ArrayList<VideosContentList> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<VideosContentList> contentList) {
        this.contentList = contentList;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
