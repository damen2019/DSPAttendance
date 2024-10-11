package com.dsp.dspattendenceapp.services;

import android.os.AsyncTask;

import com.dsp.dspattendenceapp.roomdb.dao.AttendenceDao;
import com.dsp.dspattendenceapp.roomdb.databases.MyDatabase;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;

import java.util.List;

public class InsertDataTask extends AsyncTask<Void, Void, Void> {

    private AttendenceDao dao;
    private List<AttendenceTable> attendances;

    public InsertDataTask(MyDatabase db, List<AttendenceTable> attendances) {
        this.dao = db.getAttendenceOfEmp();
        this.attendances = attendances;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        dao.insertAttendence(attendances);
        return null;
    }
}