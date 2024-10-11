package com.dsp.dspattendenceapp.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AttendenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAttendence(List<AttendenceTable> attendenceTables);

    @Insert
    void insertAttendences(AttendenceTable attendenceTables);


//    @Query("UPDATE AttendenceTable SET dtTimeOut = :clockout, GPS_OUT = :GPS_OUT  WHERE DTAttend = :dtAttend AND empID = :empID AND dtTimeOut = '' OR dtTimeOut = '00:00:00'")
//    void updateAttendance(String clockout, String dtAttend, String empID, String GPS_OUT);

//    @Query("UPDATE AttendenceTable SET sync = :sync WHERE DTAttend = :dtAttend AND empID = :empID AND sync = true")
//    void updateAttendanceSync(boolean sync, String dtAttend, String empID);


//    @Query("SELECT * FROM AttendenceTable WHERE empID = :empID")
//    List<AttendenceTable> getAllAttendances(String empID);

//    @Query("SELECT * FROM AttendenceTable WHERE sync = :sync")
//    List<AttendenceTable> getAllAttendancesNotSync(Boolean sync);

//    @Query("SELECT DISTINCT empID, name, location, devicId, date, " +
//            "(SELECT MIN(clockin) FROM AttendenceTable WHERE date = :date AND empID = :empID) AS clockin, " +
//            "(SELECT MAX(clockout) FROM AttendenceTable WHERE date = :date AND empID = :empID) AS clockout " +
//            "FROM AttendenceTable")
//    List<AttendenceTable> getDistinctRecordsWithMinMaxClockInClockOut(String date,String empID);
}
