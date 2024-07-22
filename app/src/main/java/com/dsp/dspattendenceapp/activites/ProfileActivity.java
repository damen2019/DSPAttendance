package com.dsp.dspattendenceapp.activites;



import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dsp.dspattendenceapp.R;
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
import com.dsp.dspattendenceapp.models.AddDeviceIdRequest;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.services.MyUserDataService;
import com.dsp.dspattendenceapp.utills.Utillity;
import com.google.android.material.navigation.NavigationView;

public class ProfileActivity extends BaseActivity {
    private ActivityProfileBinding binding;
    private  Fragment selectedFragment;
    private UserTable userTable;
    private  String userName,userLastName,userDesignation,empID,response,OfficeName,OfficeID;

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
        addDeviceID();



    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addDeviceID() {
        AddDeviceIdRequest addDeviceIdRequest=new AddDeviceIdRequest(getIEMI(),"","","","")
    }

    private void sendValueToFragment() {
        Bundle bundle = new Bundle();
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

            if (!response.equals("server")){
                Intent serviceIntent = new Intent(this, MyUserDataService.class);
                serviceIntent.putExtra("userDataKey", "auto");
                startService(serviceIntent);
            }
        }
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
                Intent serviceIntent = new Intent(getApplicationContext(), MyUserDataService.class);
                serviceIntent.putExtra("userDataKey", "manual");
                startService(serviceIntent);
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
                    sendValueToFragment();
                }else if (item.getItemId()==R.id.nav_selectoffice){
                    selectedFragment = new SelectOfficeFragment();
                }else if (item.getItemId()== R.id.nav_gpsmarked){
                    selectedFragment = new GpsMarkedTodayFragment();
                }else if (item.getItemId()== R.id.nav_dsbbranch){
                    selectedFragment = new DspBranchesFragment();
                }else if (item.getItemId()==R.id.nav_contectlist){
                    selectedFragment = new DspContactListFragment();
                }else if (item.getItemId() == R.id.nav_salaryslip){
                    selectedFragment = new SalarySlipFragment();
                }else if (item.getItemId() == R.id.nav_pams){
                    selectedFragment = new PamsFragment();
                } else if (item.getItemId()==R.id.nav_pfund) {
                    selectedFragment = new ProvidentFundFragment();
                }else if (item.getItemId()==R.id.nav_changepass){
                    selectedFragment = new ChangePasswordFragment();
                }
                else if (item.getItemId()==R.id.nav_logout){
                    binding.maune.setVisibility(View.VISIBLE);
                    binding.back.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, "Logout", Toast.LENGTH_SHORT).show();
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

}
