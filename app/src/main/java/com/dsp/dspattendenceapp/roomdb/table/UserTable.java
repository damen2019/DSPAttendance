package com.dsp.dspattendenceapp.roomdb.table;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usertable")
public class UserTable {
    @NonNull
    @PrimaryKey()
    private String EmpID;
    @ColumnInfo(name = "FullName")
    private String FullName;
    @ColumnInfo(name = "Username")

    private String Username;

    @ColumnInfo(name = "UserPass")

    private String UserPass;
    @ColumnInfo(name = "MyTheme")
    private String MyTheme;

    @ColumnInfo(name = "DeptName")
    private String DeptName;

    @ColumnInfo(name = "DesigName")
    private String DesigName;
    @ColumnInfo(name = "OfficeName")
    private String OfficeName;
    @ColumnInfo(name = "OfficeID")
    private Integer OfficeID;

    @ColumnInfo(name = "DeptID")
    private Integer DeptID;

    @ColumnInfo(name = "RoleName")
    private String RoleName;

    @ColumnInfo(name = "UserRole")
    private Integer UserRole;

    @ColumnInfo(name = "PositionID")
    private Integer PositionID;

    @ColumnInfo(name = "PositionLvl")
    private Integer PositionLvl;

    @ColumnInfo(name = "RankNum")
    private Integer RankNum;

    @ColumnInfo(name = "PositionName")
    private String PositionName;

    @ColumnInfo(name = "OfficeLevel")
    private Integer OfficeLevel;

    @ColumnInfo(name = "MainOffice")
    private Integer MainOffice;


    @ColumnInfo(name = "MainOfficeName")
    private String MainOfficeName;

    @ColumnInfo(name = "LTS_AoID")
    private Integer LTS_AoID;

    @ColumnInfo(name = "LTS_FoID")
    private Integer LTS_FoID;

    @ColumnInfo(name = "PositionRank")
    private Integer PositionRank;

    @ColumnInfo(name = "PWDChangeDate")
    private String PWDChangeDate;

    @ColumnInfo(name = "IsIslamic")
    private Integer IsIslamic;

    @ColumnInfo(name = "FYStart")
    private String FYStart;

    @ColumnInfo(name = "FYEnd")
    private String FYEnd;

    @ColumnInfo(name = "FinYear")
    private String FinYear;

    @ColumnInfo(name = "OfcID")
    private String OfcID;

    @ColumnInfo(name = "ParentOfcID")
    private String ParentOfcID;

    @ColumnInfo(name = "RegionCode")
    private String RegionCode;

    @ColumnInfo(name = "IsEnt")
    private Integer IsEnt;

    @ColumnInfo(name = "LatLon")
    private String LatLon;

    @ColumnInfo(name = "IsWFH")
    private Integer IsWFH;

    @ColumnInfo(name = "ProductPerm")
    private Integer ProductPerm;

    @ColumnInfo(name = "DeviceIMEI")
    private String DeviceIMEI;

    @ColumnInfo(name = "IsAuthorized")
    private Integer IsAuthorized;

    public UserTable() {
    }

    public UserTable(String empID, String fullName, String username, String userPass, String myTheme, String deptName, String desigName, String officeName, Integer officeID, Integer deptID, String roleName, Integer userRole, Integer positionID, Integer positionLvl, Integer rankNum, String positionName, Integer officeLevel, Integer mainOffice, String mainOfficeName, Integer LTS_AoID, Integer LTS_FoID, Integer positionRank, String PWDChangeDate, Integer isIslamic, String FYStart, String FYEnd, String finYear, String ofcID, String parentOfcID, String regionCode, Integer isEnt, String latLon, Integer isWFH, Integer productPerm,String deviceIMEI,Integer isAuthorized) {
        EmpID = empID;
        FullName = fullName;
        Username = username;
        UserPass = userPass;
        MyTheme = myTheme;
        DeptName = deptName;
        DesigName = desigName;
        OfficeName = officeName;
        OfficeID = officeID;
        DeptID = deptID;
        RoleName = roleName;
        UserRole = userRole;
        PositionID = positionID;
        PositionLvl = positionLvl;
        RankNum = rankNum;
        PositionName = positionName;
        OfficeLevel = officeLevel;
        MainOffice = mainOffice;
        MainOfficeName = mainOfficeName;
        this.LTS_AoID = LTS_AoID;
        this.LTS_FoID = LTS_FoID;
        PositionRank = positionRank;
        this.PWDChangeDate = PWDChangeDate;
        IsIslamic = isIslamic;
        this.FYStart = FYStart;
        this.FYEnd = FYEnd;
        FinYear = finYear;
        OfcID = ofcID;
        ParentOfcID = parentOfcID;
        RegionCode = regionCode;
        IsEnt = isEnt;
        LatLon = latLon;
        IsWFH = isWFH;
        ProductPerm = productPerm;
        DeviceIMEI = deviceIMEI;
        IsAuthorized = isAuthorized;
    }

