package com.dsp.dspattendenceapp.activites;

import static androidx.core.content.PermissionChecker.checkSelfPermission;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.databinding.ActivityMainBinding;
import com.dsp.dspattendenceapp.global.Constants;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.utills.Preferences;
import com.dsp.dspattendenceapp.utills.Utillity;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_CODE = 1212;
    ActivityMainBinding binding;
    private String imei = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        binding.img.startAnimation(animation);

        moveToNextActiviy();
    }

    private void moveToNextActiviy() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Preferences.getUserDetails(getApplicationContext()) !=null){
                    UserTable userTable= Preferences.getUserDetails(getApplicationContext());
                    Log.d("TAG", "run: "+userTable);
                    Bundle bundle = new Bundle();
                    bundle.putString("userName",userTable.getFullName());
                    bundle.putString("userDesignation",userTable.getDesigName());
                    bundle.putString("empID",userTable.getUsername());
                    bundle.putString("OfficeID",userTable.getOfficeID().toString());
                    bundle.putString("OfficeName",userTable.getOfficeName());
                    bundle.putString("res","local");
                    Preferences.saveLoginDefaults(getApplicationContext(),"userData",userTable);
                    Utillity.startActivity(MainActivity.this,ProfileActivity.class, Constants.FINISH_ACTIVITY,bundle);

                }else {
                    Utillity.startActivity(MainActivity.this,LoginActivity.class, Constants.FINISH_ACTIVITY);

                }

            }
        },800);
    }


}