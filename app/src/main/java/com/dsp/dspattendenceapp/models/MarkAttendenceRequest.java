package com.dsp.dspattendenceapp.models;

public class MarkAttendenceRequest {
    String EmpID,Token,JsonData;

    public MarkAttendenceRequest(String empID, String token, String jsonData) {
        EmpID = empID;
        Token = token;
        JsonData = jsonData;
    }
}
