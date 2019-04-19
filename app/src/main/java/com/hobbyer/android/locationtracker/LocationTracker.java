package com.hobbyer.android.locationtracker;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.hobbyer.android.R;
import com.hobbyer.android.interfaces.LocationResult;
import com.hobbyer.android.utils.ToastUtils;


@SuppressWarnings("ALL")
@SuppressLint("ALL")
public class LocationTracker implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult> {


    public static int REQUEST_CHECK_SETTINGS = 200;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mLocationClient;
    private Context context;
    private LocationResult locationResult;

    public LocationTracker(Context context, LocationResult locationResult) {
        super();
        this.context = context;
        this.locationResult = locationResult;
    }

    public void onUpdateLocation() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationClient = new GoogleApiClient.Builder(context, LocationTracker.this, LocationTracker.this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        if (mLocationClient.isConnected()) {
            Log.e(context.getString(R.string.location_tracker), context.getString(R.string.location_client_is_connected));
            startUpdates();
        } else {
            mLocationClient.connect();
            Log.e(context.getString(R.string.location_tracker), context.getString(R.string.location_client_is_going_to_connect));
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.e(context.getString(R.string.location_tracker), context.getString(R.string.on_connection_failed));
        ToastUtils.showToastShort(context, context.getString(R.string.unable_to_fetch_current_location));

    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.e(context.getString(R.string.location_tracker), context.getString(R.string.on_connected));
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mLocationClient,
                        builder.build()
                );

        result.setResultCallback(this);
        startUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(context.getString(R.string.location_tracker), context.getString(R.string.on_location_changed) + location.getLongitude());
        if (location.getLatitude() != 0 && location.getLongitude() != 0) {
            try {
                locationResult.gotLocation(location);
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*
			* uncomment this line if you want location only one time
			* */
			/*if (mLocationClient != null && mLocationClient.isConnected()) {
				stopPeriodicUpdates();
			}*/
        }
    }

    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {
        Log.e(context.getString(R.string.location_tracker), context.getString(R.string.start_periodics_updates));
        if (checkPermission())
            LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, LocationTracker.this);
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    public void stopPeriodicUpdates() {
        if (checkPermission())
            LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient, LocationTracker.this);


    }


    private void startUpdates() {
        Log.e(context.getString(R.string.location_tracker), context.getString(R.string.start_update));
        startPeriodicUpdates();

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
        Log.e(context.getString(R.string.location_tracker), context.getString(R.string.connection_suspended));
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    //failed to show
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }
}
