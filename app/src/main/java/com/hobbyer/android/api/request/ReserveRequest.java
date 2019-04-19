package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class ReserveRequest {

    @SerializedName("class_scheduled_id")
    public String class_scheduled_id;

    @SerializedName("class_id")
    public int class_id;

    @SerializedName("user_id")
    public long user_id;

    @SerializedName("studio_id")
    public String studio_id;

    @SerializedName("class_point")
    public int class_point;

    @SerializedName("reserve_key")
    public boolean reserve_key;


    @SerializedName("reserve_date")
    public String reserve_date;

    @SerializedName("reservation_id")
    public String reservation_id;

    @SerializedName("reserve_id")
    public String reserve_id;

    @SerializedName("booking_start_time")
    public String booking_start_time;

    @SerializedName("booking_end_time")
    public String booking_end_time;


    public String getClass_scheduled_id() {
        return class_scheduled_id;
    }

    public void setClass_scheduled_id(String class_scheduled_id) {
        this.class_scheduled_id = class_scheduled_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getStudio_id() {
        return studio_id;
    }

    public void setStudio_id(String studio_id) {
        this.studio_id = studio_id;
    }

    public int getClass_point() {
        return class_point;
    }

    public void setClass_point(int class_point) {
        this.class_point = class_point;
    }

    public boolean isReserve_key() {
        return reserve_key;
    }

    public void setReserve_key(boolean reserve_key) {
        this.reserve_key = reserve_key;
    }

    public String getReserve_date() {
        return reserve_date;
    }

    public void setReserve_date(String reserve_date) {
        this.reserve_date = reserve_date;
    }

    public String getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(String reservation_id) {
        this.reservation_id = reservation_id;
    }

    public String getReserve_id() {
        return reserve_id;
    }

    public void setReserve_id(String reserve_id) {
        this.reserve_id = reserve_id;
    }

    public String getBooking_start_time() {
        return booking_start_time;
    }

    public void setBooking_start_time(String booking_start_time) {
        this.booking_start_time = booking_start_time;
    }

    public String getBooking_end_time() {
        return booking_end_time;
    }

    public void setBooking_end_time(String booking_end_time) {
        this.booking_end_time = booking_end_time;
    }
}
