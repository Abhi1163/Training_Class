package com.hobbyer.android.api.response.auth.planlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hobbyer.android.api.BaseResponse;

import java.util.List;

public class PlanListResult  {


    @SerializedName("contentList")
    @Expose
    private List<PlanListData> contentList = null;

    public List<PlanListData> getContentList() {
        return contentList;
    }

    public void setContentList(List<PlanListData> contentList) {
        this.contentList = contentList;
    }

}



