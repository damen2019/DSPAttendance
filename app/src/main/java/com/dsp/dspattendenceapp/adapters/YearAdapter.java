package com.dsp.dspattendenceapp.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.interfaces.OnYearClickListener;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceLogTable;

import java.util.List;

public class YearAdapter extends RecyclerView.Adapter<YearAdapter.MyViewHolder> {
    private Context context;
    private List<String> mData;
    private OnYearClickListener onYearClickListener;
    private Dialog dialog;

    public YearAdapter(Context context, List<String> mData, OnYearClickListener onYearClickListener, Dialog dialog) {
        this.context = context;
        this.mData = mData;
        this.onYearClickListener = onYearClickListener;
        this.dialog = dialog;
    }

    @NonNull
    @Override
    public YearAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_year_record_option, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YearAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (mData != null){
            holder.tvyear.setText(mData.get(position));
        }

        holder.relmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onYearClickListener.onClick(mData.get(position),dialog);
            }
        });
        holder.rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onYearClickListener.onClick(mData.get(position),dialog);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvyear;
        private RadioButton rd;
        private RelativeLayout relmain;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvyear= itemView.findViewById(R.id.tvyear);
            relmain= itemView.findViewById(R.id.relmain);
            rd= itemView.findViewById(R.id.rd);
        }
    }
}
