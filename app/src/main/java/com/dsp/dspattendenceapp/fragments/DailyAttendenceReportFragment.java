package com.dsp.dspattendenceapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dsp.dspattendenceapp.activites.LoginActivity;
import com.dsp.dspattendenceapp.adapters.AttendanceRecordAdapter;
import com.dsp.dspattendenceapp.databinding.FragmentDailyAttendenceReportBinding;
import com.dsp.dspattendenceapp.models.MarkAttendenceRequest;
import com.dsp.dspattendenceapp.network.ResponseHandler;
import com.dsp.dspattendenceapp.network.RestCaller;
import com.dsp.dspattendenceapp.network.RetrofitClient;
import com.dsp.dspattendenceapp.roomdb.dao.AttendenceDao;
import com.dsp.dspattendenceapp.roomdb.dao.AttendenceLogDao;
import com.dsp.dspattendenceapp.roomdb.databases.MyDatabase;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceLogTable;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.utills.Preferences;
import com.dsp.dspattendenceapp.utills.Utillity;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DailyAttendenceReportFragment extends BaseFragment {

    private static final int MARK_EMP_ATTENDENCE_REQ_CODE = 06;
    private FragmentDailyAttendenceReportBinding binding;
    private List<AttendenceLogTable> listNotSync=new ArrayList<>();
    private AttendanceRecordAdapter adapter;
    MyDatabase myDb,myDb2;
    AttendenceDao attendenceDao;
    AttendenceLogDao attendenceLogDao;
    String empID;

    public DailyAttendenceReportFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDailyAttendenceReportBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initDb();
        getDailyAttendenceRecord();
        return view;
    }

    private void initDb() {
        myDb= Room.databaseBuilder(getContext(), MyDatabase.class,"attendencetable")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        attendenceDao=myDb.getAttendenceOfEmp();

        myDb2= Room.databaseBuilder(getContext(), MyDatabase.class,"attendencelogtable")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        attendenceLogDao=myDb2.getAttendenceLog();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getDailyAttendenceRecord() {
        UserTable userTable= Preferences.getUserDetails(getContext());
        empID = userTable.getEmpID();
            listNotSync.clear();
            listNotSync=attendenceLogDao.getAllAttendanceLogsOfEmp(empID);
        if (listNotSync !=null && !listNotSync.isEmpty() && listNotSync.size() >0){
                binding.rvRecord.setLayoutManager(new LinearLayoutManager(getContext()));
                Collections.reverse(listNotSync);
                adapter=new AttendanceRecordAdapter(getContext(),listNotSync);
                binding.rvRecord.setAdapter(adapter);adapter.notifyDataSetChanged();
        }else {
            showOrNot();
        }

    }

    private void showOrNot() {
        binding.rvRecord.setVisibility(View.GONE);
        binding.tvnoShow.setVisibility(View.VISIBLE);
    }


}