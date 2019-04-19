package com.hobbyer.android.api.response.auth.pastprofile;

import com.hobbyer.android.api.response.Pagination;
import com.hobbyer.android.api.response.auth.pastprofile.PastUserViewContentList;

import java.util.ArrayList;

public class PastUserViewResult {
    private ArrayList<PastUserViewContentList>contentList;
    private Pagination pagination;

    public ArrayList<PastUserViewContentList> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<PastUserViewContentList> contentList) {
        this.contentList = contentList;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}

