package com.dsp.dspattendenceapp.models;

public class PamsModel {
    String positionId,appraisalDate,appraisalPeriod;

    public PamsModel(String positionId, String appraisalDate, String appraisalPeriod) {
        this.positionId = positionId;
        this.appraisalDate = appraisalDate;
        this.appraisalPeriod = appraisalPeriod;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getAppraisalDate() {
        return appraisalDate;
    }

    public void setAppraisalDate(String appraisalDate) {
        this.appraisalDate = appraisalDate;
    }

    public String getAppraisalPeriod() {
        return appraisalPeriod;
    }

    public void setAppraisalPeriod(String appraisalPeriod) {
        this.appraisalPeriod = appraisalPeriod;
    }
}
