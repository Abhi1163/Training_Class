package com.hobbyer.android.interfaces;

/**
 * Created by Ashutosh Kumar on 22/7/2018.
 */

public interface ApiResponseListener<T> {
    void onApiSuccess(T response, String apiName);
    void onApiFailure(String failureMessage, String apiName);
}
