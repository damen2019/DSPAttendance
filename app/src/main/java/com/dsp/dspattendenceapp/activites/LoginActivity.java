package com.dsp.dspattendenceapp.activites;


import static com.dsp.dspattendenceapp.utills.Utillity.openErrorDialog;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.databinding.ActivityLoginBinding;
import com.dsp.dspattendenceapp.global.Constants;
import com.dsp.dspattendenceapp.models.AddDeviceIdRequest;
import com.dsp.dspattendenceapp.models.UpdateDeviceIdModel;
import com.dsp.dspattendenceapp.network.ResponseHandler;
import com.dsp.dspattendenceapp.network.RestCaller;
import com.dsp.dspattendenceapp.network.RetrofitClient;
import com.dsp.dspattendenceapp.roomdb.dao.UserDao;
import com.dsp.dspattendenceapp.roomdb.databases.MyDatabase;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.utills.EncryptionUtils;
import com.dsp.dspattendenceapp.utills.Preferences;
import com.dsp.dspattendenceapp.utills.Utillity;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Base64;

public class LoginActivity extends BaseActivity implements ResponseHandler {
    private static final int LOGIN_REQ_CODE = 01;
    private static final int UPDATE_DEVICE_ID_REQ_CODE = 03;
    private static final int ADD_DEVICE_REQ_CODE = 04;
    private ActivityLoginBinding binding;
    private UserTable userTable;

