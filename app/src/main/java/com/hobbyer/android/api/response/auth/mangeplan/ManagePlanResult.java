package com.hobbyer.android.api.response.auth.mangeplan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ManagePlanResult {

    @SerializedName("contentList")
    @Expose
    private List<ManagePlanData> contentList = null;

    public List<ManagePlanData> getContentList() {
        return contentList;
    }

    public void setContentList(List<ManagePlanData> contentList) {
        this.contentList = contentList;
    }

    @SerializedName("data")
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
