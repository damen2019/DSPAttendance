package com.dsp.dspattendenceapp.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.databinding.FragmentMyAttendenceBinding;
import com.dsp.dspattendenceapp.databinding.FragmentSettingBinding;
import com.dsp.dspattendenceapp.network.ResponseHandler;
import com.dsp.dspattendenceapp.network.RestCaller;
import com.dsp.dspattendenceapp.network.RetrofitClient;
import com.dsp.dspattendenceapp.roomdb.dao.AttendenceLogDao;
import com.dsp.dspattendenceapp.roomdb.databases.MyDatabase;
import com.dsp.dspattendenceapp.roomdb.table.OfficesData;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.utills.Preferences;
import com.dsp.dspattendenceapp.utills.Utillity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SettingFragment extends BaseFragment implements ResponseHandler {
    private FragmentSettingBinding binding;
    private static final int GET_EMP_OFFICES_REQ_CODE = 87;
    private List<OfficesData> listOffices = new ArrayList<>();
    private AttendenceLogDao attendenceLogDao;
    private MyDatabase myDb2;

    public SettingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initDb();
        initClickListener();
        return view;
    }

    private void initClickListener() {
        binding.relupdateOffice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hitApi();

            }
        });
    }

    private void initDb() {
        myDb2= Room.databaseBuilder(getContext(), MyDatabase.class,"attendencelogtable")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        attendenceLogDao=myDb2.getAttendenceLog();
    }
    private void hitApi() {
        UserTable userTable= Preferences.getUserDetails(getContext());
        showWaitingDialog();
        new RestCaller(getContext(), this, RetrofitClient.getInstance(getActivity()).getEmpOffices(userTable.getEmpID(), Preferences.getSharedPrefValue(getContext(),"token")), GET_EMP_OFFICES_REQ_CODE);
    }

    @Override
    public <T> void onSuccess(Object response, int reqCode) {
        if (reqCode == GET_EMP_OFFICES_REQ_CODE){
            dismissWaitingDialog();
            JSONArray jsonArray= null;
            attendenceLogDao.nukeTable();
            try {
                jsonArray = new JSONArray(Utillity.convertToString(response));
//                Gson gson = new Gson();
//                Type type = new TypeToken<List<OfficesData>>() {
//                }.getType();
//                listOffices = gson.fromJson(jsonArray.toString(), type);
                Log.d("TAG", "onSuccess: "+jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    OfficesData office = new OfficesData(
                            jsonObject.getString("OfcID"),
                            jsonObject.getString("ParentOfcID"),
                            jsonObject.getInt("OfficeID"),
                            jsonObject.getString("OfficeName"),
                            jsonObject.getInt("OfficeOrder"),
                            jsonObject.getBoolean("IsActive"),
                            jsonObject.getString("LatLon")
                    );
                    attendenceLogDao.insertOffices(office);
                }
                Utillity.openAlertDialog(getActivity(),"Office list is updated please go home page and open attendence page again.");
            } catch (JSONException e) {
                Utillity.openErrorDialog(getActivity(),e.getMessage());
            }

        }
    }

    @Override
    public void onFailure(Throwable t, int reqCode) {
        dismissWaitingDialog();
        Utillity.openErrorDialog(getActivity(),t.getMessage());
    }
}