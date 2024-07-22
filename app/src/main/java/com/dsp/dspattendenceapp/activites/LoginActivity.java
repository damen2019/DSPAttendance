package com.dsp.dspattendenceapp.activites;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.pm.PackageManager;
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
import com.dsp.dspattendenceapp.models.LoginRequest;
import com.dsp.dspattendenceapp.models.UpdateDeviceIdModel;
import com.dsp.dspattendenceapp.network.ResponseHandler;
import com.dsp.dspattendenceapp.network.RestCaller;
import com.dsp.dspattendenceapp.network.RetrofitClient;
import com.dsp.dspattendenceapp.roomdb.dao.UserDao;
import com.dsp.dspattendenceapp.roomdb.databases.MyDatabase;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.utills.Preferences;
import com.dsp.dspattendenceapp.utills.Utillity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements ResponseHandler {
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 543;
    private static final int LOGIN_REQ_CODE = 01;
    private static final int UPDATE_DEVICE_ID_REQ_CODE = 03;
    private ActivityLoginBinding binding;
    private String iEMI="";
   
   MyDatabase myDb;
   UserDao userDao;
   boolean isLogedIn=false;
           

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        binding.tvalter.startAnimation(animation);
         initView();
         initDb();
         initClickListener();
         iEMI= getIEMI();
        Toast.makeText(activity, iEMI, Toast.LENGTH_SHORT).show();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            // Permission has already been granted
            // Proceed with location-related operations
        }

    }

    private void initView() {
//        if (Preferences.getSharedPrefValue(getApplicationContext(),"add") !=null && !Preferences.getSharedPrefValue(getApplicationContext(),"add").isEmpty()){
//            binding.tvadduser.setVisibility(View.GONE);
//        }
    }

    private void AddUserLocally(UserTable userTable) {
        UserTable userTableInsert=new UserTable(userTable.getEmpID(),userTable.getFullName(),userTable.getUsername(),userTable.getUserPass(),userTable.getMyTheme(),userTable.getDeptName(),userTable.getDesigName(),userTable.getOfficeName(),userTable.getOfficeID(),userTable.getDeptID(),userTable.getRoleName(),userTable.getUserRole(),userTable.getPositionID(),userTable.getPositionLvl(),userTable.getRankNum(),userTable.getPositionName(),userTable.getOfficeLevel(),userTable.getMainOffice(),userTable.getMainOfficeName(),userTable.getLTS_AoID(),userTable.getLTS_FoID(),userTable.getPositionRank(),userTable.getPWDChangeDate(),userTable.getIsIslamic(),userTable.getFYStart(),userTable.getFYEnd(),userTable.getFinYear(),userTable.getOfcID(),userTable.getParentOfcID(),userTable.getRegionCode(),userTable.getIsEnt(),userTable.getLatLon(),userTable.getIsWFH(),userTable.getProductPerm());
        userDao.insertUser(userTableInsert);
    }

    private void initDb() {
        myDb= Room.databaseBuilder(getApplicationContext(),MyDatabase.class,"usertable")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao=myDb.getUserDao();
    }
    private boolean Validation() {
        if (binding.etuserid.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter user id!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.etpassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter password!", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }
    private void initClickListener() {
        binding.tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation()){
                    UserTable userTable = userDao.login(binding.etuserid.getText().toString().trim(),binding.etpassword.getText().toString().trim());
                    Log.d("TAG", "onClick: "+userTable);
                    if (userTable !=null){
//                        clearField();
//                        Log.d("TAG", "onClick: "+userTable.getDeviceID() );
//                        if (userTable.getDeviceID() != null && !userTable.getDeviceID().equals("") && userTable.getDeviceID().equals(iEMI)){
                            Bundle bundle = new Bundle();
                            bundle.putString("userName",userTable.getFullName());
                            bundle.putString("userDesignation",userTable.getDesigName());
                            bundle.putString("empID",userTable.getUsername());
                            bundle.putString("OfficeID",userTable.getOfficeID().toString());
                            bundle.putString("OfficeName",userTable.getOfficeName());
                            bundle.putString("res","local");
                            Preferences.deleteKey(activity,"status");
                            Preferences.saveLoginDefaults(getApplicationContext(),"userData",userTable);
                            Utillity.startActivity(LoginActivity.this,ProfileActivity.class, Constants.START_ACTIVITY,bundle);
//                        }else {
//                            Toast.makeText(activity, "DeviceID Not Matched!", Toast.LENGTH_SHORT).show();
//                        }
                    }else {
                        loginUser();

                    }
                }
            }
        });
        binding.tvadduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 Preferences.saveSharedPrefValue(getApplicationContext(),"add","add");
