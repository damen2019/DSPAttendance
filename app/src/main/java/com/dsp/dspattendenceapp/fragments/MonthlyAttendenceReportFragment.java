package com.dsp.dspattendenceapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.adapters.AttendanceRecordAdapter;
import com.dsp.dspattendenceapp.databinding.FragmentMonthlyAttendenceReportBinding;
import com.dsp.dspattendenceapp.databinding.FragmentMyAttendenceBinding;
import com.dsp.dspattendenceapp.roomdb.dao.AttendenceDao;
import com.dsp.dspattendenceapp.roomdb.databases.MyDatabase;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;
import com.dsp.dspattendenceapp.utills.Utillity;

import java.util.ArrayList;
import java.util.List;

public class MonthlyAttendenceReportFragment extends Fragment {

    private FragmentMonthlyAttendenceReportBinding binding;
    private List<AttendenceTable> list=new ArrayList<>();
    private AttendanceRecordAdapter adapter;
    MyDatabase myDb;
    AttendenceDao attendenceDao;
    String empID;
    public MonthlyAttendenceReportFragment(String empID) {
        this.empID = empID;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMonthlyAttendenceReportBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initDb();
        getRecordsForDateWithMinMaxClockInClockOut();
        return view;
    }

    private void initDb() {
        myDb= Room.databaseBuilder(getContext(), MyDatabase.class,"attendencetable")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        attendenceDao=myDb.getAttendenceOfEmp();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getRecordsForDateWithMinMaxClockInClockOut() {
        list.clear();
        list= attendenceDao.getDistinctRecordsWithMinMaxClockInClockOut(Utillity.getCurrentDateWithOutDay(),empID);
        if (list !=null && !list.isEmpty() && list.size() >0){
            binding.rvMonthlyRecord.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter=new AttendanceRecordAdapter(getContext(),list);
            binding.rvMonthlyRecord.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }
}