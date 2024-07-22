package com.dsp.dspattendenceapp.roomdb.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dsp.dspattendenceapp.roomdb.dao.AttendenceDao;
import com.dsp.dspattendenceapp.roomdb.dao.UserDao;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;

@Database(entities = {UserTable.class, AttendenceTable.class},version = 4)
public abstract class MyDatabase extends RoomDatabase {
   public abstract UserDao getUserDao();
   public abstract AttendenceDao getAttendenceOfEmp();
}