//                 UserTable userTable=new UserTable("Faraz","Ahmad","Android Developer","10124008","1212");
//                 userDao.insertUser(userTable);
//                 binding.tvadduser.setVisibility(View.GONE);
                Utillity.startActivity(LoginActivity.this,SignUpActivity.class, Constants.START_ACTIVITY);

            }
        });
    }

    private void updateDeviceId() {
        UpdateDeviceIdModel updateDeviceIdModel= new UpdateDeviceIdModel(iEMI);
        new RestCaller(this, this, RetrofitClient.getInstance(LoginActivity.this).UpdateDeviceId(binding.etuserid.getText().toString().trim(),updateDeviceIdModel), UPDATE_DEVICE_ID_REQ_CODE);

    }

    private void loginUser() {
//        LoginRequest request = new LoginRequest(binding.etuserid.getText().toString().trim(),binding.etpassword.getText().toString().trim());
        showWaitingDialog();
        new RestCaller(this, this, RetrofitClient.getInstance(LoginActivity.this).login(binding.etuserid.getText().toString().trim(),binding.etpassword.getText().toString().trim()), LOGIN_REQ_CODE);

    }

    private void clearField() {
        binding.etpassword.setText("");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted. Proceed with location-related operations.
            } else {
                // Permission denied. Disable location-related functionality or show a message.
            }
        }
    }


    @Override
    public <T> void onSuccess(Object response, int reqCode) {
        if (reqCode== LOGIN_REQ_CODE){
//            clearField();
            dismissWaitingDialog();
            try {
                JSONObject model = null;
                JSONObject jsonObject=new JSONObject(Utillity.convertToString(response));
                String token= jsonObject.getString("token");
                Preferences.saveSharedPrefValue(activity,"token",token);
                Log.d("TAG", "onSuccess: "+token);
                JSONArray jsonArray = jsonObject.getJSONArray("Employee");
                for (int i = 0; i < jsonArray.length(); i++) {
                    model = jsonArray.getJSONObject(i);
                }
                UserTable userTable = (UserTable) Utillity.convertObject(model.toString(), UserTable.class);
                if (userTable !=null){
//                    Log.d("TAG", "onClick: "+userTable.getDeviceID() + " "+ iEMI);
//                    if (userTable.getDeviceID() != null && !userTable.getDeviceID().isEmpty()) {
//                        if ( userTable.getDeviceID().equals(iEMI)){
                            Bundle bundle = new Bundle();
                            bundle.putString("userName",userTable.getFullName());
                            bundle.putString("userDesignation",userTable.getDesigName());
                            bundle.putString("empID",userTable.getUsername());
                            bundle.putString("OfficeID",userTable.getOfficeID().toString());
                            bundle.putString("OfficeName",userTable.getOfficeName());
                            bundle.putString("res","server");
                            Preferences.deleteKey(activity,"status");
                            AddUserLocally(userTable);
                            Preferences.saveLoginDefaults(getApplicationContext(),"userData",userTable);
                            Utillity.startActivity(LoginActivity.this,ProfileActivity.class, Constants.START_ACTIVITY,bundle);

//                        }else {
//                            Toast.makeText(activity, "Device Id Not Matched With this Device!", Toast.LENGTH_SHORT).show();
//                        }
//                    }else {
//                        updateDeviceId();
//                    }

                }else {
                        Toast.makeText(LoginActivity.this, "Invalid user id or password!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (reqCode == UPDATE_DEVICE_ID_REQ_CODE){
            try {
                JSONObject object = new JSONObject(Utillity.convertToString(response));
                Toast.makeText(activity, object.getString("message"), Toast.LENGTH_SHORT).show();
                loginUser();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public void onFailure(Throwable t, int reqCode) {
//        clearField();
        dismissWaitingDialog();
        if (reqCode==LOGIN_REQ_CODE){
            if (t.getMessage().contains("400")){
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (reqCode == UPDATE_DEVICE_ID_REQ_CODE) {
            Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
}