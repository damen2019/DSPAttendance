package com.dsp.dspattendenceapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.models.AttendenceRecordModel;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;

import java.util.ArrayList;
import java.util.List;

public class AttendanceRecordAdapter extends RecyclerView.Adapter<AttendanceRecordAdapter.MyViewHolder> {
    private Context context;
    private List<AttendenceTable> mData;

    public AttendanceRecordAdapter(Context context, List<AttendenceTable> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public AttendanceRecordAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attndance_record_option, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceRecordAdapter.MyViewHolder holder, int position) {
        if (mData!=null){
            if (position > -1){
                if (position == 0){
                    holder.tvdate.setTextSize(14);
                    holder.tvdate.setTypeface(null, Typeface.BOLD);
                    holder.tvin.setTextSize(14);
                    holder.tvin.setTypeface(null, Typeface.BOLD);
                    holder.tvout.setTextSize(14);
                    holder.tvout.setTypeface(null, Typeface.BOLD);
                }else {
                    if (position % 2 == 0) {
                        holder.itemView.setBackgroundColor(context.getColor(R.color.medium_gray)); // Example: Set background color to blue for even rows
                    } else {
                        holder.itemView.setBackgroundColor(context.getColor(R.color.lightst_gray)); // Example: Set background color to white for odd rows
                    }
                    holder.tvdate.setText(mData.get(position-1).getDate());
                    holder.tvin.setText(mData.get(position-1).getClockin());
                    holder.tvout.setText(mData.get(position-1).getClockout());
                    holder.v1.setVisibility(View.INVISIBLE);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return mData.size()+1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvdate,tvin,tvout;
        private View v1;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvdate=itemView.findViewById(R.id.tvdate);
            tvin=itemView.findViewById(R.id.tvin);
            tvout=itemView.findViewById(R.id.tvout);
            v1=itemView.findViewById(R.id.v1);
        }
    }
}
