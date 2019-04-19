package com.hobbyer.android.prefs;

import com.hobbyer.android.api.response.auth.UserModel;
import com.hobbyer.android.api.response.auth.classdetails.ClassDetailData;
import com.hobbyer.android.api.response.auth.countpoint.CountPointData;
import com.hobbyer.android.api.response.auth.findclass.FindClassContentList;
import com.hobbyer.android.api.response.auth.homepage.HomePageContentList;
import com.hobbyer.android.api.response.auth.referearn.ReferEarnData;
import com.hobbyer.android.model.CardDataModel;
import com.hobbyer.android.model.FilterModel;
import com.hobbyer.android.model.ProfileImageModel;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

public class PreferenceUtils {

    private static final String KEY_USER_MODEL = "KEY_USER_MODEL";
    private static final String KEY_REFER_EARN = "KEY_REFER_EARN";
    private static final String KEY_HOME_MODEL = "KEY_HOME_MODEL";
    private static final String KEY_COUNT_POINT_MODEL = "KEY_COUNT_POINT_MODEL";
    private static final String KEY_CLASS_MODEL = "KEY_CLASS_MODEL";
    private static final String KEY_CARD_MODEL = "KEY_CARD_MODEL";
    private static final String KEY_SAVE_FIND_MODEL = "KEY_SAVE_FIND_MODEL";
    private static final String KEY_FIND_CLASS_MODEL = "KEY_FIND_CLASS_MODEL";
    private static final String FILTER_DATA = "FILTER_DATA";
    private static final String SAVE_PROFILE = "SAVE_PROFILE";
    private static final String KEY_PROFILE_IMAGE = "KEY_PROFILE_IMAGE";

    /*save Edit Profile Data*/
    public static void saveUserModel(UserModel userModel) {
        Hawk.delete(KEY_USER_MODEL);
        Hawk.put(KEY_USER_MODEL, userModel);
    }

    public static UserModel getUserModel() {
        return Hawk.get(KEY_USER_MODEL);
    }

    public static void deleteUserModel() {
        Hawk.delete(KEY_USER_MODEL);
    }

    //image

    public static void saveProfle(ProfileImageModel userModel) {
        Hawk.delete(KEY_PROFILE_IMAGE);
        Hawk.put(KEY_PROFILE_IMAGE, userModel);
    }

    public static ProfileImageModel getProfile() {
        return Hawk.get(KEY_PROFILE_IMAGE);
    }

    public static void deleteProfile() {
        Hawk.delete(KEY_PROFILE_IMAGE);
    }



    /*save referEarn Data*/
    public static void saveReferEarn(ReferEarnData userModel) {
        Hawk.delete(KEY_REFER_EARN);
        Hawk.put(KEY_REFER_EARN, userModel);
    }

    public static ReferEarnData getReferEarn() {
        return Hawk.get(KEY_REFER_EARN);
    }

    public static void deleteReferEarn() {
        Hawk.delete(KEY_REFER_EARN);
    }


    /*save Home Data*/
    public static void saveHome(HomePageContentList homeModel) {
        Hawk.delete(KEY_HOME_MODEL);
        Hawk.put(KEY_HOME_MODEL, homeModel);
    }

    public static HomePageContentList getHome() {
        return Hawk.get(KEY_HOME_MODEL);
    }

    public static void deleteHome() {
        Hawk.delete(KEY_HOME_MODEL);
    }


    /*save Home Data*/
    public static void savePoint(CountPointData homeModel) {
        Hawk.delete(KEY_COUNT_POINT_MODEL);
        Hawk.put(KEY_COUNT_POINT_MODEL, homeModel);
    }

    public static CountPointData getPoint() {
        return Hawk.get(KEY_COUNT_POINT_MODEL);
    }

    public static void deletePoint() {
        Hawk.delete(KEY_COUNT_POINT_MODEL);
    }


    public static void saveClassModel(ClassDetailData classDetailData) {
        Hawk.delete(KEY_CLASS_MODEL);
        Hawk.put(KEY_CLASS_MODEL, classDetailData);
    }

    public static ClassDetailData getClassModel() {
        return Hawk.get(KEY_CLASS_MODEL);
    }

    //Save Card Detail
    public static void saveCardDetail(CardDataModel classDetailData) {
        Hawk.delete(KEY_CARD_MODEL);
        Hawk.put(KEY_CARD_MODEL, classDetailData);
    }

    public static void saveFilter(FilterModel filterModel) {
        Hawk.delete(FILTER_DATA);
        Hawk.put(FILTER_DATA, filterModel);
    }

    public static FilterModel getFilter() {
        return Hawk.get(FILTER_DATA);
    }


    public static CardDataModel getCardModel() {
        return Hawk.get(KEY_CARD_MODEL);
    }

    public static void deleteCard() {
        Hawk.delete(KEY_CARD_MODEL);
    }

    public static void saveFindDetail(FindClassContentList findClassContentList) {
        Hawk.delete(KEY_FIND_CLASS_MODEL);
        Hawk.put(KEY_FIND_CLASS_MODEL, findClassContentList);
    }


    public static void saveFind(ArrayList<FindClassContentList> findClassContentList) {
        Hawk.delete(KEY_SAVE_FIND_MODEL);
        Hawk.put(KEY_SAVE_FIND_MODEL, findClassContentList);
    }

    public static ArrayList<FindClassContentList> getFindClass() {
        return Hawk.get(KEY_SAVE_FIND_MODEL);
    }

    public static void deleteFindClassModel() {
        Hawk.delete(KEY_FIND_CLASS_MODEL);
    }
}
