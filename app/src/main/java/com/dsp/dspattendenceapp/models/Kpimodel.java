package com.dsp.dspattendenceapp.models;

public class Kpimodel {
    String kpi,kpides,achieved,comment;

    public Kpimodel(String kpi, String kpides, String achieved, String comment) {
        this.kpi = kpi;
        this.kpides = kpides;
        this.achieved = achieved;
        this.comment = comment;
    }

    public String getKpi() {
        return kpi;
    }

    public void setKpi(String kpi) {
        this.kpi = kpi;
    }

    public String getKpides() {
        return kpides;
    }

    public void setKpides(String kpides) {
        this.kpides = kpides;
    }

    public String getAchieved() {
        return achieved;
    }

    public void setAchieved(String achieved) {
        this.achieved = achieved;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
