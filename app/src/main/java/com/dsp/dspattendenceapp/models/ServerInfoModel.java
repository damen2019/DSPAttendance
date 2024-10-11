package com.dsp.dspattendenceapp.models;

import java.io.Serializable;

public class ServerInfoModel implements Serializable {
    private String ip;
    private String hostname;
    boolean status;

    String date;
    String name;
    String type;

    public ServerInfoModel(String ip, String hostname, boolean status, String date, String name, String type) {
        this.ip = ip;
        this.hostname = hostname;
        this.status = status;
        this.date = date;
        this.name = name;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
