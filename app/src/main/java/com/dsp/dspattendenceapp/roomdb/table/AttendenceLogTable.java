package com.dsp.dspattendenceapp.roomdb.table;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "attendencelogtable")
public class AttendenceLogTable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    Integer id;


    @ColumnInfo(name = "AttDIR")
    String AttDIR;

    @ColumnInfo(name = "DTAttend")
    String DTAttend;

    @ColumnInfo(name = "EmpID")
    String EmpID;

    @ColumnInfo(name = "OfficeID")
    String OfficeID;

    @ColumnInfo(name = "LatLon")
    String LatLon;

    boolean sync;

    public AttendenceLogTable() {
    }

    public AttendenceLogTable(String attDIR, String DTAttend, String empID, String officeID, String latLon, Boolean sync) {
        AttDIR = attDIR;
        this.DTAttend = DTAttend;
        EmpID = empID;
        OfficeID = officeID;
        LatLon = latLon;
        this.sync = sync;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getAttDIR() {
        return AttDIR;
    }

    public void setAttDIR(String attDIR) {
        AttDIR = attDIR;
    }

    public String getDTAttend() {
        return DTAttend;
    }

    public void setDTAttend(String DTAttend) {
        this.DTAttend = DTAttend;
    }

    public String getEmpID() {
        return EmpID;
    }

    public void setEmpID(String empID) {
        EmpID = empID;
    }

    public String getOfficeID() {
        return OfficeID;
    }

    public void setOfficeID(String officeID) {
        OfficeID = officeID;
    }

    public String getLatLon() {
        return LatLon;
    }

    public void setLatLon(String latLon) {
        LatLon = latLon;
    }
}
