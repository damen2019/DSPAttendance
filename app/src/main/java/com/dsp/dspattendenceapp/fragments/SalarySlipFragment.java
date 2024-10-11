package com.dsp.dspattendenceapp.fragments;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.adapters.YearAdapter;
import com.dsp.dspattendenceapp.databinding.FragmentSalarySlipBinding;
import com.dsp.dspattendenceapp.interfaces.OnYearClickListener;
import com.dsp.dspattendenceapp.network.ResponseHandler;
import com.dsp.dspattendenceapp.network.RestCaller;
import com.dsp.dspattendenceapp.network.RetrofitClient;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.utills.Preferences;
import com.dsp.dspattendenceapp.utills.Utillity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SalarySlipFragment extends BaseFragment implements OnYearClickListener, ResponseHandler {
    private static final int GET_EMP_SALARY_SLIP_REQ_CODE = 8;
    private FragmentSalarySlipBinding binding;
    List<String> yearMonths;
    private YearAdapter adapter;

    public SalarySlipFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSalarySlipBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        loadYearMonths();
        initClickListener();
        return view;
    }

    private void initClickListener() {
        binding.relyear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openYearMothDialog(yearMonths);
            }
        });

        binding.go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmpSalarySlip(binding.tvyearmonth.getText().toString().trim());
            }
        });
    }

    private void openYearMothDialog(List<String> yearMonths) {

            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.year_month_dialog);
            dialog.setCanceledOnTouchOutside(false);
            RecyclerView rvmothyear = dialog.findViewById(R.id.rvmothyear);
            setRecyclerView(rvmothyear,yearMonths,dialog);
            dialog.show();

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dailog);
            }


    }

    private void setRecyclerView(RecyclerView rvmothyear, List<String> yearMonths, Dialog dialog) {
        if (yearMonths != null && yearMonths.size() >0){
            rvmothyear.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter= new YearAdapter(getContext(),yearMonths,this,dialog);
            rvmothyear.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public void loadYearMonths() {
         yearMonths= new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);

        for (int i = 0; i <= 12; i++) {
            calendar.add(Calendar.MONTH, -1);
            yearMonths.add(sdf.format(calendar.getTime()));
        }
        binding.tvyearmonth.setText(yearMonths.get(0));
        getEmpSalarySlip(yearMonths.get(0));
    }

    private void getEmpSalarySlip(String year) {
        UserTable userTable= Preferences.getUserDetails(getContext());
        showWaitingDialog();
        new RestCaller(getContext(), this, RetrofitClient.getInstance(getActivity()).getEmployeeSalarySlip(userTable.getEmpID(),year,Preferences.getSharedPrefValue(getContext(),"token")), GET_EMP_SALARY_SLIP_REQ_CODE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(String year, Dialog dialog) {
        dialog.dismiss();
        binding.tvyearmonth.setText(year);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public <T> void onSuccess(Object response, int reqCode) {
        if (reqCode == GET_EMP_SALARY_SLIP_REQ_CODE){
            dismissWaitingDialog();
            try {
                JSONArray jsonArray=new JSONArray(Utillity.convertToString(response));
                setUiView(jsonArray);
            } catch (JSONException e) {
                Utillity.openErrorDialog(getActivity(),e.getMessage());
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUiView(JSONArray jsonArray) {
        if (jsonArray != null && jsonArray.length() >0){
            try {
               JSONObject jsonObject= jsonArray.getJSONObject(0);
               binding.tv1.setText(jsonObject.getString("VDate"));
               binding.tv2.setText(jsonObject.getString("FullName"));
               binding.tv3.setText(jsonObject.getString("OfficeName"));
               binding.tv4.setText(jsonObject.getString("DesigName"));
               binding.tv5.setText(jsonObject.getString("EmpID"));
               binding.tv6.setText(Utillity.convertDateFormat(jsonObject.getString("DTJoin")));
               binding.tv7.setText(jsonObject.getString("BankACNo"));
               binding.tv8.setText(jsonObject.getString("BankName"));
               binding.tv9.setText(String.valueOf(jsonObject.getInt("BasicSal")));
               binding.tv10.setText(String.valueOf(jsonObject.getInt("AlwHouse")));
               binding.tv11.setText(String.valueOf(jsonObject.getInt("AlwUtil")));
               binding.tv12.setText(String.valueOf(jsonObject.getInt("AlwMedical")));
               binding.tv13.setText(String.valueOf(jsonObject.getInt("AlwTravel")));
               binding.tv14.setText(String.valueOf(jsonObject.getInt("POL")));
               binding.tv15.setText(String.valueOf(jsonObject.getInt("OtherSal")));
               binding.tv16.setText(String.valueOf(jsonObject.getInt("GrossSal")));
               binding.tv17.setText(String.valueOf(jsonObject.getInt("EmplPF")));
               binding.tv18.setText(String.valueOf(jsonObject.getInt("DedTax")));
               binding.tv19.setText(String.valueOf(jsonObject.getInt("DedOther")));
               binding.tv20.setText(String.valueOf(jsonObject.getInt("DedOther3")));
               binding.tv21.setText(String.valueOf(jsonObject.getInt("DedOther1")));
               binding.tv22.setText(String.valueOf(jsonObject.getInt("DedOther2")));
               binding.tv23.setText(String.valueOf(jsonObject.getInt("DedEOBI")));
               binding.tv24.setText(String.valueOf(jsonObject.getInt("TotDed")));
               binding.tv25.setText(String.valueOf(jsonObject.getInt("NetPay")));

            } catch (JSONException e) {
                Utillity.openErrorDialog(getActivity(),e.getMessage());
            }
        }else {
            clearFields();
        }
    }

    private void clearFields() {
        binding.tv1.setText("");
        binding.tv2.setText("");
        binding.tv3.setText("");
        binding.tv4.setText("");
        binding.tv5.setText("");
        binding.tv6.setText("");
        binding.tv7.setText("");
        binding.tv8.setText("");
        binding.tv9.setText("");
        binding.tv10.setText("");
        binding.tv11.setText("");
        binding.tv12.setText("");
        binding.tv13.setText("");
        binding.tv14.setText("");
        binding.tv15.setText("");
        binding.tv16.setText("");
        binding.tv17.setText("");
        binding.tv18.setText("");
        binding.tv19.setText("");
        binding.tv20.setText("");
        binding.tv21.setText("");
        binding.tv22.setText("");
        binding.tv23.setText("");
        binding.tv24.setText("");
        binding.tv25.setText("");
    }

    @Override
    public void onFailure(Throwable t, int reqCode) {
        dismissWaitingDialog();
      Utillity.openErrorDialog(getActivity(),t.getMessage());
    }
}