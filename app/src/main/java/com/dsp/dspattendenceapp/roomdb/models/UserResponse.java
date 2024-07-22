package com.dsp.dspattendenceapp.roomdb.models;

public class UserResponse {
    private String name;
    private String lastname;
    private String designation;
    private String email;
    private String password;

    public UserResponse(String name, String lastname, String designation, String email, String password) {
        this.name = name;
        this.lastname = lastname;
        this.designation = designation;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
