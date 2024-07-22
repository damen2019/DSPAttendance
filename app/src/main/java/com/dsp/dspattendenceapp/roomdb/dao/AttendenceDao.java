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
    long insertAttendence(AttendenceTable attendenceTable);

    @Insert
    void insertAttendences(List<AttendenceTable> attendenceTables);


    @Query("UPDATE AttendenceTable SET clockout = :clockout, status = :status WHERE empID = :empID AND clockout = ''")
    void updateAttendance(String clockout, String status, String empID);

    @Query("SELECT * FROM AttendenceTable WHERE empID = :empID")
    List<AttendenceTable> getAllAttendances(String empID);

    @Query("SELECT DISTINCT empID, name, location, devicId, date, " +
            "(SELECT MIN(clockin) FROM AttendenceTable WHERE date = :date AND empID = :empID) AS clockin, " +
            "(SELECT MAX(clockout) FROM AttendenceTable WHERE date = :date AND empID = :empID) AS clockout " +
            "FROM AttendenceTable")
    List<AttendenceTable> getDistinctRecordsWithMinMaxClockInClockOut(String date,String empID);
}
