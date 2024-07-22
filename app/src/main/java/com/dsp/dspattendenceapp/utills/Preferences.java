package com.dsp.dspattendenceapp.utills;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.dsp.dspattendenceapp.global.Constants;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.google.gson.Gson;

import java.util.Random;

public class Preferences {
    private static final String SHARED_PREFRENCES_KEY = "UserSharedPrefs";

    public static void saveSharedPrefValue(Context mContext, String key, String value) {
        SharedPreferences UserSharedPrefs = mContext.getSharedPreferences(SHARED_PREFRENCES_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor edit = UserSharedPrefs.edit();
        edit.putString(key, scrambleText(value));
        edit.apply();
    }

    public static String getSharedPrefValue(Context mContext, String key) {
        SharedPreferences UserSharedPrefs = mContext.getSharedPreferences(SHARED_PREFRENCES_KEY, Activity.MODE_PRIVATE);
        String value = UserSharedPrefs.getString(key, null);
        return unScrambleText(value);
    }
    private static String scrambleText(String text) {
        try {
            Random r = new Random();
            String prefix = String.valueOf(r.nextInt(90000) + 10000);

            String suffix = String.valueOf(r.nextInt(90000) + 10000);

            text = prefix + text + suffix;

            byte[] bytes = text.getBytes("UTF-8");
            byte[] newBytes = new byte[bytes.length];

            for (int i = 0; i < bytes.length; i++) {
                newBytes[i] = (byte) (bytes[i] - 1);
            }

            return new String(newBytes, "UTF-8");
        } catch (Exception e) {
            return text;
        }
    }

    private static String unScrambleText(String text) {
        try {
            byte[] bytes = text.getBytes("UTF-8");
            byte[] newBytes = new byte[bytes.length];
            for (int i = 0; i < bytes.length; i++) {
                newBytes[i] = (byte) (bytes[i] + 1);
            }
            String textVal = new String(newBytes, "UTF-8");

            return textVal.substring(5, textVal.length() - 5);
        } catch (Exception e) {
            return text;
        }
    }

    public static void saveLoginDefaults(Context mContext,String key, UserTable myObject) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFRENCES_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(myObject);
        editor.putString(Constants.USERDEFAULT_USER_DATA, json);
        editor.putBoolean(Constants.USERDEFAULT_ISLOGGEDIN, true);
        editor.apply();
    }

    public static UserTable getUserDetails(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFRENCES_KEY, Activity.MODE_PRIVATE);
        String val = sharedPreferences.getString(Constants.USERDEFAULT_USER_DATA, null);
        return new Gson().fromJson(val, UserTable.class);

    }

    public static void deleteKey(Context mContext , String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(SHARED_PREFRENCES_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }
}
