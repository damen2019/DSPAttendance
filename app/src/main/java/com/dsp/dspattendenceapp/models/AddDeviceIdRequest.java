package com.dsp.dspattendenceapp.models;

public class AddDeviceIdRequest {
    String DeviceID,DeviceCompany,DeviceModel,EmpID,Token;

    public AddDeviceIdRequest(String deviceID, String deviceCompany, String deviceModel, String empID, String token) {
        DeviceID = deviceID;
        DeviceCompany = deviceCompany;
        DeviceModel = deviceModel;
        EmpID = empID;
        Token = token;
    }
}
