package com.dsp.dspattendenceapp.services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.models.MarkAttendenceRequest;
import com.dsp.dspattendenceapp.network.ResponseHandler;
import com.dsp.dspattendenceapp.network.RestCaller;
import com.dsp.dspattendenceapp.network.RetrofitClient;
import com.dsp.dspattendenceapp.roomdb.dao.AttendenceDao;
import com.dsp.dspattendenceapp.roomdb.dao.AttendenceLogDao;
import com.dsp.dspattendenceapp.roomdb.databases.MyDatabase;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceLogTable;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;
import com.dsp.dspattendenceapp.utills.Preferences;
import com.dsp.dspattendenceapp.utills.Utillity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UploadUnSyncAttendenceService extends Service implements ResponseHandler {
    private static final int MARK_EMP_ATTENDENCE_REQ_CODE = 07;
    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    MyDatabase myDb;
    AttendenceLogDao attendenceLogDao;
    String empID;
    private List<AttendenceLogTable> listNotSync = new ArrayList<>();


    @SuppressLint("ForegroundServiceType")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    Log.d("TAG", "Foreground service is running");
                    initDb();
                    if (intent != null) {
                        empID = intent.getStringExtra("empID");
                        listNotSync = attendenceLogDao.getAllAttendanceLogs(empID,true);
                        if (listNotSync != null && listNotSync.size() > 0 && Utillity.isNetworkAvailable(getApplicationContext())) {
                            Gson gson = new Gson();
                            String jsonString = gson.toJson(listNotSync);
                            markEmpAttendence(jsonString, empID);
                        }
                    }
                    try {
                        Thread.sleep(8000);
                    }catch (Exception e){

                    }
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    private void markEmpAttendence(String jsonString, String empID) {
        MarkAttendenceRequest markAttendenceRequest = new MarkAttendenceRequest(empID, Preferences.getSharedPrefValue(getApplicationContext(), "token"), jsonString);
        new RestCaller(getApplicationContext(), this, RetrofitClient.getInstance(RetrofitClient.activity).markEmployeeAttendence(markAttendenceRequest), MARK_EMP_ATTENDENCE_REQ_CODE);
    }

    private void initDb() {
        myDb = Room.databaseBuilder(getApplicationContext(), MyDatabase.class, "attendencelogtable")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        attendenceLogDao = myDb.getAttendenceLog();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("ForegroundServiceType")
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel= new NotificationChannel(CHANNEL_ID,CHANNEL_ID, NotificationManager.IMPORTANCE_LOW);
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder notification = new Notification.Builder(this,CHANNEL_ID)
                    .setContentText("Service is runningg")
                    .setContentTitle("DspApp");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                startForeground(101, notification.build(), ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC); // Replace with the correct type
            }else {
                startForeground(101,notification.build());
            }

        }else {
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentText("Service is running")
                    .setContentTitle("DspApp")
                    .setSmallIcon(R.drawable.baseline_notifications_none_24) // Add your icon here
                    .setPriority(NotificationCompat.PRIORITY_LOW) // Equivalent to IMPORTANCE_LOW
                    .build();
            startForeground(101, notification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ForegroundServiceType")

    @Override
    public <T> void onSuccess(Object response, int reqCode) {
        if (reqCode == MARK_EMP_ATTENDENCE_REQ_CODE) {
            try {
                JSONObject jsonObject = new JSONObject(Utillity.convertToString(response));
                Notification notification = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    notification = new Notification.Builder(this, CHANNEL_ID)
                            .setContentTitle("Uploading Attendance")
                            .setContentText("Your attendance data is being uploaded.")
                            .setSmallIcon(R.drawable.baseline_notifications_none_24)
                            .build();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC); // Replace with the correct type

                    }else {
                        startForeground(1,notification);
                    }
                }else {
                    notification = new NotificationCompat.Builder(this)
                            .setContentTitle("Uploading Attendance")
                            .setContentText("Your attendance data is being uploaded.")
                            .setSmallIcon(R.drawable.baseline_notifications_none_24)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .build();
                    startForeground(1, notification);
                }
                Log.d("TAG", "xxx: "+Utillity.getCurrentDateTimeFormatted()+"  "+ empID);
                for (int i = 0; i < listNotSync.size(); i++) {
                    attendenceLogDao.updateAttendanceSync(false,listNotSync.get(i).getDTAttend(),empID);
                }
            } catch (JSONException e) {
                Log.d("TAG", "onFailuree: "+e.getMessage() +" "+e.getLocalizedMessage());
//                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(Throwable t, int reqCode) {
        Log.d("TAG", "onFailure: "+t.getMessage() +" "+t.getLocalizedMessage());
//        Toast.makeText(this, t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
