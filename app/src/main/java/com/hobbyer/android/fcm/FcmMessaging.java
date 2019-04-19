package com.hobbyer.android.fcm;
/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.hobbyer.android.R;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.utils.LogUtils;
import com.hobbyer.android.utils.NotificationUtils;
import com.hobbyer.android.utils.SharedPref;
import com.hobbyer.android.view.activities.home.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import static com.hobbyer.android.constant.AppConstant.PUSH_NOTIFICATION;


@SuppressWarnings("ALL")
@SuppressLint("ALL")
public class FcmMessaging extends FirebaseMessagingService {

    private static final String TAG = FcmMessaging.class.getSimpleName();
    public static NotificationManager notificationManager;
    private String filterMessage = "";
    private String title = "Hobbyer";
    private PendingIntent pendingIntent;
    private Intent intent;
    private Context context;
    private SharedPref prefManager;
    private NotificationUtils notificationUtils;
    private FCMResponse fcmResponse;

    public void onMessageReceived(RemoteMessage remoteMessage) {

        LogUtils.LOGD("onMessageReceived", remoteMessage.toString());
        Log.e("onMessageReceived", remoteMessage.toString());
        context = FcmMessaging.this;
        prefManager = SharedPref.getInstance(context);
        if (remoteMessage != null && remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
            Gson gson = new Gson();

            JSONObject jsonObject = new JSONObject(remoteMessage.getData());
            FCMResponse fcmResponse = gson.fromJson(jsonObject.toString(), FCMResponse.class);

          /*  JSONObject jsonObject = new JSONObject(remoteMessage.getData());
            fcmResponse = gson.fromJson(remoteMessage.getData().toString(), FCMResponse.class);*/

            /* if (*//*ActivityViewModel.isVisible &&*//* fcmResponse.getMessage().get(0).getNotification_type().equalsIgnoreCase("BookingConfirmation")) {
                Intent intent = new Intent(mContext, NotificationActivity.class);
                startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } else {
            }*/



              /*  try {
                    if (!fcmResponse.getMessage().get(0).getNotification_type().equalsIgnoreCase("Message")) {
                        int count = Integer.valueOf(prefManager.getStringPreferences(context,AppConstant.NOTIFICATION_COUNT));
                        count++;
                        prefManager.saveStringPreferences(context,AppConstant.NOTIFICATION_COUNT, "" + count);
                        EventBus.getDefault().post(new NotificationModel("" + count));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }*/
            sendNotification(fcmResponse);
        }
    }


    private void sendNotification(FCMResponse fcmResponse) {

/*

        if (!StringUtils.isBlank(prefManager.getStringPreferences(context,AppConstant.USER_ID))) {
            if (fcmResponse.getMessage().get(0).getNotification_type().equalsIgnoreCase("1")) {
                Fragment fragment=new UpComingFragment();
                CommonUtils.setFragment(fragment,true, (FragmentActivity) context,R.id.flHome);

                }
                //  EventBus.getDefault().post(new NotificationModel(fcmResponse.getMessage().get(0).getNotification_type(),
                //   fcmResponse.getMessage().get(0).getJob_id(), fcmResponse.getMessage().get(0).getFrom_location(), fcmResponse.getMessage().get(0).getTo_location()));

            } else if (fcmResponse.getMessage().get(0).getNotification_type().equalsIgnoreCase("2")) {
            Fragment fragment=new ProfileFragment();
            CommonUtils.setFragment(fragment,true, (FragmentActivity) context,R.id.flHome);
        }
        else if (fcmResponse.getMessage().get(0).getNotification_type().equalsIgnoreCase("3")) {
            Fragment fragment=new ProfileFragment();
            CommonUtils.setFragment(fragment,true, (FragmentActivity) context,R.id.flHome);
        }
*/

        Intent intent = new Intent(this, HomeActivity.class);

// Creating a pending intent and wrapping our intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        try {
            // Perform the operation associated with our pendingIntent
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }


        setUpNotifcaition(fcmResponse.getMessage(), pendingIntent);


    }

    private void setUpPendingIntent(final NotificationModel notificationModel) {

    }


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        if (s != null && !s.equalsIgnoreCase("")) {
            PrefManager.savePreferences(getApplicationContext(), AppConstant.DEVICE_TOKEN, s);
            LogUtils.LOGE(/*TAG*/"token", s);

        } else {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }
                            // Get newback Instance ID token
                            String token = task.getResult().getToken();
                            PrefManager.savePreferences(getApplicationContext(), AppConstant.DEVICE_TOKEN, token);
                            LogUtils.LOGE(/*TAG*/"token", token);
                        }
                    });
        }
    }


//******************* Call when generate Push notification **************************

    private void setUpNotifcaition(String messageBody, PendingIntent pendingIntent) {
        if (messageBody != null && messageBody.length() > 0) {

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        // .setColor(getResources().getColor(R.color.Yellow1))
                        .setContentTitle(title)
                        .setContentText(messageBody).setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(messageBody))
                        .setAutoCancel(true)
                        .setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary))
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


            } else {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(messageBody).setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(messageBody))
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
                notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        //Log.e(TAG, "push json: " + json.toString());

        Toast.makeText(FcmMessaging.this, json.toString(), Toast.LENGTH_SHORT).show();
        try {
            JSONObject data = json.getJSONObject("data");

            String title = data.getString("title");
            String message = data.getString("message");
            boolean isBackground = data.getBoolean("is_background");
            String imageUrl = data.getString("image");
            String timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            //Log.e(TAG, "title: " + title);
            //Log.e(TAG, "message: " + message);
            //Log.e(TAG, "isBackground: " + isBackground);
            //Log.e(TAG, "payload: " + payload.toString());
            //Log.e(TAG, "imageUrl: " + imageUrl);
            //Log.e(TAG, "timestamp: " + timestamp);


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (JSONException e) {
            //Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            //Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}


/*xtends FirebaseInstanceIdService {

    private String refreshedToken = "";


    @Override
    public void onTokenRefresh() {
        PrefManager prefManager = PrefManager.getSharedInstance();

        try {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            if (refreshedToken != null && !refreshedToken.equalsIgnoreCase("")) {

                prefManager.savePreferences(this,AppConstant.DEVICE_TOKEN, refreshedToken);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sendRegistrationToServer(refreshedToken);

        Log.e("deviceToken: ", refreshedToken);

    }

    private void sendRegistrationToServer(String refreshedToken) {
        sendBroadcast(new Intent("Token").putExtra("refreshedToken", refreshedToken));
    }
}*/