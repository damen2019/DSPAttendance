package com.dsp.dspattendenceapp.models;

public class ProvidentFoundHistoryModel {
    private String Year_Month,Description;
    private int Amount,Balance;

    public ProvidentFoundHistoryModel() {
    }

    public ProvidentFoundHistoryModel(String year_Month, String description, int amount, int balance) {
        Year_Month = year_Month;
        Description = description;
        Amount = amount;
        Balance = balance;
    }

    public String getYear_Month() {
        return Year_Month;
    }

    public void setYear_Month(String year_Month) {
        Year_Month = year_Month;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public int getBalance() {
        return Balance;
    }

    public void setBalance(int balance) {
        Balance = balance;
    }
}
