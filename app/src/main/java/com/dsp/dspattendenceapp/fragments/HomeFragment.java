package com.dsp.dspattendenceapp.fragments;



import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.adapters.MainOptionAdapter;
import com.dsp.dspattendenceapp.databinding.FragmentHomeBinding;
import com.dsp.dspattendenceapp.interfaces.OnOptionClickListener;
import com.dsp.dspattendenceapp.models.MainOptionModel;
import com.dsp.dspattendenceapp.utills.Utillity;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment implements OnOptionClickListener {
    private FragmentHomeBinding binding;
    private ArrayList<MainOptionModel> list=new ArrayList<>();
    private MainOptionAdapter adapter;
    private NavController navController;
    private String userName,lastName, userDesignation,empID,OfficeID,OfficeName;


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
            binding.tvinint.setText(Utillity.getInitials(userName));

        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private void valueOptions() {
        list.add(new MainOptionModel(getString(R.string.my_attendence),"1"));
        list.add(new MainOptionModel(getString(R.string.desktop_mis),"2"));
        list.add(new MainOptionModel(getString(R.string.my_salary_slip),"3"));
        list.add(new MainOptionModel(getString(R.string.my_pams),"4"));
        list.add(new MainOptionModel(getString(R.string.my_provident_fund_net_balance),"5"));

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

    @Override
    public void onClick(String id) {
        if (id.equals("1")){
            binding.li1.setVisibility(View.GONE);
            sendValueToFragment();
        } else if (id.equals("2")) {

        } else if (id.equals("3")) {
            binding.li1.setVisibility(View.GONE);
            replaceFragment(new SalarySlipFragment());
        } else if (id.equals("4")) {
            binding.li1.setVisibility(View.GONE);
            replaceFragment(new PamsFragment());
        }else if (id.equals("5")) {
            binding.li1.setVisibility(View.GONE);
            replaceFragment(new ProvidentFundFragment());

        }

    }
}