    public String getDeviceIMEI() {
        return DeviceIMEI;
    }

    public void setDeviceIMEI(String deviceIMEI) {
        DeviceIMEI = deviceIMEI;
    }

    public Integer getIsAuthorized() {
        return IsAuthorized;
    }

    public void setIsAuthorized(Integer isAuthorized) {
        IsAuthorized = isAuthorized;
    }

    public String getEmpID() {
        return EmpID;
    }

    public void setEmpID(String empID) {
        EmpID = empID;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getUserPass() {
        return UserPass;
    }

    public void setUserPass(String userPass) {
        UserPass = userPass;
    }

    public String getMyTheme() {
        return MyTheme;
    }

    public void setMyTheme(String myTheme) {
        MyTheme = myTheme;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getDesigName() {
        return DesigName;
    }

    public void setDesigName(String desigName) {
        DesigName = desigName;
    }

    public String getOfficeName() {
        return OfficeName;
    }

    public void setOfficeName(String officeName) {
        OfficeName = officeName;
    }

    public Integer getOfficeID() {
        return OfficeID;
    }

    public void setOfficeID(Integer officeID) {
        OfficeID = officeID;
    }

    public Integer getDeptID() {
        return DeptID;
    }

    public void setDeptID(Integer deptID) {
        DeptID = deptID;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public Integer getUserRole() {
        return UserRole;
    }

    public void setUserRole(Integer userRole) {
        UserRole = userRole;
    }

    public Integer getPositionID() {
        return PositionID;
    }

    public void setPositionID(Integer positionID) {
        PositionID = positionID;
    }

    public Integer getPositionLvl() {
        return PositionLvl;
    }

    public void setPositionLvl(Integer positionLvl) {
        PositionLvl = positionLvl;
    }

    public Integer getRankNum() {
        return RankNum;
    }

    public void setRankNum(Integer rankNum) {
        RankNum = rankNum;
    }

    public String getPositionName() {
        return PositionName;
    }

    public void setPositionName(String positionName) {
        PositionName = positionName;
    }

    public Integer getOfficeLevel() {
        return OfficeLevel;
    }

    public void setOfficeLevel(Integer officeLevel) {
        OfficeLevel = officeLevel;
    }

    public Integer getMainOffice() {
        return MainOffice;
    }

    public void setMainOffice(Integer mainOffice) {
        MainOffice = mainOffice;
    }

    public String getMainOfficeName() {
        return MainOfficeName;
    }

    public void setMainOfficeName(String mainOfficeName) {
        MainOfficeName = mainOfficeName;
    }

    public Integer getLTS_AoID() {
        return LTS_AoID;
    }

    public void setLTS_AoID(Integer LTS_AoID) {
        this.LTS_AoID = LTS_AoID;
    }

    public Integer getLTS_FoID() {
        return LTS_FoID;
    }

    public void setLTS_FoID(Integer LTS_FoID) {
        this.LTS_FoID = LTS_FoID;
    }

    public Integer getPositionRank() {
        return PositionRank;
    }

    public void setPositionRank(Integer positionRank) {
        PositionRank = positionRank;
    }

    public String getPWDChangeDate() {
        return PWDChangeDate;
    }

    public void setPWDChangeDate(String PWDChangeDate) {
        this.PWDChangeDate = PWDChangeDate;
    }

    public Integer getIsIslamic() {
        return IsIslamic;
    }

    public void setIsIslamic(Integer isIslamic) {
        IsIslamic = isIslamic;
    }

    public String getFYStart() {
        return FYStart;
    }

    public void setFYStart(String FYStart) {
        this.FYStart = FYStart;
    }

    public String getFYEnd() {
        return FYEnd;
    }

    public void setFYEnd(String FYEnd) {
        this.FYEnd = FYEnd;
    }

    public String getFinYear() {
        return FinYear;
    }

    public void setFinYear(String finYear) {
        FinYear = finYear;
    }

    public String getOfcID() {
        return OfcID;
    }

    public void setOfcID(String ofcID) {
        OfcID = ofcID;
    }

    public String getParentOfcID() {
        return ParentOfcID;
    }

    public void setParentOfcID(String parentOfcID) {
        ParentOfcID = parentOfcID;
    }

    public String getRegionCode() {
        return RegionCode;
    }

    public void setRegionCode(String regionCode) {
        RegionCode = regionCode;
    }

    public Integer getIsEnt() {
        return IsEnt;
    }

    public void setIsEnt(Integer isEnt) {
        IsEnt = isEnt;
    }

    public String getLatLon() {
        return LatLon;
    }

    public void setLatLon(String latLon) {
        LatLon = latLon;
    }

    public Integer getIsWFH() {
        return IsWFH;
    }

    public void setIsWFH(Integer isWFH) {
        IsWFH = isWFH;
    }

    public Integer getProductPerm() {
        return ProductPerm;
    }

    public void setProductPerm(Integer productPerm) {
        ProductPerm = productPerm;
    }
}
