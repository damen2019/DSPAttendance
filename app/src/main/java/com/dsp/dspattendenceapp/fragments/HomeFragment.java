package com.dsp.dspattendenceapp.fragments;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.activites.MainActivity;
import com.dsp.dspattendenceapp.activites.ProfileActivity;
import com.dsp.dspattendenceapp.activites.WebViewActivity;
import com.dsp.dspattendenceapp.adapters.MainOptionAdapter;
import com.dsp.dspattendenceapp.databinding.FragmentHomeBinding;
import com.dsp.dspattendenceapp.global.Constants;
import com.dsp.dspattendenceapp.interfaces.OnOptionClickListener;
import com.dsp.dspattendenceapp.models.MainOptionModel;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.utills.EncryptionUtils;
import com.dsp.dspattendenceapp.utills.Preferences;
import com.dsp.dspattendenceapp.utills.Utillity;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment implements OnOptionClickListener {
    private FragmentHomeBinding binding;
    private ArrayList<MainOptionModel> list=new ArrayList<>();
    private MainOptionAdapter adapter;
    private NavController navController;
    String encrKey;
    private String userName,userdetailstr, userDesignation,empID,OfficeID,OfficeName;


    public HomeFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        getBundleValue();

        valueOptions();



        return view;
    }


    private void sendValueToFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("userName",userName);
        bundle.putString("userDesignation",userDesignation);
        bundle.putString("empID",empID);
        bundle.putString("OfficeID",OfficeID);
        bundle.putString("OfficeName",OfficeName);


        MyAttendenceFragment myAttendenceFragment = new MyAttendenceFragment();
        myAttendenceFragment.setArguments(bundle);

        replaceFragment(myAttendenceFragment);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getBundleValue() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            userName = bundle.getString("userName");
            userDesignation = bundle.getString("userDesignation");
            empID = bundle.getString("empID");
            OfficeID  =bundle.getString("OfficeID");
            OfficeName  =bundle.getString("OfficeName");
            binding.tvname.setText(userName);
            binding.designation.setText(userDesignation);
            binding.officeid.setText(OfficeID +":"+ OfficeName);
            binding.empid.setText(empID);
            binding.tvinint.setText(Utillity.getInitials(userName));
            getMISKEYDATA();
        }
    }

    private void getMISKEYDATA() {


        try {
            UserTable userTable= Preferences.getUserDetails(getContext());
            String passwordMd5= Utillity.calculateMD5Hash(userTable.getUserPass());
            String empidd=  userTable.getEmpID();
            userdetailstr=empidd+"|"+userTable.getUserPass();
            encrKey = Utillity.encryptUrlParams(userTable.getEmpID(),userTable.getUserPass(),Utillity.getNextDayDate());
            Log.d("TAG", "onKEY: "+encrKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void valueOptions() {
        list.add(new MainOptionModel(getString(R.string.my_attendence),"1"));
        list.add(new MainOptionModel(getString(R.string.mis),"2"));
        list.add(new MainOptionModel(getString(R.string.my_salary_slip),"3"));
//        list.add(new MainOptionModel(getString(R.string.my_pams),"4"));
        list.add(new MainOptionModel(getString(R.string.my_provident_fund_net_balance),"4"));

        binding.rvoption.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter= new MainOptionAdapter(getContext(),list,this);
        binding.rvoption.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nullify the binding object to avoid memory leaks
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(String id) {
        if (id.equals("1")){
            binding.li1.setVisibility(View.GONE);
            replaceFragment(new MyAttendenceFragment());
            //sendValueToFragment();
        } else if (id.equals("2")) {
            if (!Utillity.isNetworkAvailable(getContext())){
                Utillity.openAlertDialog(getActivity(),"Please check your internet connection.");
            }else {
                if (!encrKey.isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mis.damensp.com/MIS/Login.aspx?k="+encrKey));
                    startActivity(intent);
                }
            }
        } else if (id.equals("3")) {
            if (Utillity.isNetworkAvailable(getContext())){
                binding.li1.setVisibility(View.GONE);
                replaceFragment(new SalarySlipFragment());
            }else {
                Utillity.openAlertDialog(getActivity(),getString(R.string.please_check_your_internet_connectivity));
            }

        }
//        else if (id.equals("4")) {
//            binding.li1.setVisibility(View.GONE);
//            replaceFragment(new PamsFragment());
//        }
        else if (id.equals("4")) {
            if (Utillity.isNetworkAvailable(getContext())){
                binding.li1.setVisibility(View.GONE);
                replaceFragment(new ProvidentFundFragment());
            }else {
                Utillity.openAlertDialog(getActivity(),getString(R.string.please_check_your_internet_connectivity));
            }
        }
    }
}