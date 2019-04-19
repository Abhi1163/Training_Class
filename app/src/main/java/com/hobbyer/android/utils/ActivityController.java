package com.hobbyer.android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;

public class ActivityController {

    public static void startActivity(Activity activity, Class clazz, boolean isClearPrevious, boolean isClearStack) {
        Intent intent = new Intent(activity, clazz);
        ActivityCompat.finishAffinity(activity);
        activity.startActivity(intent);
        if (isClearStack && isClearPrevious || isClearStack)
            ActivityCompat.finishAffinity(activity);
        if (isClearPrevious)
            activity.finish();

    }
    public static void startActivity(Activity activity, Class clazz, Bundle bundle) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
    public static void startActivity(Activity activity, Class clazz, Bundle bundle,boolean isClearPrevious) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        if (isClearPrevious)
            activity.finish();
    }
    public static void startActivity(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }
    public static void startActivity(Activity activity, Class clazz, boolean isClearPrevious) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);
        if (isClearPrevious)
            activity.finish();
    }
    public static void startActivityForResult(Activity activity, Class clazz, int requestCode,Bundle bundle,boolean isClearPrevious) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
        if (isClearPrevious)
            activity.finish();
    }
    public static void startActivityForResult(AppCompatActivity activity, Class clazz, Bundle bundle, boolean isClearPrevious) {
        Intent intent = new Intent(activity, clazz);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        if (isClearPrevious) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.finish();
        }


    }
    public static void setFragment(Fragment fragment, boolean removeStack, FragmentActivity activity, int mContainer) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ftTransaction = fragmentManager.beginTransaction();
        if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ftTransaction.replace(mContainer, fragment);
        } else {
            ftTransaction.replace(mContainer, fragment);
            ftTransaction.addToBackStack(null);
        }
        ftTransaction.commit();
    }
    public static void setFragments(Fragment fragment, boolean removeStack, FragmentManager activity, int mContainer) {
        FragmentManager fragmentManager = activity;
        FragmentTransaction ftTransaction = fragmentManager.beginTransaction();
        if (removeStack) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ftTransaction.replace(mContainer, fragment);
        } else {
            ftTransaction.replace(mContainer, fragment);
            ftTransaction.addToBackStack(null);
        }
        ftTransaction.commit();
    }
    public static void setFragmentChild(Fragment fragment,Bundle bundle, int mContainer, FragmentManager fm) {

        FragmentManager fragmentManager = fm;
        FragmentTransaction ftTransaction = fragmentManager.beginTransaction();
            ftTransaction.replace(mContainer, fragment);
            fragment.setArguments(bundle);
            ftTransaction.addToBackStack(null);
        ftTransaction.commit();

    }

    }
