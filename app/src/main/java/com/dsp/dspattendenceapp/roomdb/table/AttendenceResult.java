package com.dsp.dspattendenceapp.roomdb.table;

public class AttendenceResult {
    private String empId;
    private String dtAttend;
    private String inTime;
    private String outTime;

    public AttendenceResult(String empId, String dtAttend, String inTime, String outTime) {
        this.empId = empId;
        this.dtAttend = dtAttend;
        this.inTime = inTime;
        this.outTime = outTime;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getDtAttend() {
        return dtAttend;
    }

    public void setDtAttend(String dtAttend) {
        this.dtAttend = dtAttend;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }
}
