package com.hobbyer.android.api.response.auth.filter;

import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.response.Pagination;

public class CategoryListResponse extends BaseResponse {


    private CategoryListResult result;
    private Pagination pagination;

    public CategoryListResult getResult() {
        return result;
    }

    public void setResult(CategoryListResult result) {
        this.result = result;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
