package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class SearchFriendViewRequest {

    @SerializedName("user_id")
    public String userId;

    @SerializedName("keyword")
    public String keyword;

    @SerializedName("facebook_search")
    public int facebookSearch;

    public int getFacebookSearch() {
        return facebookSearch;
    }

    public void setFacebookSearch(int facebookSearch) {
        this.facebookSearch = facebookSearch;
    }

    @SerializedName("page_number")
    public int page_number;

    @SerializedName("page_size")
    public int page_size;


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
