package com.hobbyer.android.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;


public class ConverterUtils {
    private static String TAG = ConverterUtils.class.getSimpleName();

    public static RequestBody parseString(String str) {
        if (str == null) {
            str = "";
        }
        return RequestBody.create(MediaType.parse("text/plain"), str);
    }


    public static MultipartBody.Part getMultipartFromFile(String partName, File imageFile) {

        if (imageFile != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            return MultipartBody.Part.createFormData(partName, imageFile.getName(), requestFile);
        } else {
            return MultipartBody.Part.createFormData(partName, "", new RequestBody() {
                @Nullable
                @Override
                public MediaType contentType() {
                    return null;
                }

                @Override
                public void writeTo(@NonNull BufferedSink sink) throws IOException {

                }
            });
        }
        // MultipartBody.Part is used to send also the actual file name
    }
}
