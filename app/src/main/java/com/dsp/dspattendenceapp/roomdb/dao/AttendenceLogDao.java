package com.dsp.dspattendenceapp.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dsp.dspattendenceapp.roomdb.table.AttendenceLogTable;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceResult;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;
import com.dsp.dspattendenceapp.roomdb.table.OfficesData;

import java.util.List;

@Dao
public interface  AttendenceLogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAttendenceLog(AttendenceLogTable attendenceLogTable);

    @Insert
    void insertOffices(OfficesData officesData);


    @Query("SELECT * FROM AttendenceLogTable WHERE EmpID = :EmpID AND sync = :sync")
    List<AttendenceLogTable> getAllAttendanceLogs(String EmpID,boolean sync);

    @Query("SELECT * FROM OfficesData ")
    List<OfficesData> getAllOffices();

    @Query("SELECT * FROM AttendenceLogTable WHERE EmpID = :EmpID")
    List<AttendenceLogTable> getAllAttendanceLogsOfEmp(String EmpID);


    @Query("UPDATE AttendenceLogTable SET sync = :sync WHERE DTAttend = :dtAttend AND empID = :empID AND sync = 1")
    void updateAttendanceSync(boolean sync, String dtAttend, String empID);

    @Query("SELECT DISTINCT EmpID AS empId, DATE(DTAttend) AS dtAttend, " +
            "(SELECT MIN(DTAttend) FROM AttendenceLogTable WHERE EmpID = A.EmpID AND AttDIR = 'IN' AND DATE(DTAttend) = DATE(A.dtAttend)) AS inTime, " +
            "(SELECT MAX(DTAttend) FROM AttendenceLogTable WHERE EmpID = A.EmpID AND AttDIR = 'OUT' AND DATE(DTAttend) = DATE(A.dtAttend)) AS outTime " +
            "FROM AttendenceLogTable A " +
            "WHERE EmpID = :empId " +
            "LIMIT 15")
    List<AttendenceResult> getAttendanceLocally(String empId);

    @Query("DELETE FROM OfficesData")
    void nukeTable();

    @Query("DELETE FROM ATTENDENCELOGTABLE")
    void clearTable();


}
