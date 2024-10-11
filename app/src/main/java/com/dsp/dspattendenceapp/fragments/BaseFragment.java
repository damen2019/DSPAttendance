package com.dsp.dspattendenceapp.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dsp.dspattendenceapp.R;

public class BaseFragment extends Fragment {
    public Dialog progressDialog;
    private Boolean loading=false;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void replaceFragment(Fragment fragment) {
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void replaceFragment1(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);  // Optional: to add to back stack for back navigation
        transaction.commit();
    }

    public void showWaitingDialog() {
        if(!loading)
        {
            getActivity().runOnUiThread(() -> {
                try {
                    progressDialog = new Dialog(getActivity());
                    if (progressDialog.getWindow() != null) {
                        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    }
                    progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    progressDialog.setCancelable(false);
                    progressDialog.setContentView(R.layout.layout_lottie_loading);
                    progressDialog.show();
                    loading=true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

    }

    public void dismissWaitingDialog() {
        getActivity().runOnUiThread(() -> {
            try {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    progressDialog.cancel();
                    loading=false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                progressDialog = null;
            }
        });

    }
}
