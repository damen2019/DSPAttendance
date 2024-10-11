package com.dsp.dspattendenceapp.roomdb.table;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "OfficesData")
public class OfficesData {

    @NonNull
    @PrimaryKey()
    private String OfcID;

    @ColumnInfo(name = "ParentOfcID")
    private String ParentOfcID;

    @ColumnInfo(name = "OfficeID")
    private int OfficeID;

    @ColumnInfo(name = "OfficeName")
    private String OfficeName;

    @ColumnInfo(name = "OfficeOrder")
    private int OfficeOrder;

    @ColumnInfo(name = "IsActive")
    private Boolean IsActive;

    @ColumnInfo(name = "LatLon")
    private String LatLon;

    public OfficesData() {
    }

    public OfficesData(@NonNull String ofcID, String parentOfcID, int officeID, String officeName, int officeOrder, Boolean isActive, String latLon) {
        OfcID = ofcID;
        ParentOfcID = parentOfcID;
        OfficeID = officeID;
        OfficeName = officeName;
        OfficeOrder = officeOrder;
        IsActive = isActive;
        LatLon = latLon;
    }

    @NonNull
    public String getOfcID() {
        return OfcID;
    }

    public void setOfcID(@NonNull String ofcID) {
        OfcID = ofcID;
    }

    public String getParentOfcID() {
        return ParentOfcID;
    }

    public void setParentOfcID(String parentOfcID) {
        ParentOfcID = parentOfcID;
    }

    public int getOfficeID() {
        return OfficeID;
    }

    public void setOfficeID(int officeID) {
        OfficeID = officeID;
    }

    public String getOfficeName() {
        return OfficeName;
    }

    public void setOfficeName(String officeName) {
        OfficeName = officeName;
    }

    public int getOfficeOrder() {
        return OfficeOrder;
    }

    public void setOfficeOrder(int officeOrder) {
        OfficeOrder = officeOrder;
    }

    public Boolean getIsActive() {
        return IsActive;
    }

    public void setIsActive(Boolean active) {
        IsActive = active;
    }

    public String getLatLon() {
        return LatLon;
    }

    public void setLatLon(String latLon) {
        LatLon = latLon;
    }
}
