package com.hobbyer.android.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Ashutosh Kumar on 22/7/2018.
 */
public class ToastUtils {

    private ToastUtils() {
        throw new Error("U will not able to instantiate it");
    }

    /**
     * context The context
     * message The message
     */
    public static void showToastShort(Context context, String message) {
        if(!StringUtils.isBlank(message))
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * context The context
     * message The message
     */
    public static void showToastLong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
