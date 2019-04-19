package com.hobbyer.android.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.hobbyer.android.api.response.auth.login.SignInData;
import com.hobbyer.android.constant.AppConstant;
import com.hobbyer.android.model.UserFaceBookModel;
import com.orhanobut.hawk.Hawk;

import java.util.StringTokenizer;

public class PrefManager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static final String EMPTY_STRING = "";
    private static PrefManager instance;
    Context _context;
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String USER_PROFILE_DETAILS = "USER_PROFILE";
    private static final String PREFS_NAME = "Hobbyer";
    public static final String KEY_ACCESS_TOKEN = "key_access_token";


    public PrefManager() {
        super();
    }

    public static PrefManager getSharedInstance() {
        if(instance == null)
            instance = new PrefManager();
        return instance;
    }



    public PrefManager(Context context) {
        this._context = context;
        String prefsFile = context.getPackageName();
        pref = _context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE);
        editor = pref.edit();
    }
    public boolean setStringInPreferences(String key, String value) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        return editor.commit();
    }
    private String getStringFromPreferances(String key) {
        String data = pref.getString(key, EMPTY_STRING);
        return data;
    }




    public void saveUserModel(UserFaceBookModel userModel) {
        setStringInPreferences(AppConstant.SharedPreferenceKeys.USER_NAME.getKey(), userModel.userName);
        setStringInPreferences(AppConstant.SharedPreferenceKeys.USER_EMAIL.getKey(), userModel.userEmail);
        setStringInPreferences(AppConstant.SharedPreferenceKeys.USER_IMAGE_URL.getKey(), userModel.profilePic);
    }

    public UserFaceBookModel getUserModelFromPreferences()
    {
        UserFaceBookModel userModel =null;
        String name = getStringFromPreferances(AppConstant.SharedPreferenceKeys.USER_NAME.getKey());
        if(!TextUtils.isEmpty(name)) {
            userModel = new UserFaceBookModel();
            userModel.userName = name;
            userModel.userEmail = getStringFromPreferances(AppConstant.SharedPreferenceKeys.USER_EMAIL.getKey());
            userModel.profilePic = getStringFromPreferances(AppConstant.SharedPreferenceKeys.USER_IMAGE_URL.getKey());
        }
        return userModel;
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public static String getUserMailId(Context conteActivity) {
          SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(conteActivity);
         String type = sharedPreferences.getString("Email", "");
         return type;
    }

    public static void saveMailId(Context context, String key, String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static String getPreferencesString(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key,null);
    }


    public static void savePreferences(Context context, String key, String value) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }
    private boolean getBooleanValue(String key) {
        return this.pref.getBoolean(key, false);
    }

    /**
     * Saving the preference
     */
    public void setBooleanValue(String key, boolean value) {
        this.editor.putBoolean(key, value);
        this.editor.commit();
    }







    public void Save(Context context,String key,double latitude) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, (long) latitude);
        editor.commit();
    }



    public void setKeyAccessToken(String token){
        setStringValue(KEY_ACCESS_TOKEN,token);
    }
    public String getKeyAccessToken(){
        return getStringValue(KEY_ACCESS_TOKEN);
    }


 /*  public static void removeSharedPrefrenceData(String projectPrefName,String contentPrefName) {
       try {
           SharedPreferences prefs = PreferenceManager.getSharedPreferences(projectPrefName,Context.MODE_PRIVATE);
           SharedPreferences.Editor prefEditor = prefs.edit();
           prefEditor.remove(contentPrefName);
           prefEditor.commit();
           prefEditor.clear();
       } catch (Exception e) {
           Log.i("", "Exception : " + e.toString());
       }

   }*/
 public <T> T getPreference(String key) {
     return (T) pref.getAll().get(key);
 }

    public <T> T getPreference(String key, T defValue) {
        T returnValue = (T) pref.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }

    private String getStringValue(String key) {
        return this.pref.getString(key,"");
    }

    /**
     * Saving the preference
     * @param key : Key of the preference.
     * @param value : Value of the preference.
     */
    private void setStringValue(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
    }

}
