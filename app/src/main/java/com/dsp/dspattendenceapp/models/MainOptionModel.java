package com.dsp.dspattendenceapp.models;

public class MainOptionModel {
    String optionName,id;

    public MainOptionModel() {
    }

    public MainOptionModel(String optionName, String id) {
        this.optionName = optionName;
        this.id = id;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
