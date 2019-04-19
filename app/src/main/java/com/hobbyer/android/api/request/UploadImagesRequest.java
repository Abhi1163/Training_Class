package com.hobbyer.android.api.request;

import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class UploadImagesRequest {

    @SerializedName("image")
    public File image;

    @SerializedName("id")
    public int id ;

    @SerializedName("type")
    public String type;

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
