/*
 * Copyright © 2017 SeatXchange. All rights reserved.
 * Developed by Appster.
 *
 */

package com.hobbyer.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Appster
 * Used for Resizing Bitmap image as per sample size
 */

public class ImageUtil {


    public static File getFileFromBitmap(Bitmap mBitmap, Context context) {
        File file = null;

        if (mBitmap == null) {
            return file;
        }

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
        file = new File(Environment.getExternalStorageDirectory() + File.separator + "type=image/jpeg");
        FileOutputStream fileOutputStream;
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file, false);
        } catch (IOException e) {
            LogUtils.LOGE(ImageUtil.class.getName(), e.getMessage());
            File outputDir = context.getCacheDir(); // context being the Activity pointer
            File outputFile = null;
            try {
                outputFile = File.createTempFile("img", "png", outputDir);
            } catch (IOException e1) {
                LogUtils.LOGE(ImageUtil.class.getName(), e1.getMessage());
            }
            return outputFile;
        }
        try {
            fileOutputStream.write(bytes.toByteArray());
            fileOutputStream.close();
        } catch (IOException e) {
            LogUtils.LOGE(ImageUtil.class.getName(), e.getMessage());
        }
        return file;
    }

    /**
     * Returns resized bitmap
     *
     * @param ctx
     * @param selectedImage
     * @return
     * @throws FileNotFoundException
     */
    public static Bitmap decodeUri(Context ctx, Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                ctx.getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 180;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(selectedImage), null, o2);
    }
}
