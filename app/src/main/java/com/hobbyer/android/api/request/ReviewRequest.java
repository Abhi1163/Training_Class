package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.response.Pagination;

public class ReviewRequest  {




    @SerializedName("studio_id")
    public String studio_id;

    @SerializedName("page_number")
    public int page_number;

    @SerializedName("page_size")
    public int page_size;


    public String getStudio_id() {
        return studio_id;
    }

    public void setStudio_id(String studio_id) {
        this.studio_id = studio_id;
    }

    public int getPage_number() {
        return page_number;
    }

    public void setPage_number(int page_number) {
        this.page_number = page_number;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

}
