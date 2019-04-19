package com.hobbyer.android.api.response.auth.countrylist;

import com.hobbyer.android.api.response.Pagination;

import java.util.ArrayList;

public class CountryListResult {

    private ArrayList<CountryContentList> contentList;
   private Pagination pagination;

    public ArrayList<CountryContentList> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<CountryContentList> contentList) {
        this.contentList = contentList;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
