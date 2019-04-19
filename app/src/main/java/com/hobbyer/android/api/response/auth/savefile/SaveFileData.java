package com.hobbyer.android.api.response.auth.savefile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveFileData {

    @SerializedName("url")
    @Expose
    private String url;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
