package com.hobbyer.android.interfaces;

import android.annotation.SuppressLint;
import android.location.Location;
import android.support.annotation.NonNull;

@SuppressWarnings("ALL")
@SuppressLint("ALL")
public interface LocationResult {

    void gotLocation(Location location);
}