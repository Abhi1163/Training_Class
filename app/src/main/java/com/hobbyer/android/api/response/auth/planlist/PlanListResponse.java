package com.hobbyer.android.api.response.auth.planlist;

import com.google.gson.annotations.SerializedName;
import com.hobbyer.android.api.BaseResponse;

public class PlanListResponse extends BaseResponse {
    @SerializedName("result")
    public PlanListResult planListResult;

    public PlanListResult getPlanListResult() {
        return planListResult;
    }

    public void setPlanListResult(PlanListResult planListResult) {
        this.planListResult = planListResult;
    }
}
