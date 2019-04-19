package com.hobbyer.android;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.orhanobut.hawk.Hawk;

import io.fabric.sdk.android.Fabric;

public class HobbyerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(getApplicationContext()).build();
        Fabric.with(this, new Crashlytics());
    }
}
