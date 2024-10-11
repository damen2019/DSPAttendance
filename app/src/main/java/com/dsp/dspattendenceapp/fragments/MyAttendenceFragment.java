package com.dsp.dspattendenceapp.fragments;


import static com.dsp.dspattendenceapp.network.RetrofitClient.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.adapters.AttendanceRecordAdapter;
import com.dsp.dspattendenceapp.adapters.Selected_page_adapter;
import com.dsp.dspattendenceapp.databinding.FragmentMyAttendenceBinding;
import com.dsp.dspattendenceapp.interfaces.OnFetchDailyecord;
import com.dsp.dspattendenceapp.models.MarkAttendenceRequest;
import com.dsp.dspattendenceapp.network.ResponseHandler;
import com.dsp.dspattendenceapp.network.RestCaller;
import com.dsp.dspattendenceapp.network.RetrofitClient;
import com.dsp.dspattendenceapp.roomdb.dao.AttendenceDao;
import com.dsp.dspattendenceapp.roomdb.dao.AttendenceLogDao;
import com.dsp.dspattendenceapp.roomdb.databases.MyDatabase;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceLogTable;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;
import com.dsp.dspattendenceapp.roomdb.table.OfficesData;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.services.InsertDataTask;
import com.dsp.dspattendenceapp.utills.Preferences;
import com.dsp.dspattendenceapp.utills.TimeFetcher;
import com.dsp.dspattendenceapp.utills.Utillity;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyAttendenceFragment extends BaseFragment implements ResponseHandler {
    private static final int GET_EMP_ATTENDENCE_REQ_CODE = 05;
    private static final int MARK_EMP_ATTENDENCE_REQ_CODE = 06;
    private static final int GET_EMP_OFFICES_REQ_CODE = 87;
    private FragmentMyAttendenceBinding binding;
    private List<AttendenceTable> list = new ArrayList<>();
    private List<OfficesData> listOffices = new ArrayList<>();
    private List<AttendenceLogTable> listlogs = new ArrayList<>();
    private AttendanceRecordAdapter adapter;
    private AttendenceTable attendenceTable;
    private LocationManager locationManager;
    private double radiusvalue;
    final int REQUEST_CODE = 101;
    private String imei = "";
    private double latitude = 0.0;
    private double longitude = 0.0;
    private List<AttendenceLogTable> listNotSync = new ArrayList<>();
    private String latlng = "";
    private MyDatabase myDb, myDb2;
    private AttendenceDao attendenceDao;
    private AttendenceLogDao attendenceLogDao;
    private boolean mockLocation;
    private String userName, desigination, empID, status, OfficeID, OfficeName, latdefult, lngdefult,latdefult1, lngdefult1;
    private OnFetchDailyecord onFetchDailyecord;
    private ScheduledExecutorService scheduler;
    private UserTable userTable;
    private FusedLocationProviderClient mfused_location;
    private Task locationtask;
    private String timeDateClick;
    private TimeFetcher timeFetcher;
    private Handler handler;
    private Runnable runnable;
    double nearestDistance = Double.MAX_VALUE;
    OfficesData nearestOffice = null;

    public MyAttendenceFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyAttendenceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        getDateAndTime();
        initDb();
        getBundleValue();
        setUpViewPager();
        initClickListener();
        initUi();
        getAttendenceRecord();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getDateAndTime() {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Utillity.clearPreferencesIfDateChanged(getContext());
        timeFetcher = new TimeFetcher();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                timeFetcher.getNetworkTime(new TimeFetcher.OnTimeFetchedListener() {
                    @Override
                    public void onTimeFetched(long networkTime) {
                        timeDateClick = Utillity.formatTimee(networkTime);
                    }
                });
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);

    }

    private void checkNeerOffice() {
        listOffices = attendenceLogDao.getAllOffices();
        Log.d("TAG", "checkNeerOffice: "+ listOffices.size());
        if (listOffices != null && listOffices.size() > 0 ){
            Log.d("TAG", "checkNeerOffice: "+" Local");
            for (int i = 0; i < listOffices.size(); i++) {
                String[] parts = listOffices.get(i).getLatLon().split(",");
                latdefult1= parts[0];
                lngdefult1 = parts[1];
                float radius= Utillity.calculateDistanceInMeters(latitude,longitude,Double.parseDouble(latdefult1), Double.parseDouble(lngdefult1));
                radiusvalue= Double.parseDouble(Utillity.formatDecimal(radius));
                Log.d("TAG", "radiusvalue: "+radiusvalue + " "+ listOffices.get(i).getOfficeName());

                if (radiusvalue < 50.0 && radiusvalue < nearestDistance) {
                    nearestDistance = radiusvalue;  // Update nearest distance
                    nearestOffice = listOffices.get(i);  // Update nearest office
                }
            }
            if (nearestOffice != null) {
                binding.tvdistance.setText("Distance: " + nearestDistance + " Meters");
                binding.tvdoffice.setText(nearestOffice.getOfficeID() + ": " + nearestOffice.getOfficeName());

            } else {
                Log.d("TAG", "No offices within 50 meters.");
                binding.tvdoffice.setTextSize(12);
                binding.tvdoffice.setText("No offices within 50 meters. Please go home page or restart the app.");
            }
        }else {
            showWaitingDialog();
            new RestCaller(getContext(), this, RetrofitClient.getInstance(getActivity()).getEmpOffices(empID, Preferences.getSharedPrefValue(getContext(),"token")), GET_EMP_OFFICES_REQ_CODE);

        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        forGettingLocation();

    }

    private void forGettingLocation() {
        mfused_location = LocationServices.getFusedLocationProviderClient(requireContext());
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationtask= mfused_location.getLastLocation();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            createLocationRequest();
        }
    }

    private void createLocationRequest() {
        Utillity.request_location(getActivity());
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


        task.addOnSuccessListener(getActivity(), locationSettingsResponse -> {
            //This means that the GPS was already opened
            get_location();
        });

        task.addOnFailureListener(getActivity(), e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(activity,
                            101);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }

    private void get_location() {
        locationtask.addOnCompleteListener(task -> {
            if (task.getResult() != null) {
                Location MY_LOCATION = (Location) task.getResult();
                latitude = MY_LOCATION.getLatitude();
                longitude = MY_LOCATION.getLongitude();
                binding.clockin.setClickable(true);
                binding.clockout.setClickable(true);
                latlng= latitude+","+longitude;
                checkNeerOffice();
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        get_location();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            } else {
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        get_location();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
            }
        });
    }

    private void disableButtonOnWeekEnd() {
        if (Utillity.getCurrentDayOfWeek() == Calendar.SATURDAY || Utillity.getCurrentDayOfWeek() == Calendar.SUNDAY){
            binding.clockin.setVisibility(View.GONE);
            binding.clockingray.setVisibility(View.VISIBLE);
            binding.clockout.setVisibility(View.GONE);
            binding.clockoutgray.setVisibility(View.VISIBLE);
        }
    }

    private void setUpViewPager() {
        setUpViewPages(binding.myViewPager);
        binding.myViewPager.getCurrentItem();
        binding.tabLayout.setupWithViewPager(binding.myViewPager);
    }

    private void setUpViewPages(ViewPager viewPager) {

        Selected_page_adapter selected_page_adapter = new Selected_page_adapter(getChildFragmentManager());

        selected_page_adapter.addFragment(new DailyAttendenceReportFragment(), "Daily Record");
        selected_page_adapter.addFragment(new MonthlyAttendenceReportFragment(), "Monthly Record");
        viewPager.setAdapter(selected_page_adapter);

    }
    private void getBundleValue() {
        Utillity.clearAttLogIfNewMonth(getContext(),attendenceLogDao);
        userTable= Preferences.getUserDetails(getContext());
        String[] parts = userTable.getLatLon().split(",");
        latdefult= parts[0]; // 004
        lngdefult = parts[1];
        desigination= userTable.getDesigName();
        userName= userTable.getFullName();
        empID= userTable.getEmpID();
        OfficeID = String.valueOf(userTable.getOfficeID());
        OfficeName = userTable.getOfficeName();
        binding.tvname.setText(userName+" | "+desigination);
        Log.d("TAG", "getBundleValue: "+userTable.getOfficeID()+" "+userTable.getOfcID());
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            desigination= bundle.getString("userDesignation");
//            userName= bundle.getString("userName");
//            empID= bundle.getString("empID");
//            OfficeID  =bundle.getString("OfficeID");
//            OfficeName  =bundle.getString("OfficeName");
////            binding.tvdoffice.setText(OfficeID +":"+ OfficeName);
//            binding.tvname.setText(userName+" | "+desigination);
//
//        }
    }
    private void initDb() {
        myDb= Room.databaseBuilder(getContext(),MyDatabase.class,"attendencetable")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        attendenceDao=myDb.getAttendenceOfEmp();

        myDb2= Room.databaseBuilder(getContext(), MyDatabase.class,"attendencelogtable")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        attendenceLogDao=myDb2.getAttendenceLog();
    }
    private void initUi() {
        binding.tvdate.setText(Utillity.getCurrentDate());
//        disableButtonAfter12();
        disableButtonOnWeekEnd();
        if (Preferences.getSharedPrefValue(getContext(),"status") != null && !Preferences.getSharedPrefValue(getContext(),"status").isEmpty()){
            if (Preferences.getSharedPrefValue(getContext(),"status").equals("clockin")){
                binding.clockin.setVisibility(View.GONE);
                binding.clockingray.setVisibility(View.VISIBLE);
                binding.clockingray.setClickable(false);
                binding.clockingray.setFocusable(false);
            } else if (Preferences.getSharedPrefValue(getContext(),"status").equals("clockout")) {
                binding.clockin.setVisibility(View.VISIBLE);
                binding.clockoutgray.setVisibility(View.VISIBLE);
                binding.clockoutgray.setClickable(false);
                binding.clockoutgray.setFocusable(false);
            }
        }
    }

    private void disableButtonAfter12() {
        if (Utillity.getCurrentHour() >= 12 && !Preferences.getSharedPrefValue(getContext(),"status").equals("clockin")){
            binding.clockin.setVisibility(View.GONE);
            binding.clockingray.setVisibility(View.VISIBLE);
            binding.clockout.setVisibility(View.GONE);
            binding.clockoutgray.setVisibility(View.VISIBLE);
        }

    }

    private void initClickListener() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        binding.clockin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (!Utillity.isNetworkAvailable(getContext())){
                    if (nearestDistance < 50.0 && nearestDistance !=0.0){
                        Preferences.saveSharedPrefValue(getContext(),"datevalue",Utillity.getCurrentDateFormatted());
                        if (timeDateClick != null && !timeDateClick.isEmpty()){
                            binding.clockin.setVisibility(View.GONE);
                            binding.clockingray.setVisibility(View.VISIBLE);
                            binding.clockingray.setClickable(false);
                            binding.clockingray.setFocusable(false);
                            binding.clockoutgray.setVisibility(View.GONE);
                            binding.clockout.setVisibility(View.VISIBLE);
                            status="clockin";
                            attendenceLogDao.insertAttendenceLog(new AttendenceLogTable("IN",timeDateClick,empID,OfficeID,latlng,true));
                            Preferences.saveSharedPrefValue(getContext(),"status",status);
                            getAttendenceRecord();
                            openAlertDialog(getString(R.string.your_attendance_has_been_successfully_marked));
//                            if (!Utillity.isNetworkAvailable(getContext())){
//                                Preferences.saveSharedPrefValue(getContext(),"status",status);
//                                getAttendenceRecord();
//                            }
//                            else {
//                                listNotSync = attendenceLogDao.getAllAttendanceLogs(empID,true);
//                                if (listNotSync != null && listNotSync.size() > 0) {
//                                    Gson gson = new Gson();
//                                    String jsonString = gson.toJson(listNotSync);
//                                    Log.d("TAG", "onClick: "+jsonString);
//                                    markAttendence(jsonString);
//                                }
//                            }
                        }else {
                            Utillity.openAlertDialog(getActivity(),getString(R.string.something_went_wrong_please_close_the_app_and_open_again_thank));
                        }
                    }else {
                        Utillity.openAlertDialog(getActivity(),getString(R.string.please_check_your_distance_and_try_again_thanks));
                    }

                }else {
                    if (!Utillity.isVpnActive(getContext()) && !Utillity.isLocationFromMockProvider(location)){
                        if (nearestDistance < 50.0 && nearestDistance !=0.0){
                            Preferences.saveSharedPrefValue(getContext(),"datevalue",Utillity.getCurrentDateFormatted());
                            if (timeDateClick != null && !timeDateClick.isEmpty()){
                                binding.clockin.setVisibility(View.GONE);
                                binding.clockingray.setVisibility(View.VISIBLE);
                                binding.clockingray.setClickable(false);
                                binding.clockingray.setFocusable(false);
                                binding.clockoutgray.setVisibility(View.GONE);
                                binding.clockout.setVisibility(View.VISIBLE);
                                status="clockin";
                                attendenceLogDao.insertAttendenceLog(new AttendenceLogTable("IN",timeDateClick,empID,OfficeID,latlng,true));
                                Preferences.saveSharedPrefValue(getContext(),"status",status);
                                getAttendenceRecord();
                                openAlertDialog(getString(R.string.your_attendance_has_been_successfully_marked));
//                                if (!Utillity.isNetworkAvailable(getContext())){
//                                    Preferences.saveSharedPrefValue(getContext(),"status",status);
//                                    getAttendenceRecord();
//                                }
//                                else {
//                                    Log.d("TAG", "onClick: "+"noline");
//                                    listNotSync = attendenceLogDao.getAllAttendanceLogs(empID,true);
//                                    if (listNotSync != null && listNotSync.size() > 0) {
//                                        Gson gson = new Gson();
//                                        String jsonString = gson.toJson(listNotSync);
//                                        Log.d("TAG", "onClick: "+jsonString);
//                                        markAttendence(jsonString);
//                                    }
//                                }
                            }else {
                                Utillity.openAlertDialog(getActivity(),getString(R.string.something_went_wrong_please_close_the_app_and_open_again_thank));
                            }
                        }else {
                            Utillity.openAlertDialog(getActivity(),getString(R.string.please_check_your_distance_and_try_again_thanks));
                        }
                    }else {
                        Utillity.openAlertDialog(getActivity(),getString(R.string.please_remove_vpn_connection_and_fake_gps));
                    }
                }

            }
        });
        binding.clockout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (!Utillity.isNetworkAvailable(getContext())){
                    if (nearestDistance < 50.0 && nearestDistance !=0.0) {
                        if (timeDateClick != null && !timeDateClick.isEmpty()){
                            binding.clockout.setVisibility(View.GONE);
                            binding.clockoutgray.setVisibility(View.VISIBLE);
                            binding.clockoutgray.setClickable(false);
                            binding.clockoutgray.setFocusable(false);
                            binding.clockin.setVisibility(View.VISIBLE);
                            binding.clockingray.setVisibility(View.GONE);
                            status = "clockout";
                            attendenceLogDao.insertAttendenceLog(new AttendenceLogTable("OUT", timeDateClick, empID, OfficeID, latlng, true));
                            Preferences.saveSharedPrefValue(getContext(), "status", status);
                            updateAttendenceData();
                            openAlertDialog(getString(R.string.your_attendance_has_been_successfully_marked));
//                            if (!Utillity.isNetworkAvailable(getContext())) {
//                                Preferences.saveSharedPrefValue(getContext(), "status", status);
//                                updateAttendenceData();
//                            } else {
//                                listNotSync = attendenceLogDao.getAllAttendanceLogs(empID, true);
//                                if (listNotSync != null && listNotSync.size() > 0) {
//                                    Gson gson = new Gson();
//                                    String jsonString = gson.toJson(listNotSync);
//                                    Log.d("TAG", "onClick: "+jsonString);
//                                    markAttendence(jsonString);
//                                }
//                            }
                        }else {
                            Utillity.openAlertDialog(getActivity(),getString(R.string.something_went_wrong_please_close_the_app_and_open_again_thank));
                        }
                    }else {
                        Utillity.openAlertDialog(getActivity(),getString(R.string.please_check_your_distance_and_try_again_thanks));
                    }

                }else {
                    if (!Utillity.isVpnActive(getContext()) && !Utillity.isLocationFromMockProvider(location)){
                       if (nearestDistance < 50.0 && nearestDistance !=0.0) {
                           if (timeDateClick != null && !timeDateClick.isEmpty()){
                               binding.clockout.setVisibility(View.GONE);
                               binding.clockoutgray.setVisibility(View.VISIBLE);
                               binding.clockoutgray.setClickable(false);
                               binding.clockoutgray.setFocusable(false);
                               binding.clockin.setVisibility(View.VISIBLE);
                               binding.clockingray.setVisibility(View.GONE);
                               status = "clockout";
                               attendenceLogDao.insertAttendenceLog(new AttendenceLogTable("OUT", timeDateClick, empID, OfficeID, latlng, true));
                               Preferences.saveSharedPrefValue(getContext(), "status", status);
                               updateAttendenceData();
                               openAlertDialog(getString(R.string.your_attendance_has_been_successfully_marked));

//                               if (!Utillity.isNetworkAvailable(getContext())) {
//                                   Preferences.saveSharedPrefValue(getContext(), "status", status);
//                                   updateAttendenceData();
//                               } else {
//                                   listNotSync = attendenceLogDao.getAllAttendanceLogs(empID, true);
//                                   if (listNotSync != null && listNotSync.size() > 0) {
//                                       Gson gson = new Gson();
//                                       String jsonString = gson.toJson(listNotSync);
//                                       Log.d("TAG", "onClick: "+jsonString);
//                                       markAttendence(jsonString);
//                                   }
//                               }
                           }else {
                               Utillity.openAlertDialog(getActivity(),getString(R.string.something_went_wrong_please_close_the_app_and_open_again_thank));
                           }
                       }else {
                           Utillity.openAlertDialog(getActivity(),getString(R.string.please_check_your_distance_and_try_again_thanks));
                       }
                    }else {
                        Utillity.openAlertDialog(getActivity(),getString(R.string.please_remove_vpn_connection_and_fake_gps));
                    }

                }

            }
        });
        binding.clockingray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.clockoutgray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void markAttendence(String jsonString) {
        showWaitingDialog();
        MarkAttendenceRequest markAttendenceRequest = new MarkAttendenceRequest(this.empID, Preferences.getSharedPrefValue(getContext(),"token"),jsonString);
        new RestCaller(getContext(), this, RetrofitClient.getInstance(getActivity()).markEmployeeAttendence(markAttendenceRequest), MARK_EMP_ATTENDENCE_REQ_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateAttendenceData() {
        getAttendenceRecord();
    }
    private void getAttendenceRecord() {
        setUpViewPager();
        binding.tvrecord.setVisibility(View.VISIBLE);
        binding.tabLayout.setVisibility(View.VISIBLE);
        binding.myViewPager.setVisibility(View.VISIBLE);
    }
    @Override
    public <T> void onSuccess(Object response, int reqCode) {
        if (reqCode == MARK_EMP_ATTENDENCE_REQ_CODE){
            dismissWaitingDialog();
            try {
                JSONObject jsonObject=new JSONObject(Utillity.convertToString(response));
                Preferences.saveSharedPrefValue(getContext(),"status",status);
                getAttendenceRecord();
            } catch (JSONException e) {
                Utillity.openErrorDialog(getActivity(),e.getMessage());
            }
        }
        else if (reqCode == GET_EMP_OFFICES_REQ_CODE){
            dismissWaitingDialog();
            listOffices.clear();
            try {
                JSONArray jsonArray=new JSONArray(Utillity.convertToString(response));
                Gson gson = new Gson();
                Type type = new TypeToken<List<OfficesData>>() {
                }.getType();
                listOffices = gson.fromJson(jsonArray.toString(), type);
                for (int i = 0; i < listOffices.size(); i++) {
                    attendenceLogDao.insertOffices(new OfficesData(listOffices.get(i).getOfcID(),listOffices.get(i).getParentOfcID(),listOffices.get(i).getOfficeID(),listOffices.get(i).getOfficeName(),listOffices.get(i).getOfficeOrder(),listOffices.get(i).getIsActive(),listOffices.get(i).getLatLon()));
                    String[] parts = listOffices.get(i).getLatLon().split(",");
                    latdefult1= parts[0];
                    lngdefult1 = parts[1];
                    float radius= Utillity.calculateDistanceInMeters(latitude,longitude,Double.parseDouble(latdefult1), Double.parseDouble(lngdefult1));
                    radiusvalue= Double.parseDouble(Utillity.formatDecimal(radius));
                    if (radiusvalue < 50.0 && radiusvalue < nearestDistance) {
                        nearestDistance = radiusvalue;  // Update nearest distance
                        nearestOffice = listOffices.get(i);  // Update nearest office
                    }
                }
                if (nearestOffice != null) {
                    binding.tvdistance.setText("Distance: " + nearestDistance + " Meters");
                   // binding.tvdoffice.setText(nearestOffice.getOfficeID() + ": " + nearestOffice.getOfficeName());
                } else {
                    Log.d("TAG", "No offices within 50 meters.");
                    binding.tvdoffice.setTextSize(12);
                    binding.tvdoffice.setText("No offices within 50 meters. Please go home page or restart the app.");

                }
            } catch (JSONException e) {
//                Utillity.openErrorDialog(getActivity(),e.getMessage());
            }
        }
    }
    @Override
    public void onFailure(Throwable t, int reqCode) {
        dismissWaitingDialog();
        Log.d("TAG", "onFailure4: "+t.getMessage());
//        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
//        Utillity.openErrorDialog(getActivity(),t.getMessage());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        forGettingLocation();
    }

    private void openAlertDialog(String message) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.alert_success_dialog);
        dialog.setCanceledOnTouchOutside(false);

        TextView btn_ok = dialog.findViewById(R.id.yes);
        TextView tconfirm = dialog.findViewById(R.id.tconfirm);
        tconfirm.setText(message);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                UserTable userTable= Preferences.getUserDetails(getContext());
                Bundle bundle;
                bundle = new Bundle();
                bundle.putString("userName",userTable.getFullName());
                bundle.putString("userDesignation",userTable.getDesigName());
                bundle.putString("empID",userTable.getEmpID());
                bundle.putString("OfficeID",String.valueOf(userTable.getOfficeID()));
                bundle.putString("OfficeName",userTable.getOfficeName());
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);
                replaceFragment1(homeFragment);
            }
        });
        if (!getActivity().isFinishing() && !getActivity().isDestroyed()) {
            dialog.show();
        }

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dailog);
        }

    }
}