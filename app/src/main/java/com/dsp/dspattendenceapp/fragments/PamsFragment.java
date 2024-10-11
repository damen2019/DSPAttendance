package com.dsp.dspattendenceapp.fragments;

import static com.dsp.dspattendenceapp.R.id.radioButton1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.adapters.KpiAdapter;
import com.dsp.dspattendenceapp.adapters.PamsReportTopAdatper;
import com.dsp.dspattendenceapp.adapters.ProvidentFoundHistoryAdapter;
import com.dsp.dspattendenceapp.databinding.FragmentDspBranchesBinding;
import com.dsp.dspattendenceapp.databinding.FragmentPamsBinding;
import com.dsp.dspattendenceapp.models.Kpimodel;
import com.dsp.dspattendenceapp.models.PamsModel;
import com.dsp.dspattendenceapp.models.ProvidentFoundHistoryModel;

import java.util.ArrayList;
import java.util.List;

public class PamsFragment extends Fragment {
    private FragmentPamsBinding binding;
    private List<PamsModel> list=new ArrayList<>();
    private List<Kpimodel> listKpi=new ArrayList<>();
    private PamsReportTopAdatper adapter;
    private KpiAdapter adapterKpi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPamsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        initClickListener();
        getPamsData();
        getKpiData();
        return view;
    }

    private void getKpiData() {
        listKpi.add(new Kpimodel("010","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS to Dy.Manager Development.","3","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS."));
        listKpi.add(new Kpimodel("010","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS to Dy.Manager Development.","3","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS."));
        listKpi.add(new Kpimodel("010","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS to Dy.Manager Development.","3","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS."));
        listKpi.add(new Kpimodel("010","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS to Dy.Manager Development.","3","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS."));
        listKpi.add(new Kpimodel("010","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS to Dy.Manager Development.","3","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS."));
        listKpi.add(new Kpimodel("010","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS to Dy.Manager Development.","3","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS."));
        listKpi.add(new Kpimodel("010","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS to Dy.Manager Development.","3","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS."));
        listKpi.add(new Kpimodel("010","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS to Dy.Manager Development.","3","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS."));
        listKpi.add(new Kpimodel("010","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS to Dy.Manager Development.","3","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS."));
        listKpi.add(new Kpimodel("010","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS to Dy.Manager Development.","3","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS."));
        listKpi.add(new Kpimodel("010","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS to Dy.Manager Development.","3","Provide continuous support in the integration & development of DSP MIS and Mobile-MIS."));
        binding.rvkpi.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterKpi= new KpiAdapter(getContext(),listKpi);
        binding.rvkpi.setAdapter(adapterKpi);
        binding.rvkpi.setNestedScrollingEnabled(true);
        adapterKpi.notifyDataSetChanged();
    }

    private void getPamsData() {
        list.add(new PamsModel("125","30/Sep/2023","Quarterly"));
        list.add(new PamsModel("125","31/Dec/2023","Quarterly"));
        list.add(new PamsModel("125","30/Mar/2023","Quarterly"));
        list.add(new PamsModel("125","30/Jun/2023","Quarterly"));
        binding.rvpamreporttop.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter= new PamsReportTopAdatper(getContext(),list);
        binding.rvpamreporttop.setAdapter(adapter);
        binding.rvpamreporttop.setNestedScrollingEnabled(true);
        adapter.notifyDataSetChanged();
    }

    private void initClickListener() {
        binding.radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.tvpams.setText("PAMS Report(Un-saved)");
                    binding.radioButton2.setChecked(false);
                }
            }
        });
        binding.radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    binding.tvpams.setText("PAMS Report(Saved)");
                    binding.radioButton1.setChecked(false);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Nullify the binding object to avoid memory leaks
        binding = null;
    }
}