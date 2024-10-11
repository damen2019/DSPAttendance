package com.dsp.dspattendenceapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.databinding.FragmentChangePasswordBinding;
import com.dsp.dspattendenceapp.databinding.FragmentProvidentFundBinding;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.utills.Preferences;


public class ChangePasswordFragment extends BaseFragment {
    private FragmentChangePasswordBinding  binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        UserTable userTable= Preferences.getUserDetails(getContext());
        binding.etuserid.setText(userTable.getEmpID());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}