package com.hobbyer.android.api.response.auth.findclasstomorrow;

import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.response.Pagination;

public class FindClassTomorrowResponse extends BaseResponse {

    private FindClassTomorrowResult result;
    private Pagination pagination;
    public FindClassTomorrowResult getResult() {
        return result;
    }

    public void setResult(FindClassTomorrowResult result) {
        this.result = result;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
