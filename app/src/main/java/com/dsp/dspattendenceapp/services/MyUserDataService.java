package com.dsp.dspattendenceapp.services;

import static com.dsp.dspattendenceapp.network.RetrofitClient.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import androidx.room.Room;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.activites.LoginActivity;
import com.dsp.dspattendenceapp.network.ResponseHandler;
import com.dsp.dspattendenceapp.network.RestCaller;
import com.dsp.dspattendenceapp.network.RetrofitClient;
import com.dsp.dspattendenceapp.roomdb.dao.UserDao;
import com.dsp.dspattendenceapp.roomdb.databases.MyDatabase;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.utills.Preferences;
import com.dsp.dspattendenceapp.utills.Utillity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class MyUserDataService extends IntentService implements ResponseHandler {


    private static final int USER_DATA_REQ_CODE = 02;
    MyDatabase myDb;
    UserDao userDao;
    public MyUserDataService() {
        super("MyUserDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("BackgroundService", "Executing background task...");
        initDb();
        if (intent !=null){
            String userDataOption = intent.getStringExtra("userDataKey");
        if (Preferences.getUserDetails(getApplicationContext()) !=null && Utillity.isNetworkConnected(getApplicationContext())) {
            UserTable userTable = Preferences.getUserDetails(getApplicationContext());
            Log.d("BackgroundServiceee", userDataOption);
            new RestCaller(this, this, RetrofitClient.getInstance(activity).login(userTable.getUsername(),userTable.getUserPass()), USER_DATA_REQ_CODE);
//            scheduleNextUpdate();
        }
        }
    }

    private void initDb() {
        myDb= Room.databaseBuilder(getApplicationContext(),MyDatabase.class,"usertable")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao=myDb.getUserDao();
    }
    private void scheduleNextUpdate() {
        long nextUpdateTimeMillis = System.currentTimeMillis() + (4 * 1000); // 4 minutes in milliseconds

        Intent intent = new Intent(getApplicationContext(), MyUserDataService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, nextUpdateTimeMillis, pendingIntent);
        }
    }

    @Override
    public <T> void onSuccess(Object response, int reqCode) {
        if (reqCode== USER_DATA_REQ_CODE){
            try {
                JSONObject object = new JSONObject(Utillity.convertToString(response));
                UserTable userTable = (UserTable) Utillity.convertObject(object.toString(), UserTable.class);
                UpdateUserLocally(userTable);
            } catch (JSONException e) {
                Utillity.openErrorDialog(activity,e.getMessage());
            }
        }
    }

    private void UpdateUserLocally(UserTable userTable) {

        userDao.updateUser(userTable.getEmpID(),userTable.getFullName(),userTable.getUsername(),userTable.getUserPass(),userTable.getMyTheme(),userTable.getDeptName(),userTable.getDesigName(),userTable.getOfficeName(),userTable.getOfficeID(),userTable.getDeptID(),userTable.getRoleName(),userTable.getUserRole(),userTable.getPositionID(),userTable.getPositionLvl(),userTable.getRankNum(),userTable.getPositionName(),userTable.getOfficeLevel(),userTable.getMainOffice(),userTable.getMainOfficeName(),userTable.getLTS_AoID(),userTable.getLTS_FoID(),userTable.getPositionRank(),userTable.getPWDChangeDate(),userTable.getIsIslamic(),userTable.getFYStart(),userTable.getFYEnd(),userTable.getFinYear(),userTable.getOfcID(),userTable.getParentOfcID(),userTable.getRegionCode(),userTable.getIsEnt(),userTable.getLatLon(),userTable.getIsWFH(),userTable.getProductPerm());
    }

    @Override
    public void onFailure(Throwable t, int reqCode) {
        Utillity.openErrorDialog(activity,t.getMessage());
    }


}