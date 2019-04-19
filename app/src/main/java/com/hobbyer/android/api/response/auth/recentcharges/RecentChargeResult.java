package com.hobbyer.android.api.response.auth.recentcharges;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hobbyer.android.api.response.Pagination;

import java.util.List;

public class RecentChargeResult {


    @SerializedName("contentList")
    @Expose
    private List<RecentChargeList> contentList = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;
    public List<RecentChargeList> getContentList() {
        return contentList;
    }

    public void setContentList(List<RecentChargeList> contentList) {
        this.contentList = contentList;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

}
