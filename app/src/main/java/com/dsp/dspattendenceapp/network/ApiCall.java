package com.dsp.dspattendenceapp.network;

import com.dsp.dspattendenceapp.models.AddDeviceIdRequest;
import com.dsp.dspattendenceapp.models.LoginRequest;
import com.dsp.dspattendenceapp.models.MarkAttendenceRequest;
import com.dsp.dspattendenceapp.models.UpdateDeviceIdModel;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ApiCall {
    @GET("login/{Username}/{UserPass}")
    Observable<Object> login(@Path("Username") String Username,@Path("UserPass") String UserPass);

    @GET("getEmployeeAttendence/{EmpID}/{Token}")
    Observable<Object> getEmployeeAttendence(@Path("EmpID") String EmpID,@Path("Token") String Token);

    @GET("getEmployeePf/{EmpID}/{Token}")
    Observable<Object> getEmpPf(@Path("EmpID") String EmpID,@Path("Token") String Token);

    @GET("getEmployeeSalarySlip/{EmpID}/{YearMonth}/{Token}")
    Observable<Object> getEmployeeSalarySlip(@Path("EmpID") String EmpID,@Path("YearMonth") String YearMonth,@Path("Token") String Token);

    @POST("AddDevice")
    Observable<Object> AddDevice(@Body AddDeviceIdRequest request);

    @POST("MarkAttendence")
    Observable<Object> markEmployeeAttendence(@Body MarkAttendenceRequest request);

    @GET("getEmpOffices/{EmpID}/{Token}")
    Observable<Object> getEmpOffices(@Path("EmpID") String EmpID,@Path("Token") String Token);


    @GET("DSP_Net_Stat.json")
    Observable<Object> getServerData();

}
