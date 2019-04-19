package com.hobbyer.android.api.response.auth.faq;

import com.hobbyer.android.api.response.Pagination;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FaqResult {

    private ArrayList<FaqContentList> contentList;

    public ArrayList<FaqContentList> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<FaqContentList> contentList) {
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
