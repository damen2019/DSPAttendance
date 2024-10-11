package com.dsp.dspattendenceapp.global;

public class Constants {
    public static final String DEVICE_TYPE_ANDROID = "Android";
    public static final String KEY_STATUS = "status";
    public static final String KEY_RESULT = "Student";
    public static final String USERDEFAULT_ISLOGGEDIN = "isLoggedIn";
    public static final String USERDEFAULT_USER_DATA = "userData";
    public static final String USERDEFAULT_GCM_STRING = "gcmString";


    /**************************************************
     *
     *		 Constants
     *
     **************************************************/

    public static String FLOW_TYPE = "";
    public static final int START_ACTIVITY = 0;
    public static final int FINISH_ACTIVITY = 1;
    public static final int CLEAR_BACK_STACK = 2;
    public static final String EXPIRED = "expired";
    public static final String ACTIVE = "active";
    public static final String REDEEMED = "redeemed";
    public static final String IMAGE = "IMAGE";

    public static final String TRADER = "TRADER";
    public static final String COLOR = "COLOR";
    public static final String PERCENTAGE = "PERCENTAGE";
    public static final String OFFER = "offer";
    public static final String REWARD = "reward";

    public static final String CAR = "Car";

    public static final String CASH = "Cash";
    public static final String BANK = "Bank";
    // who will pay status
    public static final String CREDIT = "Credit";
    public static final String COD = "COD";
    public static final String PICKUP_CASH_PAID = "Pickup cash/paid";


    /**************************************************
     *
     *		 URLS
     *
     **************************************************/
    public static final String BASE_URL_GOOGLE = "https://maps.googleapis.com/maps/"; // test server

    // test sever
//    public static final String BASE_URL = "https://s1test.damensp.com/DSPAPI/Employee/";
    // live server
    public static final String BASE_URL = "https://mis.damensp.com/DSPApi/Employee/";
    public static final String BASE_URL_SERVER = "https://damensp.com/MIS/";
    public static final String PROFILE_IMAGES = BASE_URL + "profileImage/";
    public static final String IMAGES_URL = BASE_URL + "uploadImages/";
    public static final String termsURL = BASE_URL + "terms";
    public static final String privacyURL = BASE_URL + "privacy";


}