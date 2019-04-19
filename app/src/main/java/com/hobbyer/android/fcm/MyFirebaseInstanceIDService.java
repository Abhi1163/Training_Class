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

package com.hobbyer.android.fcm;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.model.DeviceTokenEvent;
import com.hobbyer.android.prefs.PrefManager;
import com.hobbyer.android.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;


@SuppressWarnings("ALL")
@SuppressLint("ALL")
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

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
}