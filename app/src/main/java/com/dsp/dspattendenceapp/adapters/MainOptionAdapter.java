package com.dsp.dspattendenceapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.interfaces.OnOptionClickListener;
import com.dsp.dspattendenceapp.models.MainOptionModel;

import java.util.ArrayList;

public class MainOptionAdapter extends RecyclerView.Adapter<MainOptionAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<MainOptionModel> arrayList;

    private OnOptionClickListener onOptionClickListener;

    public MainOptionAdapter(Context context, ArrayList<MainOptionModel> arrayList, OnOptionClickListener onOptionClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.onOptionClickListener = onOptionClickListener;
    }

    @NonNull
    @Override
    public MainOptionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_option, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainOptionAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
      if (arrayList !=null){
          holder.optionname.setText(arrayList.get(position).getOptionName());

          holder.optionname.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  onOptionClickListener.onClick(arrayList.get(position).getId());
              }
          });
      }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView optionname;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            optionname=itemView.findViewById(R.id.optionname);
        }
    }
}
