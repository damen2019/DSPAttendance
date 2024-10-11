package com.dsp.dspattendenceapp.roomdb.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.dsp.dspattendenceapp.roomdb.dao.AttendenceDao;
import com.dsp.dspattendenceapp.roomdb.dao.AttendenceLogDao;
import com.dsp.dspattendenceapp.roomdb.dao.UserDao;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceLogTable;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;
import com.dsp.dspattendenceapp.roomdb.table.OfficesData;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;

@Database(entities = {UserTable.class, AttendenceTable.class, AttendenceLogTable.class, OfficesData.class},version = 9)
public abstract class MyDatabase extends RoomDatabase {
   public abstract UserDao getUserDao();
   public abstract AttendenceDao getAttendenceOfEmp();
   public abstract AttendenceLogDao getAttendenceLog();
}
