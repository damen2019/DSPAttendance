package com.dsp.dspattendenceapp.activites;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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

public class BaseActivity extends AppCompatActivity {
    public Activity activity = BaseActivity.this;
    private static final int REQUEST_CODE = 1212;
    public Dialog progressDialog;
    private Boolean loading=false;
    private String imei = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                Toast.makeText(activity, "Permission granted.", Toast.LENGTH_SHORT).show();
                getIEMI();
            } else {
                Toast.makeText(activity, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
