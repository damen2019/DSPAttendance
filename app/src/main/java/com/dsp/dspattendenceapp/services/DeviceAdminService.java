package com.dsp.dspattendenceapp.services;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class DeviceAdminService extends DeviceAdminReceiver {

    private String TAG = "DeviceAdminService";

    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context.getApplicationContext(), DeviceAdminReceiver.class);
    }

    void showLog(String msg) {
        String status = msg;
        Log.e(TAG, status);
    }

    @Override
    public void onEnabled(Context context, Intent intent) {
        showLog("Device Admin Enabled");
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {
        return "Do you want to disable device admin?";
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        showLog("Device Admin Disabled");
    }

    @Override
    public void onLockTaskModeEntering(Context context, Intent intent, String pkg) {
        showLog("KIOSK mode enabled");
    }

    @Override
    public void onLockTaskModeExiting(Context context, Intent intent) {
        showLog("KIOSK mode disabled");
    }
}
