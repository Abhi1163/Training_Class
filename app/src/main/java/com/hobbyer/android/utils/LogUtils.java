package com.hobbyer.android.utils;

import android.util.Log;

import com.hobbyer.android.BuildConfig;

public class LogUtils {
        private static final String LOG_PREFIX = "Hobbyer";
        private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
        private static final int MAX_LOG_TAG_LENGTH = 23;

        private LogUtils() {
        }

        public static String makeLogTag(String str) {
            if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
                return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
            }

            return LOG_PREFIX + str;
        }

        /**
         * Don't use this when obfuscating class names!
         */
        public static String makeLogTag(Class cls) {
            return makeLogTag(cls.getSimpleName());
        }

        public static void LOGD(final String tag, String message) {
            //noinspection PointlessBooleanExpression,ConstantConditions
            if (BuildConfig.DEBUG || Log.isLoggable(tag, Log.DEBUG)) {
                if (!StringUtils.isNullOrEmpty(message))
                    Log.d(tag, message);
            }
        }

        public static void LOGD(final String tag, String message, Throwable cause) {
            //noinspection PointlessBooleanExpression,ConstantConditions
            if (BuildConfig.DEBUG || Log.isLoggable(tag, Log.DEBUG)) {
                if (!StringUtils.isNullOrEmpty(message))
                    Log.d(tag, message, cause);
            }
        }

        public static void LOGV(final String tag, String message) {
            //noinspection PointlessBooleanExpression,ConstantConditions
            if (BuildConfig.DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
                Log.v(tag, message);
            }

        }

        public static void LOGV(final String tag, String message, Throwable cause) {
            //noinspection PointlessBooleanExpression,ConstantConditions
            if (BuildConfig.DEBUG && Log.isLoggable(tag, Log.VERBOSE)) {
                Log.v(tag, message, cause);
            }
        }

        public static void LOGI(final String tag, String message) {
            if (!StringUtils.isNullOrEmpty(message))
                Log.i(tag, message);
        }

        public static void LOGI(final String tag, String message, Throwable cause) {
            if (!StringUtils.isNullOrEmpty(message))
                Log.i(tag, message, cause);
        }

        public static void LOGW(final String tag, String message) {
            if (!StringUtils.isNullOrEmpty(message))
                Log.w(tag, message);
        }

        public static void LOGW(final String tag, String message, Throwable cause) {
            if (!StringUtils.isNullOrEmpty(message))
                Log.w(tag, message, cause);
        }

        public static void LOGE(final String tag, String message) {
            if (!StringUtils.isNullOrEmpty(message)) {
                Log.e(tag, message);
            /*if(!BuildConfig.DEBUG)
                Crashlytics.log(Log.ERROR, tag, message);*/
            }
        }

        public static void LOGE(final String tag, String message, Throwable cause) {
            if (cause != null && !StringUtils.isNullOrEmpty(message)) {
                Log.e(tag, message, cause);
            /*if(!BuildConfig.DEBUG) {
                Crashlytics.log(Log.ERROR, tag, message);
                Crashlytics.logException(cause);
            }*/
            }
        }

        public static void printStackTrace(Throwable e) {
            //e.printStackTrace();
            LogUtils.LOGE(LOG_PREFIX,"Connectivity>>"+e.getMessage());

        }
}