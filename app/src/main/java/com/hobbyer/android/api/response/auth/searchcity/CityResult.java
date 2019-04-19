package com.hobbyer.android.api.response.auth.searchcity;

import com.hobbyer.android.api.response.Pagination;
import com.hobbyer.android.api.response.auth.signup.SignUpData;

import java.util.ArrayList;

public class CityResult {

    private ArrayList<CityContentList> contentList;

    public ArrayList<CityContentList> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<CityContentList> contentList) {
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
