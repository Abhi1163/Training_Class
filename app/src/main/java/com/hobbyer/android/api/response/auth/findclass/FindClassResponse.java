package com.hobbyer.android.api.response.auth.findclass;

import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.response.Pagination;

public class FindClassResponse extends BaseResponse {

   private FindClassResult result;
  private Pagination pagination;


    public FindClassResult getResult() {
        return result;
    }

    public void setResult(FindClassResult result) {
        this.result = result;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
