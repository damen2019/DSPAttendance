package com.dsp.dspattendenceapp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceLogTable;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;
import com.dsp.dspattendenceapp.utills.Utillity;

import java.util.List;

public class MonthlyAttendenceRecordAdapter extends RecyclerView.Adapter<MonthlyAttendenceRecordAdapter.MyViewHolder> {
    private Context context;
    private List<AttendenceTable> mData;

    public MonthlyAttendenceRecordAdapter(Context context, List<AttendenceTable> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MonthlyAttendenceRecordAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attndance_record_option, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MonthlyAttendenceRecordAdapter.MyViewHolder holder, int position) {
        if (mData!=null){
            if (position > -1){
                if (position == 0){
                    holder.tvdate.setTextSize(14);
                    holder.tvdate.setText("Date");
                    holder.tvdate.setTypeface(null, Typeface.BOLD);
                    holder.tvin.setText("IN");
                    holder.tvin.setTextSize(14);
                    holder.tvin.setTypeface(null, Typeface.BOLD);
                    holder.tvout.setTextSize(14);
                    holder.tvout.setText("OUT");
                    holder.tvout.setTypeface(null, Typeface.BOLD);
                }else {
                    if (position % 2 == 0) {
                        holder.itemView.setBackgroundColor(context.getColor(R.color.medium_gray)); // Example: Set background color to blue for even rows
                    } else {
                        holder.itemView.setBackgroundColor(context.getColor(R.color.lightst_gray)); // Example: Set background color to white for odd rows
                    }
                    holder.tvdate.setText(Utillity.convertDateFormat(mData.get(position-1).getDTAttend()));
                    if (mData.get(position - 1).getOUT()!= null && !mData.get(position - 1).getOUT().isEmpty()){
                        holder.tvout.setText(Utillity.formatTime(mData.get(position - 1).getOUT()));
                    }else {
                        holder.tvout.setText("");
                    }

                    holder.tvin.setText(Utillity.formatTime(mData.get(position - 1).getIN()));

                    holder.v1.setVisibility(View.INVISIBLE);
                }
            }else {
                Toast.makeText(context, mData.get(position)+"", Toast.LENGTH_SHORT).show();
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
