package com.dsp.dspattendenceapp.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.dsp.dspattendenceapp.roomdb.models.UserResponse;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;

@Dao
public interface UserDao {
    @Insert
    void insertUser(UserTable userTable);

    @Query("SELECT * FROM UserTable WHERE Username = :userName AND UserPass = :userPass LIMIT 1")
    UserTable login(String userName,String userPass);
    @Query("UPDATE UserTable SET EmpID = :EmpID, FullName= :FullName, Username = :Username, UserPass = :UserPass, MyTheme = :MyTheme, DeptName= :DeptName, DesigName= :DesigName, OfficeName= :OfficeName, OfficeID= :OfficeID, DeptID= :DeptID, RoleName= :RoleName, UserRole= :UserRole,PositionID= :PositionID,PositionLvl= :PositionLvl,RankNum= :RankNum,PositionName= :PositionName,OfficeLevel= :OfficeLevel,MainOffice= :MainOffice,MainOfficeName= :MainOfficeName,LTS_AoID= :LTS_AoID,LTS_FoID= :LTS_FoID,PositionRank= :PositionRank,PWDChangeDate= :PWDChangeDate,IsIslamic= :IsIslamic,FYStart= :FYStart,FYEnd= :FYEnd,FinYear= :FinYear,OfcID= :OfcID,ParentOfcID= :ParentOfcID,RegionCode= :RegionCode,IsEnt= :IsEnt,LatLon= :LatLon,IsWFH= :IsWFH,ProductPerm= :ProductPerm WHERE EmpID = :EmpID")
    void updateUser(String EmpID,String FullName, String Username, String UserPass, String MyTheme,String DeptName, String DesigName, String OfficeName, Integer OfficeID,Integer DeptID,String RoleName,Integer UserRole,Integer PositionID,Integer PositionLvl,Integer RankNum,String PositionName,Integer OfficeLevel,Integer MainOffice,String MainOfficeName,Integer LTS_AoID,Integer LTS_FoID,Integer PositionRank,String PWDChangeDate,Integer IsIslamic,String FYStart,String FYEnd,String FinYear,String OfcID,String ParentOfcID,String RegionCode,Integer IsEnt,String LatLon,Integer IsWFH,Integer ProductPerm);

    @Query("DELETE FROM UserTable")
     void nukeTable();

}
