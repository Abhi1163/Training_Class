package com.hobbyer.android.api;

import com.google.gson.annotations.SerializedName;

/**
 * Base class for Api Response.
 */
public class BaseResponse {

    private int status;

    @SerializedName("response_code")
    private int responseCode;

    private String message;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    private String error;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

}
