package com.hobbyer.android.api.response.auth.filter;

import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.response.Pagination;

import java.util.ArrayList;

public class CategoryListResult {

    private ArrayList<CategoryListContentList> contentList;



    public ArrayList<CategoryListContentList> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<CategoryListContentList> contentList) {
        this.contentList = contentList;
    }


}
