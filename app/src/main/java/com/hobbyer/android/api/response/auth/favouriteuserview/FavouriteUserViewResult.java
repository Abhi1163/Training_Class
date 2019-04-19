package com.hobbyer.android.api.response.auth.favouriteuserview;

import com.hobbyer.android.api.response.Pagination;

import java.util.ArrayList;

public class FavouriteUserViewResult {

    private ArrayList<FavouriteUserViewResponseContentList>contentList;


    public ArrayList<FavouriteUserViewResponseContentList> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<FavouriteUserViewResponseContentList> contentList) {
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
