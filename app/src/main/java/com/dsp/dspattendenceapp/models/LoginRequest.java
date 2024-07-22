package com.dsp.dspattendenceapp.models;

public class LoginRequest {
    private String Username;
    private String UserPass;

    public LoginRequest(String userName, String userPass) {
        Username = userName;
        UserPass = userPass;
    }
}
