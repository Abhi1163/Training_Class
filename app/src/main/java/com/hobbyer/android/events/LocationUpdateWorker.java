package com.hobbyer.android.events;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;


/*public class LocationUpdateWorker extends Worker {
    public static final String TAG = LocationUpdateWorker.class.getSimpleName();

    *//*private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };*//*

    @NonNull
    @Override
    public Result doWork() {
        Context applicationContext = getApplicationContext();
        try {
            new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(applicationContext, "Hello", Toast.LENGTH_SHORT).show());
            return Result.SUCCESS;
        } catch (Throwable throwable) {
            // Technically WorkManager will return WorkerResult.FAILURE
            // but it's best to be explicit about it.
            // Thus if there were errors, we're return FAILURE
            Log.e(TAG, "Error applying blur", throwable);
            return Result.FAILURE;
        }
    }*/

