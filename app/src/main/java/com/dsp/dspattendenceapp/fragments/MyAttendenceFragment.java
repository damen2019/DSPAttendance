package com.dsp.dspattendenceapp.fragments;


import static androidx.core.content.PermissionChecker.checkSelfPermission;


import static com.dsp.dspattendenceapp.utills.Utillity.getAddressFromLatLng;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.adapters.AttendanceRecordAdapter;
import com.dsp.dspattendenceapp.adapters.Selected_page_adapter;
import com.dsp.dspattendenceapp.databinding.FragmentMyAttendenceBinding;
import com.dsp.dspattendenceapp.interfaces.OnFetchDailyecord;
import com.dsp.dspattendenceapp.interfaces.OnOptionClickListener;
import com.dsp.dspattendenceapp.roomdb.dao.AttendenceDao;
import com.dsp.dspattendenceapp.roomdb.databases.MyDatabase;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;
import com.dsp.dspattendenceapp.utills.Preferences;
import com.dsp.dspattendenceapp.utills.Utillity;

import java.util.ArrayList;
import java.util.List;

public class MyAttendenceFragment extends Fragment implements LocationListener {
    private FragmentMyAttendenceBinding binding;
    private List<AttendenceTable> list=new ArrayList<>();
    private AttendanceRecordAdapter adapter;

    private LocationManager locationManager;
    private double radiusvalue;
    final int REQUEST_CODE = 101;
    private String imei = "";
    private double latitude=0.0;
    private double longitude=0.0;
    MyDatabase myDb;
    AttendenceDao attendenceDao;
    private String userName,lastName,desigination,empID,locationval,status,OfficeID,OfficeName;
    private OnFetchDailyecord onFetchDailyecord;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyAttendenceBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        initDb();
        getIEMI();
        getBundleValue();
        setUpViewPager();
        startGettingLocationUpdates(Utillity.isNetworkConnected(getContext()));
        initClickListener();
        initUi();
        getAttendenceRecord();
        return view;
    }
    private void setUpViewPager() {
        setUpViewPages(binding.myViewPager);
        binding.myViewPager.getCurrentItem();
        binding.tabLayout.setupWithViewPager(binding.myViewPager);
    }

    private void setUpViewPages(ViewPager viewPager) {

        Selected_page_adapter selected_page_adapter = new Selected_page_adapter(getChildFragmentManager());

        selected_page_adapter.addFragment(new DailyAttendenceReportFragment(empID), "Daily Record");
        selected_page_adapter.addFragment(new MonthlyAttendenceReportFragment(empID), "Monthly Record");
        viewPager.setAdapter(selected_page_adapter);

    }
    private void getBundleValue() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            desigination= bundle.getString("userDesignation");
            userName= bundle.getString("userName");
            empID= bundle.getString("empID");
            OfficeID  =bundle.getString("OfficeID");
            OfficeName  =bundle.getString("OfficeName");
            binding.tvdoffice.setText("Detect Office: "+OfficeID +":"+ OfficeName);
            binding.tvname.setText(userName+" | "+desigination);

        }
    }
    private void initDb() {
        myDb= Room.databaseBuilder(getContext(),MyDatabase.class,"attendencetable")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        attendenceDao=myDb.getAttendenceOfEmp();
    }

    private void initUi() {
        binding.tvdate.setText(Utillity.getCurrentDate());
        if (Preferences.getSharedPrefValue(getContext(),"status") != null && !Preferences.getSharedPrefValue(getContext(),"status").isEmpty()){
            if (Preferences.getSharedPrefValue(getContext(),"status").equals("clockin")){
                binding.clockin.setVisibility(View.GONE);
                binding.clockingray.setVisibility(View.VISIBLE);
            } else if (Preferences.getSharedPrefValue(getContext(),"status").equals("clockout")) {
                binding.clockin.setVisibility(View.VISIBLE);
                binding.clockoutgray.setVisibility(View.VISIBLE);
            }
        }

    }
    private void initClickListener() {
        binding.clockin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radiusvalue < 15.0 && !Utillity.isVpnActive(getContext())){
                    binding.clockin.setVisibility(View.GONE);
                    binding.clockingray.setVisibility(View.VISIBLE);
                    binding.clockoutgray.setVisibility(View.GONE);
                    binding.clockout.setVisibility(View.VISIBLE);
                    status="clockin";
                    Preferences.saveSharedPrefValue(getContext(),"status",status);
                    insertAttendenceData();
                }else {
                    Toast.makeText(getContext(), "Please check your distance or remove vpn connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.clockout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radiusvalue < 15.0 && !Utillity.isVpnActive(getContext())){
                    binding.clockout.setVisibility(View.GONE);
                    binding.clockoutgray.setVisibility(View.VISIBLE);
                    binding.clockin.setVisibility(View.VISIBLE);
                    binding.clockingray.setVisibility(View.GONE);
                    status="clockout";
                    Preferences.saveSharedPrefValue(getContext(),"status",status);
                    updateAttendenceData();
                }else {
                    Toast.makeText(getContext(), "Please check your distance or remove vpn connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void updateAttendenceData() {
        attendenceDao.updateAttendance(Utillity.getCurrentTime(),status,empID);
        getAttendenceRecord();
//        setUpViewPager();
    }

    private void insertAttendenceData() {
        AttendenceTable attendenceTable=new AttendenceTable(empID,userName,locationval,imei,Utillity.getCurrentDateWithOutDay(),Utillity.getCurrentTime(),"",status);
        long insertedRowId= attendenceDao.insertAttendence(attendenceTable);
        if (insertedRowId != -1) {
            getAttendenceRecord();
        }
    }

    private void getAttendenceRecord() {
        list.clear();
       list= attendenceDao.getAllAttendances(empID);
       if (list !=null && !list.isEmpty() && list.size() >0){
           setUpViewPager();
           binding.tvrecord.setVisibility(View.VISIBLE);
           binding.tabLayout.setVisibility(View.VISIBLE);
           binding.myViewPager.setVisibility(View.VISIBLE);

       }

    }

    public void startGettingLocationUpdates(boolean connect) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {
            if (connect){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

            }else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        binding.clockin.setClickable(true);
        binding.clockout.setClickable(true);
        Log.d("TAG", "onLocationChanged: "+latitude+" "+longitude);
        float radius= Utillity.calculateDistanceInMeters(latitude,longitude,31.4547529,74.2464942);
//        showRadius(Utillity.formatDecimal(radius));
        radiusvalue= Double.parseDouble(Utillity.formatDecimal(radius));
        binding.tvdistance.setText("Distance: "+radiusvalue +" Maters");
        if (getContext() != null){
            locationval= getAddressFromLatLng(getContext(),latitude,longitude);
        }

    }


    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getIEMI() {
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(getContext(),Manifest.permission.READ_PHONE_STATE) == PermissionChecker.PERMISSION_GRANTED) {
                if (telephonyManager != null) {
                    try {
                        imei = telephonyManager.getImei();
                    } catch (Exception e) {
                        e.printStackTrace();
                        imei = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                    }
                }
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
            }
        } else {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (telephonyManager != null) {
                    imei = telephonyManager.getDeviceId();
                }
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission granted.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}