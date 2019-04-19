package com.hobbyer.android.api.response.auth.topratingstudios;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hobbyer.android.api.response.Pagination;

import java.util.List;

public class GetTopRatingResult {



    @SerializedName("contentList")
    @Expose
    private List<GetTopRatingData> contentList = null;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public List<GetTopRatingData> getContentList() {
        return contentList;
    }

    public void setContentList(List<GetTopRatingData> contentList) {
        this.contentList = contentList;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
