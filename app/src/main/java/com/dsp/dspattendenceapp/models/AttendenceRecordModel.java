package com.dsp.dspattendenceapp.models;

public class AttendenceRecordModel {
    String date,in,out;


    public AttendenceRecordModel() {
    }

    public AttendenceRecordModel(String date, String in, String out) {
        this.date = date;
        this.in = in;
        this.out = out;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }
}
