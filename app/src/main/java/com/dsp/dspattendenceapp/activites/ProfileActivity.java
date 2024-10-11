package com.dsp.dspattendenceapp.activites;



import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.adapters.ServerAdapter;
import com.dsp.dspattendenceapp.databinding.ActivityProfileBinding;
import com.dsp.dspattendenceapp.fragments.ChangePasswordFragment;
import com.dsp.dspattendenceapp.fragments.DspBranchesFragment;
import com.dsp.dspattendenceapp.fragments.DspContactListFragment;
import com.dsp.dspattendenceapp.fragments.GpsMarkedTodayFragment;
import com.dsp.dspattendenceapp.fragments.HomeFragment;
import com.dsp.dspattendenceapp.fragments.MyAttendenceFragment;
import com.dsp.dspattendenceapp.fragments.PamsFragment;
import com.dsp.dspattendenceapp.fragments.ProvidentFundFragment;
import com.dsp.dspattendenceapp.fragments.SalarySlipFragment;
import com.dsp.dspattendenceapp.fragments.SelectOfficeFragment;
import com.dsp.dspattendenceapp.fragments.SettingFragment;
import com.dsp.dspattendenceapp.interfaces.OnServerClick;
import com.dsp.dspattendenceapp.models.ServerInfo;
import com.dsp.dspattendenceapp.models.ServerInfoModel;
import com.dsp.dspattendenceapp.network.ResponseHandler;
import com.dsp.dspattendenceapp.network.RestCaller;
import com.dsp.dspattendenceapp.network.RetrofitClientServer;
import com.dsp.dspattendenceapp.roomdb.dao.AttendenceLogDao;
import com.dsp.dspattendenceapp.roomdb.databases.MyDatabase;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceLogTable;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.services.MyUserDataService;
import com.dsp.dspattendenceapp.services.ServerCheckerr;
import com.dsp.dspattendenceapp.services.UploadUnSyncAttendenceService;
import com.dsp.dspattendenceapp.utills.EncryptionUtils;
import com.dsp.dspattendenceapp.utills.TimeFetcher;
import com.dsp.dspattendenceapp.utills.Utillity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends BaseActivity implements ResponseHandler, OnServerClick {
    private static final int GET_SERVER_DATA_REQ_CODE = 90;
    private ActivityProfileBinding binding;
    private  Fragment selectedFragment;
    private UserTable userTable;
    private  String userName,userLastName,userDesignation,empID,response,OfficeName,OfficeID;
    private ArrayList<ServerInfoModel> list2=new ArrayList<>();
    private List<ServerInfo> list=new ArrayList<>();
    private ServerAdapter adapter;
    private RecyclerView rvserver;
    Dialog dialog;
    Bundle bundle;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        getIntentValues();
        sendValueToFragment();
        changeFragmentOnItemClick();
        initClickListener();

        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull android.view.View drawerView, float slideOffset) {
                binding.maune.setVisibility(View.GONE);
                binding.back.setVisibility(View.VISIBLE);
            }
            @Override
            public void onDrawerOpened(@NonNull android.view.View drawerView) {
                // Code to execute when the drawer is opened
                binding.maune.setVisibility(View.GONE);
                binding.back.setVisibility(View.VISIBLE);
            }
            @Override
            public void onDrawerClosed(@NonNull android.view.View drawerView) {
                // Code to execute when the drawer is closed
                binding.maune.setVisibility(View.VISIBLE);
                binding.back.setVisibility(View.GONE);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Code to respond to drawer state changes
            }
        });

    }
    private void sendValueToFragment() {
        bundle = new Bundle();
        bundle.putString("userName",userName);
        bundle.putString("userDesignation",userDesignation);
        bundle.putString("empID",empID);
        bundle.putString("OfficeID",OfficeID);
        bundle.putString("OfficeName",OfficeName);
        HomeFragment homeFragment = new HomeFragment();
        MyAttendenceFragment myAttendenceFragment = new MyAttendenceFragment();
        myAttendenceFragment.setArguments(bundle);
        homeFragment.setArguments(bundle);
        replaceFragment(homeFragment);
    }


    private void getIntentValues() {
        Intent intent = getIntent();
        if (intent.getExtras() != null){
            Bundle bundle = intent.getExtras();
           userName  =bundle.getString("userName");
           userDesignation  =bundle.getString("userDesignation");
           empID  =bundle.getString("empID");
           response  =bundle.getString("res");
            OfficeID  =bundle.getString("OfficeID");
            OfficeName  =bundle.getString("OfficeName");

//            if (!response.equals("server")){
//                Intent serviceIntent = new Intent(this, MyUserDataService.class);
//                serviceIntent.putExtra("userDataKey", "auto");
//                startService(serviceIntent);
//            }

                Intent serviceIntent = new Intent(this, UploadUnSyncAttendenceService.class);
                serviceIntent.putExtra("empID", empID);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent);
                }else {
                    startService(serviceIntent);
                }
                foregroundServiceRunning();
        }
    }

    public boolean foregroundServiceRunning(){
        ActivityManager manager= (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service: manager.getRunningServices(Integer.MAX_VALUE)){
            if (UploadUnSyncAttendenceService.class.getName().equals(service.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    private void initClickListener() {
        binding.maune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawerOnClick(v);
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawerOnClick();
            }
        });

        binding.ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendValueToFragment();
            }
        });
        binding.iclogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utillity.openLogoutDialog(ProfileActivity.this);
            }
        });

        binding.ivRefrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWaitingDialog();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissWaitingDialog();
                    }
                },800);
                Intent serviceIntent = new Intent(getApplicationContext(), MyUserDataService.class);
                serviceIntent.putExtra("userDataKey", "manual");
                startService(serviceIntent);
            }
        });

        binding.drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(@NonNull android.view.View drawerView) {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
            }

            @Override
            public void onDrawerClosed(@NonNull android.view.View drawerView) {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        });
    }
    private void closeDrawerOnClick() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            binding.maune.setVisibility(View.VISIBLE);
            binding.back.setVisibility(View.GONE);
        }
    }

    private void changeFragmentOnItemClick() {
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home){
                    binding.maune.setVisibility(View.VISIBLE);
                    binding.back.setVisibility(View.GONE);
                    sendValueToFragment();
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setArguments(bundle);
                    selectedFragment = homeFragment;
                }else if (item.getItemId()==R.id.nav_selectoffice){
                    selectedFragment = new SelectOfficeFragment();
                } else if (item.getItemId() == R.id.nav_switch) {
                    if (Utillity.isNetworkAvailable(getApplicationContext())){
                        binding.maune.setVisibility(View.VISIBLE);
                        binding.back.setVisibility(View.GONE);
                        getServerData();
                    }else {
                        binding.maune.setVisibility(View.VISIBLE);
                        binding.back.setVisibility(View.GONE);
                        Utillity.openAlertDialog(ProfileActivity.this,"Please check your internet connection.");
                    }
                } else if (item.getItemId()== R.id.nav_gpsmarked){
                    selectedFragment = new GpsMarkedTodayFragment();
                }else if (item.getItemId()== R.id.nav_dsbbranch){
                    selectedFragment = new DspBranchesFragment();
                }else if (item.getItemId()==R.id.nav_contectlist){
                    selectedFragment = new DspContactListFragment();
                }else if (item.getItemId() == R.id.nav_salaryslip){
                    if (Utillity.isNetworkAvailable(getApplicationContext())){
                        selectedFragment = new SalarySlipFragment();
                    }else {
                        binding.maune.setVisibility(View.VISIBLE);
                        binding.back.setVisibility(View.GONE);
                        Utillity.openAlertDialog(ProfileActivity.this,"Please check your internet connection.");
                    }

                }else if (item.getItemId() == R.id.nav_pams){
                    selectedFragment = new PamsFragment();
                } else if (item.getItemId()==R.id.nav_pfund) {
                    if (Utillity.isNetworkAvailable(getApplicationContext())){
                        selectedFragment = new ProvidentFundFragment();
                    }else {
                        binding.maune.setVisibility(View.VISIBLE);
                        binding.back.setVisibility(View.GONE);
                        Utillity.openAlertDialog(ProfileActivity.this,"Please check your internet connection.");
                    }

                }else if (item.getItemId()==R.id.nav_changepass){
                    selectedFragment = new ChangePasswordFragment();
                } else if (item.getItemId() == R.id.nav_setting) {
                    selectedFragment = new SettingFragment();
                } else if (item.getItemId()==R.id.nav_logout){
                    binding.maune.setVisibility(View.VISIBLE);
                    binding.back.setVisibility(View.GONE);
                    Utillity.openLogoutDialog(ProfileActivity.this);
                }

                if (selectedFragment != null) {
                    replaceFragment(selectedFragment);
                    binding.maune.setVisibility(View.VISIBLE);
                    binding.back.setVisibility(View.GONE);

                }

                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            }
        });
    }



    public void openDrawerOnClick(View view) {
        if (!binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.openDrawer(GravityCompat.START);
            binding.maune.setVisibility(View.GONE);
            binding.back.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // If the navigation drawer is open, close it first
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            binding.maune.setVisibility(View.VISIBLE);
            binding.back.setVisibility(View.GONE);
        } else {
            // If the navigation drawer is not open, perform the default back button action
            super.onBackPressed();
        }
    }

    public  void openServerDialog(Activity activity) {
        dialog = new Dialog(activity);
        dialog.setContentView(R.layout.alert_servers_dialog);
        dialog.setCanceledOnTouchOutside(false);

        ImageView btn_ok = dialog.findViewById(R.id.back);
        rvserver = dialog.findViewById(R.id.rvserver);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dailog);
        }

    }

    private void getServerData() {
        showWaitingDialog();
        new RestCaller(getApplicationContext(), ProfileActivity.this, RetrofitClientServer.getInstance(ProfileActivity.this).getServerData(), GET_SERVER_DATA_REQ_CODE);

    }


    @Override
    public <T> void onSuccess(Object response, int reqCode) {
        if (reqCode==GET_SERVER_DATA_REQ_CODE){
            list.clear();
            JSONObject object = null;
            try {
                object = new JSONObject(Utillity.convertToString(response));
                JSONArray jsonArray = object.getJSONArray("Servers");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject serverObject = jsonArray.getJSONObject(i);
                    String URL_IP= serverObject.getString("URL_IP");
                    String type= serverObject.getString("Type");
                    String name= serverObject.getString("Name");
                    if ("MIS PROD".equals(name) || "S1TEST Test Server".equals(name) || "DEV Dev/Test".equals(name)) {
                        list.add(new ServerInfo(name,URL_IP,type));
                    }


                }
                getListServer();
            } catch (JSONException e) {
                Utillity.openErrorDialog(activity,e.getMessage());
            }
        }
    }
    private void getListServer() {
        ServerCheckerr serverCheckerr= new ServerCheckerr(new ServerCheckerr.ServerCheckListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onServerCheckComplete(Map<ServerInfo, Boolean> serverStatusMap) {
                list2.clear();
                for (Map.Entry<ServerInfo, Boolean> entry : serverStatusMap.entrySet()) {
                    ServerInfo serverInfo = entry.getKey();
                    boolean status = entry.getValue();
                    if (serverInfo.getType().equals("ICMP/PING")){
                        list2.add(new ServerInfoModel(serverInfo.getIp(),serverInfo.getHostname(),status,Utillity.getCurrentDate(),serverInfo.getName(),serverInfo.getType()));
                    }else {
                        String protocol= Utillity.checkProtocol(serverInfo.getUrl());

                        list2.add(new ServerInfoModel(serverInfo.getIp(),"("+protocol+")"+" "+serverInfo.getHostname(),status,Utillity.getCurrentDate(),serverInfo.getName(),serverInfo.getType()));
                    }
                }
                if (list2 !=null && !list2.isEmpty() && list2.size() >0){
                    dismissWaitingDialog();
                    openServerDialog(activity);
                    rvserver.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    adapter=new ServerAdapter(list2,getApplicationContext(),ProfileActivity.this::onClick);
                    rvserver.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }else {
                    dismissWaitingDialog();
                }
            }
        });
        serverCheckerr.execute(list);

    }

    @Override
    public void onFailure(Throwable t, int reqCode) {
        dismissWaitingDialog();
        Utillity.openErrorDialog(activity,t.getMessage());
    }

    @Override
    public void onClick(ServerInfoModel serverInfoModel) {
        dialog.dismiss();

    }
}
