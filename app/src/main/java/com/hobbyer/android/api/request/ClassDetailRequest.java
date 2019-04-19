package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class ClassDetailRequest {

    @SerializedName("class_schedule_id")
    public String class_schedule_id;

    @SerializedName("user_id")
    public String user_id;
    @SerializedName("date")
    public String date;


    public String getClass_schedule_id() {
        return class_schedule_id;
    }

    public void setClass_schedule_id(String class_schedule_id) {
        this.class_schedule_id = class_schedule_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
