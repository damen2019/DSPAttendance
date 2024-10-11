package com.dsp.dspattendenceapp.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.adapters.AttendanceRecordAdapter;
import com.dsp.dspattendenceapp.adapters.MonthlyAttendenceRecordAdapter;
import com.dsp.dspattendenceapp.adapters.MonthlyAttendenceRecordLocally;
import com.dsp.dspattendenceapp.databinding.FragmentMonthlyAttendenceReportBinding;
import com.dsp.dspattendenceapp.databinding.FragmentMyAttendenceBinding;
import com.dsp.dspattendenceapp.network.ResponseHandler;
import com.dsp.dspattendenceapp.network.RestCaller;
import com.dsp.dspattendenceapp.network.RetrofitClient;
import com.dsp.dspattendenceapp.roomdb.dao.AttendenceDao;
import com.dsp.dspattendenceapp.roomdb.dao.AttendenceLogDao;
import com.dsp.dspattendenceapp.roomdb.databases.MyDatabase;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceResult;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.utills.Preferences;
import com.dsp.dspattendenceapp.utills.Utillity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonthlyAttendenceReportFragment extends BaseFragment implements ResponseHandler {

    private static final int GET_EMP_ATTENDENCE_REQ_CODE = 05;
    private FragmentMonthlyAttendenceReportBinding binding;
    private List<AttendenceTable> list=new ArrayList<>();
    private List<AttendenceResult> list2=new ArrayList<>();
    private MonthlyAttendenceRecordAdapter adapter;
    private MonthlyAttendenceRecordLocally attendenceRecordLocally;
    MyDatabase myDb;
    private AttendenceLogDao attendenceLogDao;
    String empID;
    public MonthlyAttendenceReportFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMonthlyAttendenceReportBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initDb();
        getRecordsForDateWithMinMaxClockInClockOut();
        return view;
    }
    private void initDb() {
        myDb= Room.databaseBuilder(getContext(), MyDatabase.class,"attendencelogtable")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        attendenceLogDao=myDb.getAttendenceLog();
    }
    @SuppressLint("NotifyDataSetChanged")
    private void getRecordsForDateWithMinMaxClockInClockOut() {
        UserTable userTable= Preferences.getUserDetails(getContext());
        empID= userTable.getEmpID();
        if (Utillity.isNetworkAvailable(getContext())){
            new RestCaller(getContext(), this, RetrofitClient.getInstance(getActivity()).getEmployeeAttendence(empID, Preferences.getSharedPrefValue(getContext(),"token")), GET_EMP_ATTENDENCE_REQ_CODE);
        }else {
            list2.clear();
            list2 = attendenceLogDao.getAttendanceLocally(empID);
            setRecyclerViewLocal(list2);
        }

    }
    @SuppressLint("NotifyDataSetChanged")
    private void setRecyclerViewLocal(List<AttendenceResult> list2) {
        if (!list2.isEmpty() && list2.size() > 0){
            binding.rvMonthlyRecord.setLayoutManager(new LinearLayoutManager(getContext()));
//            Collections.reverse(list);
            attendenceRecordLocally=new MonthlyAttendenceRecordLocally(getContext(),list2);
            binding.rvMonthlyRecord.setAdapter(attendenceRecordLocally);
            attendenceRecordLocally.notifyDataSetChanged();
        }else {
            showorNot();
        }

    }
    @Override
    public <T> void onSuccess(Object response, int reqCode) {
        if (reqCode == GET_EMP_ATTENDENCE_REQ_CODE){
            dismissWaitingDialog();
            list.clear();
            try {
                JSONArray jsonArray=new JSONArray(Utillity.convertToString(response));
                Gson gson = new Gson();
                Type type = new TypeToken<List<AttendenceTable>>() {
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
        Log.d("TAG", "AAA: "+ t.getMessage());
//        Utillity.openAlertDialog(getActivity(),t.getMessage());
    }
    @SuppressLint("NotifyDataSetChanged")
    private void setRecyclerView(List<AttendenceTable> list) {
        if (!list.isEmpty() && list.size() > 0){
            binding.rvMonthlyRecord.setLayoutManager(new LinearLayoutManager(getContext()));
//            Collections.reverse(list);
            adapter=new MonthlyAttendenceRecordAdapter(getContext(),list);
            binding.rvMonthlyRecord.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else {
            showorNot();
        }
    }
    private void showorNot() {
        binding.rvMonthlyRecord.setVisibility(View.GONE);
        binding.tvnoShow.setVisibility(View.VISIBLE);
    }
}