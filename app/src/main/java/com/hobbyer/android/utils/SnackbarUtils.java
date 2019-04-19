package com.hobbyer.android.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarUtils {

    private SnackbarUtils() {
        throw new Error("U will not able to instantiate it");
    }

    /**
     * context The context
     * message The message
     */
    public static void showToastShort(View view, String message) {
        if(!StringUtils.isBlank(message))
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null)
                    .show();
    }

    /**
     * context The context
     * message The message
     */
    public static void showToastLong(View view, String message) {
        if(!StringUtils.isBlank(message))
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
    }



}
