package com.hobbyer.android.api.response.auth.review;

import com.google.gson.annotations.SerializedName;
import com.hobbyer.android.api.BaseResponse;
import com.hobbyer.android.api.response.Pagination;
import com.hobbyer.android.api.response.auth.review.ReviewResult;

public class ReviewResponse extends BaseResponse {

    private ReviewResult result;
    private Pagination pagination;

    public ReviewResult getResult() {
        return result;
    }

    public void setResult(ReviewResult result) {
        this.result = result;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
