package com.dsp.dspattendenceapp.activites;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.services.DeviceAdminService;
import com.dsp.dspattendenceapp.utills.Preferences;
import com.dsp.dspattendenceapp.utills.Utillity;
import com.jakewharton.threetenabp.AndroidThreeTen;

public class BaseActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1414;
    public Activity activity = BaseActivity.this;
    private static final int REQUEST_CODE = 1212;
    private static final int REQUEST_CODE_CAMRA = 1313;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 543;
    public Dialog progressDialog;
    private Boolean loading=false;
    private String imei = "";
    private boolean doubleTab=false;
    private DevicePolicyManager devicePolicyManager = null;
    private ComponentName adminCompName = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        getIEMI();
//        initDeviceAdmin();
    }

    private void initDeviceAdmin() {
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        adminCompName = DeviceAdminService.getComponentName(this);// Initializing the component;

        if (devicePolicyManager.isDeviceOwnerApp(getPackageName())) {

            devicePolicyManager.setLockTaskPackages(adminCompName, new String[]{getPackageName()});
        }
    }

    public void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void showWaitingDialog() {
        if(!loading)
        {
            runOnUiThread(() -> {
                try {
                    progressDialog = new Dialog(activity);
                    if (progressDialog.getWindow() != null) {
                        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    }
                    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.layout_lottie_loading);
                    progressDialog.show();
                    loading=true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

    public void dismissWaitingDialog() {
        runOnUiThread(() -> {
            try {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog.cancel();
                    loading=false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                progressDialog = null;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getIEMI() {
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (telephonyManager != null) {
                    try {
                        imei = telephonyManager.getImei();
                        Log.d("TAG", "getIEMI2: "+imei);
                    } catch (Exception e) {
                        e.printStackTrace();
                        imei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        Log.d("TAG", "getIEMI3: "+imei);
//                        Toast.makeText(this, ""+imei, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (telephonyManager != null) {
                    imei = telephonyManager.getDeviceId();
                    Log.d("TAG", "getIEMI4: "+imei);
                }
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
            }
        }
        return imei;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermission();
            } else {
            }
        } else if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                camraPermission();
                // Permission is granted. Proceed with location-related operations.
            } else {
                // Permission denied. Disable location-related functionality or show a message.
            }
        } else if (requestCode == REQUEST_CODE_CAMRA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                storagePermission();
            }
        } else if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }
        }
    }

    private void storagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // Request the permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_CODE);
            } else {
                // Permissions are already granted, proceed with your logic
            }
        }
    }

    private void camraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_CAMRA);
            }
        }
    }

    private void locationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {

        }
    }

    @Override
    public void onBackPressed() {
        if (doubleTab){
            finishAffinity();
            super.onBackPressed();
        }else {
            Toast.makeText(activity, getString(R.string.press_to_exit), Toast.LENGTH_SHORT).show();
            doubleTab=true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleTab=false;
                }
            },800);
        }

    }

}
