package com.dsp.dspattendenceapp.roomdb.table;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "attendencetable")
public class AttendenceTable {

//    @ColumnInfo(name = "EmpID")
//    String EmpID;
//    @ColumnInfo(name = "FullName")
//    String FullName;
//    @ColumnInfo(name = "DesigName")
//    String DesigName;
//    @ColumnInfo(name = "OfficeName")
//    String OfficeName;
    @ColumnInfo(name = "IN")
    @NonNull
    @PrimaryKey()
    String IN;
    @ColumnInfo(name = "OUT")
    String OUT;
    @ColumnInfo(name = "DTAttend")
    String DTAttend;
//    @ColumnInfo(name = "OfficeID_IN")
//    Integer OfficeID_IN;
//
//    @ColumnInfo(name = "OfficeIDName_IN")
//    String OfficeIDName_IN;
//
//    @ColumnInfo(name = "OfficeID_OUT")
//    Integer OfficeID_OUT;
//
//    @ColumnInfo(name = "OfficeIDName_OUT")
//    String OfficeIDName_OUT;



    public AttendenceTable() {
    }

    public AttendenceTable(@NonNull String IN, String OUT, String DTAttend) {
        this.IN = IN;
        this.OUT = OUT;
        this.DTAttend = DTAttend;
    }


    @NonNull
    public String getIN() {
        return IN;
    }

    public void setIN(@NonNull String IN) {
        this.IN = IN;
    }

    public String getOUT() {
        return OUT;
    }

    public void setOUT(String OUT) {
        this.OUT = OUT;
    }

    public String getDTAttend() {
        return DTAttend;
    }

    public void setDTAttend(String DTAttend) {
        this.DTAttend = DTAttend;
    }

}
