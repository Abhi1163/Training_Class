package com.hobbyer.android.events;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.interfaces.ApiResponseListener;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.utils.CommonUtils;

public class MyService extends Service implements LocationListener, ApiResponseListener {
    private Context context;
    private Location location;
    private Double lat = 0.0;
    private Double latSaved = 0.0;
    private Double longi = 0.0;
    private Double longiSaved = 0.0;
    private float[] arr = new float[3];


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = MyService.this;
        location = getLocation();
      /*  latSaved = Double.valueOf(PrefManager.getSharedInstance().getPreference(AppConstant.LAT));
        longiSaved = Double.valueOf(PrefManager.getSharedInstance().getPreference(AppConstant.LANG));
*/
//        Location.distanceBetween(lat, longi, latSaved, longiSaved, arr);

//        Location locationA = new Location(LocationManager.GPS_PROVIDER);
//        locationA.setLatitude(lat);
//        locationA.setLongitude(longi);
//        Location locationB = new Location(LocationManager.GPS_PROVIDER);
//        locationB.setLatitude(latSaved);
//        locationB.setLongitude(longiSaved);
//        float distance = locationA.distanceTo(locationB);

        /*when distance between current location and last location grater than 2 meter*/
        /*if (distance > 2) {
            callLocationChangeApi();
            lat = latSaved;
            longi = longiSaved;
        }*/

        /*LogUtils.warnLog("RESULT------", "last location" + lat + "   " + longi);
        LogUtils.warnLog("RESULT------", "current location" + latSaved + "   " + longiSaved);
        LogUtils.warnLog("RESULT---------------------------------------", "------------------------");*/

        return START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
      //  String lat = PrefManager.getSharedInstance().getPreference(AppConstant.LAT);
      //  String longi = PrefManager.getSharedInstance().getPreference(AppConstant.LANG);


        // Create the Handler object (on the main thread by default)
        Handler handler = new Handler();
        // Define the code block to be executed
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.d("Handlers", "Called on main thread");
                // Repeat this the same runnable code block again another 2 seconds
                // 'this' is referencing the Runnable object
                Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, 2000);
            }
        };
        // Start the initial runnable task by posting through the handler
        handler.post(runnableCode);


       /* if (!(lat.equalsIgnoreCase(String.valueOf(location.getLatitude())) && !(longi.equalsIgnoreCase(String.valueOf(location.getLongitude()))))) {
            callLocationChangeApi();
        }

        PrefManager.getSharedInstance().savePreference(AppConstant.LAT, "" + location.getLatitude());
        PrefManager.getSharedInstance().savePreference(AppConstant.LANG, "" + location.getLongitude());*/
    }

    private void callLocationChangeApi() {
        if (CommonUtils.isOnline(context)) {
            JsonObject jsonObject = new JsonObject();
           /* jsonObject.addProperty(AppConstant.LAT, PrefManager.getSharedInstance().getPreference(AppConstant.LAT, ""));
            jsonObject.addProperty(AppConstant.LANG, PrefManager.getSharedInstance().getPreference(AppConstant.LANG, ""));
            ApiManager.getSharedInstance().updateDriverLocation(new ApiCallBack(this, ApiConstant.LOCATION_UPDATE_API, context), jsonObject);
 */       }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    private Location getLocation() {
        try {
            LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            //getting GPS status
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //getting network status
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                //if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return null;
                        }
                        /**
                         * @param provider the name of the provider with which to register
                         * @param minTime minimum time interval between location updates, in milliseconds
                         * @param minDistance minimum distance between location updates, in meters
                         * @param listener a {@link LocationListener} whose
                         */
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20, 1000, this);

                        Log.d("GPS Enabled", "GPS Enabled");

                        if (locationManager != null) {
                            // location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            Log.e("Error : Location", "Impossible to connect to LocationManager", e);
        }

        return location;
    }


    @Override
    public void onApiSuccess(Object response, String apiName) {

    }

    @Override
    public void onApiFailure(String failureMessage, String apiName) {
        //ToastUtils.showToastShort(context, failureMessage);
    }
}