package com.dsp.dspattendenceapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.databinding.FragmentHomeBinding;
import com.dsp.dspattendenceapp.databinding.FragmentSelectOfficeBinding;

public class SelectOfficeFragment extends Fragment {
    private FragmentSelectOfficeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSelectOfficeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nullify the binding object to avoid memory leaks
        binding = null;
    }
}