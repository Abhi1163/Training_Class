package com.hobbyer.android.constant;

public class AppConstant {

    public static final String HISTORY = "history";
    public static final String IS_LOGIN = "islogin";
    public static final String TERMS_AND_CONDITIONS_URL = "http://52.221.54.107/PROJECTS/ClassPass/trunk/public/api/terms-condition";
    public static final String URL_KEY = "url";
    public static final String TERMS_AND_CONDITIONS_TITLE = "Terms And Condition";
    public static final String PRIVACY_POLICY_URL = "http://52.221.54.107/PROJECTS/ClassPass/trunk/public/api/privacy-policy";
    public static final String HELP_CENTER_URL = "http://52.221.54.107/PROJECTS/ClassPass/trunk/public/api/help-center";
    public static final String COMMUNITY_GUIDE_URL = "http://52.221.54.107/PROJECTS/ClassPass/trunk/public/api/community-guide-lines";
    public static final String TERMS_CONDITION_URL = "http://52.221.54.107/PROJECTS/ClassPass/trunk/public/api/terms-condition";
    public static final String MEDIA_TYPE_IMAGE = "image";

    public static final String TITLE_NAME = "name";
    public static final String PRIVACY_POLICY_KEY = "privacy_name";
    public static final String POLICY_TITLE_NAME = "Privacy Policy";
    public static final String HELP_TITLE_NAME = "Help Center";
    public static final String COMMUNITY_GUIDE_NAME = "Community Guide Lines";
    public static final String TERMS_CONDITION_NAME = "Terms and Condition";
    public static final String SAVE__PROFILE = "Save Profile";
    public static final String FROM = "from";
    public static final String OTP = "otp";
    public static final String CHOSSEMEMBERSHIP = "choosemembership";
    public static final String MEMBERSHIP_TYPE = "membershipType";
    public static final String MEMBERSHIP_ID = "membershipId";
    public static final String SHARED_PREFRENCES = "myPref";
    public static final String DATE= "date";
    public static final int DEVICE_TYPE = 1;
    public static final String SHAREDPREF = "shared_pref";
    public static final String USER_PASSWORD ="password" ;
    public static final String USER_EMAIL ="email" ;
    public static final String DEVICE_TOKEN = "device_token";
    public static final String CHECK_CONNECTION ="check_connection" ;
    public static final String IS_CHAT_OPEN = "is_chat_open";
    public static final String NOTIFICATION_COUNT ="notification_count" ;
    public static final String USER_ID = "USER_ID";
    public static final String IS_OPEN ="is_open" ;
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public enum SharedPreferenceKeys {
        USER_NAME("userName"),
        USER_EMAIL("userEmail"),
        USER_IMAGE_URL("userImageUrl");


        private String value;

        SharedPreferenceKeys(String value) {
            this.value = value;
        }

        public String getKey() {
            return value;
        }
    }

    public interface BUNDLE_KEY {

        String ADDRESS_ID = "ADDRESS_ID";
        String ADDRESS_NAME = "ADDRESS_NAME";
        String ADDRESS_CITY = "ADDRESS_CITY";
        String ADDRESS_COUNTRY = "ADDRESS_COUNTRY";
        String ADDRESS_LATITUDE = "ADDRESS_LATITUDE";
        String ADDRESS_LONGITUDE = "ADDRESS_LONGITUDE";
        String USER_OTP = "USER_OTP";
        String USER_EMAIL = "USER_EMAIL";
        String CITY_NAME = "CITY_NAME";
        String CITY_LATITUDE = "CITY_LATITUDE";
        String CITY_LONGITUDE = "CITY_LONGITUDE";
        String CLASS_TYPE = "CLASS_TYPE";
        String PHONE_NUMBER = "PHONE_NUMBER";
        String COUNTRY_CODE = "COUNTRY_CODE";
        String TYPE_PHONE = "PHONE_TYPE";
        String TYPE_CODE = "TYPE_CODE";
        String TYPE_EMAIL = "EMAIL";
        String CLASS_SCHEDULE_ID = "CLASS_SCHEDULE_ID";
        String PROFILE = "PROFILE";
        String VIDEOS_LIST = "VIDEOS_LIST";
        String DATE = "DATE";
        String SettingBill = "SettingBill";
        String Membership = "Membership";
        String Membership_ID = "Membership_Id";
        String Membership_TYPE = "Membership_TYPE";
        String ISCurrent = "isCurrent";

        String OTP = "otp";
        String GET_MEMBERSHIP = "getmembership";
        String FREETRIAL = "freetrial";
        String FREE = "free";
        String RESERVED = "RESERVED";
        public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    }
}
