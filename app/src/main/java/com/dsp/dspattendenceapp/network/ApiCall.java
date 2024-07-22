package com.dsp.dspattendenceapp.network;

import com.dsp.dspattendenceapp.models.LoginRequest;
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
    @GET("EmployeeApi/login/{Username}/{UserPass}")
    Observable<Object> login(@Path("Username") String Username,@Path("UserPass") String UserPass);

    @GET("StudentAPI/{id}")
    Observable<Object> getEmployeeData(@Path("id") String id);

    @PUT("StudentAPI/updateDeviceId/{id}")
    Observable<Object> UpdateDeviceId(@Path("id") String id,@Body UpdateDeviceIdModel request);
}
