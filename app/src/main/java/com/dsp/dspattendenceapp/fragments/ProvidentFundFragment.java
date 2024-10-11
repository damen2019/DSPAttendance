package com.dsp.dspattendenceapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.adapters.MainOptionAdapter;
import com.dsp.dspattendenceapp.adapters.MonthlyAttendenceRecordAdapter;
import com.dsp.dspattendenceapp.adapters.ProvidentFoundHistoryAdapter;
import com.dsp.dspattendenceapp.databinding.FragmentPamsBinding;
import com.dsp.dspattendenceapp.databinding.FragmentProvidentFundBinding;
import com.dsp.dspattendenceapp.models.ProvidentFoundHistoryModel;
import com.dsp.dspattendenceapp.network.ResponseHandler;
import com.dsp.dspattendenceapp.network.RestCaller;
import com.dsp.dspattendenceapp.network.RetrofitClient;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.utills.Preferences;
import com.dsp.dspattendenceapp.utills.Utillity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProvidentFundFragment extends BaseFragment implements ResponseHandler {
    private static final int GET_EMP_PF_REQ_CODE = 007;
    private FragmentProvidentFundBinding binding;
    private List<ProvidentFoundHistoryModel> list=new ArrayList<>();
    private ProvidentFoundHistoryAdapter adapter;

    public ProvidentFundFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProvidentFundBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        getPFData();
        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getPFData() {
        UserTable userTable= Preferences.getUserDetails(getContext());
        showWaitingDialog();
        new RestCaller(getContext(), this, RetrofitClient.getInstance(getActivity()).getEmpPf(userTable.getEmpID(), Preferences.getSharedPrefValue(getContext(),"token")), GET_EMP_PF_REQ_CODE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nullify the binding object to avoid memory leaks
        binding = null;
    }

    @Override
    public <T> void onSuccess(Object response, int reqCode) {
        if (reqCode == GET_EMP_PF_REQ_CODE){
            dismissWaitingDialog();
            list.clear();
            try {
                JSONArray jsonArray=new JSONArray(Utillity.convertToString(response));
                Gson gson = new Gson();
                Type type = new TypeToken<List<ProvidentFoundHistoryModel>>() {
                }.getType();
                list = gson.fromJson(jsonArray.toString(), type);
                setRecyclerView(list);
            } catch (JSONException e) {
                Utillity.openErrorDialog(getActivity(),e.getMessage());
            }
        }
    }
    @Override
    public void onFailure(Throwable t, int reqCode) {
        dismissWaitingDialog();
     Utillity.openErrorDialog(getActivity(),t.getMessage());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setRecyclerView(List<ProvidentFoundHistoryModel> listt) {
        if (!listt.isEmpty() && listt.size() > 0){
            binding.rvpfhistory.setLayoutManager(new LinearLayoutManager(getContext()));
            Collections.reverse(listt);
            adapter= new ProvidentFoundHistoryAdapter(getContext(),listt);
            binding.rvpfhistory.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else {
            showorNot();
        }
    }
    private void showorNot() {
        binding.rvpfhistory.setVisibility(View.GONE);
        binding.textView.setVisibility(View.GONE);
        binding.tvnoShow.setVisibility(View.VISIBLE);
    }
}