    MyDatabase myDb;
    UserDao userDao;
    boolean isLogedIn = false;


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        binding.tvalter.startAnimation(animation);
        initView();
        initDb();
        initClickListener();

    }

    private void initView() {
        if (Preferences.getSharedPrefValue(getApplicationContext(),"userid") != null){
            binding.tvwelcome.setVisibility(View.VISIBLE);
            binding.tvwelcome.setText("Welcome Back : "+Preferences.getSharedPrefValue(getApplicationContext(),"userid"));
        }
    }

    private void AddUserLocally(UserTable userTable) {
        UserTable userTableInsert=new UserTable(userTable.getEmpID(),userTable.getFullName(),userTable.getUsername(),userTable.getUserPass(),userTable.getMyTheme(),userTable.getDeptName(),userTable.getDesigName(),userTable.getOfficeName(),userTable.getOfficeID(),userTable.getDeptID(),userTable.getRoleName(),userTable.getUserRole(),userTable.getPositionID(),userTable.getPositionLvl(),userTable.getRankNum(),userTable.getPositionName(),userTable.getOfficeLevel(),userTable.getMainOffice(),userTable.getMainOfficeName(),userTable.getLTS_AoID(),userTable.getLTS_FoID(),userTable.getPositionRank(),userTable.getPWDChangeDate(),userTable.getIsIslamic(),userTable.getFYStart(),userTable.getFYEnd(),userTable.getFinYear(),userTable.getOfcID(),userTable.getParentOfcID(),userTable.getRegionCode(),userTable.getIsEnt(),userTable.getLatLon(),userTable.getIsWFH(),userTable.getProductPerm(),userTable.getDeviceIMEI(),userTable.getIsAuthorized());
        userDao.insertUser(userTableInsert);
    }

    private void initDb() {
        myDb= Room.databaseBuilder(getApplicationContext(),MyDatabase.class,"usertable")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao=myDb.getUserDao();
    }
    private boolean Validation() {
        if (binding.etuserid.getText().toString().trim().isEmpty()){
            Utillity.openAlertDialog(activity,getString(R.string.please_enter_user_id));
            return false;
        } else if (binding.etpassword.getText().toString().trim().isEmpty()) {
            Utillity.openAlertDialog(activity,getString(R.string.please_enter_password));
            return false;
        }else {
            return true;
        }
    }
    private void initClickListener() {
        binding.tvlogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (Validation()){
                    UserTable userTable = userDao.login(binding.etuserid.getText().toString().trim(),binding.etpassword.getText().toString().trim());
                    if (userTable !=null) {
                        if (userTable.getDeviceIMEI() != null && !userTable.getDeviceIMEI().isEmpty() && userTable.getIsAuthorized() == 1) {
                            if (userTable.getDeviceIMEI().equals(getIEMI())) {
                                moveToProfileLocally(userTable);
                            } else {
                                Utillity.openAlertDialog(activity,getString(R.string.please_connect_admin_your_device_id_not_matched_with_this_device));
                            }
                        }else {
                            Utillity.openAlertDialog(activity,getString(R.string.local_is_not_matched));
                        }
                    }else {
                        if (!Utillity.isNetworkAvailable(getApplicationContext())){
                            Utillity.openAlertDialog(activity,getString(R.string.please_check_your_internet_connection_your_user_is_not_availible_locally));
                        }else {
                            loginUser();
                        }
                    }
                }
            }
        });
        binding.tvadduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utillity.startActivity(LoginActivity.this,SignUpActivity.class, Constants.START_ACTIVITY);

            }
        });
    }

    private void moveToProfileLocally(UserTable userTable) {
        Preferences.saveSharedPrefValue(activity,"userid",userTable.getEmpID());
        Bundle bundle = new Bundle();
        bundle.putString("userName", userTable.getFullName());
        bundle.putString("userDesignation", userTable.getDesigName());
        bundle.putString("empID", userTable.getUsername());
        bundle.putString("OfficeID", userTable.getOfficeID().toString());
        bundle.putString("OfficeName", userTable.getOfficeName());
        bundle.putString("res", "local");
//        Preferences.deleteKey(activity, "status");
        Preferences.saveLoginDefaults(getApplicationContext(), "userData", userTable);
        Utillity.startActivity(LoginActivity.this, ProfileActivity.class, Constants.START_ACTIVITY, bundle);

    }

    private void loginUser() {
        showWaitingDialog();
        new RestCaller(this, this, RetrofitClient.getInstance(LoginActivity.this).login(binding.etuserid.getText().toString().trim(),binding.etpassword.getText().toString().trim()), LOGIN_REQ_CODE);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public <T> void onSuccess(Object response, int reqCode) {
        if (reqCode== LOGIN_REQ_CODE){
            dismissWaitingDialog();
            try {
                JSONObject model = null;
                JSONObject jsonObject=new JSONObject(Utillity.convertToString(response));
                String token= jsonObject.getString("token");
                Preferences.saveSharedPrefValue(activity,"token",token);
                JSONArray jsonArray = jsonObject.getJSONArray("Employee");
                for (int i = 0; i < jsonArray.length(); i++) {
                    model = jsonArray.getJSONObject(i);
                }
                if (model != null){
                    userTable = (UserTable) Utillity.convertObject(model.toString(), UserTable.class);
                }
                if (userTable !=null){
                    Preferences.saveSharedPrefValue(activity,"userid",userTable.getEmpID());
                    if (userTable.getDeviceIMEI().isEmpty() && userTable.getIsAuthorized() == 0){
                        addDevice();
                    } else if (!userTable.getDeviceIMEI().isEmpty()  && userTable.getIsAuthorized() == 0) {
                        Utillity.openAlertDialog(activity,getString(R.string.please_connect_admin_you_are_not_authorized));
                    }else {
                        if (userTable.getDeviceIMEI().equals(getIEMI())){
                            moveToProfile(userTable);
                        }else {
                            Utillity.openAlertDialog(activity,getString(R.string.please_connect_admin_your_device_id_not_matched_with_this_device));
                        }
                    }
                }else {
                    Utillity.openAlertDialog(activity,getString(R.string.employee_is_found_please_check_your_credentials));
                }
            } catch (JSONException e) {
                Utillity.openErrorDialog(activity,e.getMessage());
            }
        }else if (reqCode == ADD_DEVICE_REQ_CODE){
            try {
                JSONObject object = new JSONObject(Utillity.convertToString(response));
                Utillity.openAlertDialog(activity,getString(R.string.device_id_not_exist_now_inserting_device_id_please_connect_admin_and_retry));
            } catch (JSONException e) {
                Utillity.openErrorDialog(activity,e.getMessage());
            }
        }
    }

    @Override
    public void onFailure(Throwable t, int reqCode) {
//        clearField();
        dismissWaitingDialog();
        if (reqCode==LOGIN_REQ_CODE){
            if (t.getMessage().contains("400")){
               Utillity.openErrorDialog(activity,getString(R.string.invalid_credentials));
            }else {
                Utillity.openErrorDialog(activity,t.getMessage());
            }
        } else if (reqCode == UPDATE_DEVICE_ID_REQ_CODE) {
            Utillity.openErrorDialog(activity,t.getMessage());
        }

    }


    private void moveToProfile(UserTable userTable) {
        Bundle bundle = new Bundle();
        bundle.putString("userName",userTable.getFullName());
        bundle.putString("userDesignation",userTable.getDesigName());
        bundle.putString("empID",userTable.getUsername());
        bundle.putString("OfficeID",userTable.getOfficeID().toString());
        bundle.putString("OfficeName",userTable.getOfficeName());
        bundle.putString("res","server");
//        Preferences.deleteKey(activity,"status");
        AddUserLocally(userTable);
        Preferences.saveLoginDefaults(getApplicationContext(),"userData",userTable);
        Utillity.startActivity(LoginActivity.this,ProfileActivity.class, Constants.START_ACTIVITY,bundle);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addDevice() {
        AddDeviceIdRequest addDeviceIdRequest=new AddDeviceIdRequest(getIEMI(),Utillity.getDeviceBrand(),Utillity.getDeviceModel(),binding.etuserid.getText().toString().trim(), Preferences.getSharedPrefValue(activity,"token"));
        new RestCaller(this, this, RetrofitClient.getInstance(LoginActivity.this).AddDevice(addDeviceIdRequest), ADD_DEVICE_REQ_CODE);

    }
}