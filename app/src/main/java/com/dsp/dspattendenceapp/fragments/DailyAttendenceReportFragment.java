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

import com.dsp.dspattendenceapp.adapters.AttendanceRecordAdapter;
import com.dsp.dspattendenceapp.databinding.FragmentDailyAttendenceReportBinding;
import com.dsp.dspattendenceapp.interfaces.OnFetchDailyecord;
import com.dsp.dspattendenceapp.roomdb.dao.AttendenceDao;
import com.dsp.dspattendenceapp.roomdb.databases.MyDatabase;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DailyAttendenceReportFragment extends Fragment {

    private FragmentDailyAttendenceReportBinding binding;
    private List<AttendenceTable> list=new ArrayList<>();
    private AttendanceRecordAdapter adapter;
    MyDatabase myDb;
    AttendenceDao attendenceDao;
    String empID;

    public DailyAttendenceReportFragment(String empID) {
        this.empID = empID;
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
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getDailyAttendenceRecord() {
        list.clear();
        list= attendenceDao.getAllAttendances(empID);
        if (list !=null && !list.isEmpty() && list.size() >0){
            binding.rvRecord.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter=new AttendanceRecordAdapter(getContext(),list);
            binding.rvRecord.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}