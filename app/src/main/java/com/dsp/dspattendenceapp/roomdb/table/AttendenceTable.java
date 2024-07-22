package com.dsp.dspattendenceapp.roomdb.table;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "attendencetable")
public class AttendenceTable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    Integer id;
    @ColumnInfo(name = "empID")
    String empID;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "location")
    String location;
    @ColumnInfo(name = "devicId")

    String devicId;
    @ColumnInfo(name = "date")
    String date;
    @ColumnInfo(name = "clockin")
    String clockin;
    @ColumnInfo(name = "clockout")
    String clockout;
    @ColumnInfo(name = "status")
    String status;

    public AttendenceTable(String empID, String name, String location, String devicId, String date, String clockin, String clockout, String status) {
        this.empID = empID;
        this.name = name;
        this.location = location;
        this.devicId = devicId;
        this.date = date;
        this.clockin = clockin;
        this.clockout = clockout;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmpID() {
        return empID;
    }

    public void setEmpID(String empID) {
        this.empID = empID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDevicId() {
        return devicId;
    }

    public void setDevicId(String devicId) {
        this.devicId = devicId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getClockin() {
        return clockin;
    }

    public void setClockin(String clockin) {
        this.clockin = clockin;
    }

    public String getClockout() {
        return clockout;
    }

    public void setClockout(String clockout) {
        this.clockout = clockout;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
