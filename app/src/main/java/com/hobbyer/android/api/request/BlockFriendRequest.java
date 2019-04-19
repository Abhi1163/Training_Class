package com.hobbyer.android.api.request;

import com.google.gson.annotations.SerializedName;

public class BlockFriendRequest {

    @SerializedName("sender_id")
    public String sender_id;

    @SerializedName("reciever_id")
    public String reciever_id;

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReciever_id() {
        return reciever_id;
    }

    public void setReciever_id(String reciever_id) {
        this.reciever_id = reciever_id;
    }
}
