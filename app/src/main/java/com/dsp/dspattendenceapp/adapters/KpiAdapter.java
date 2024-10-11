package com.dsp.dspattendenceapp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.models.Kpimodel;
import com.dsp.dspattendenceapp.models.PamsModel;

import java.util.List;

public class KpiAdapter extends RecyclerView.Adapter<KpiAdapter.MyViewHolder> {
    private Context context;
    private List<Kpimodel> mData;

    public KpiAdapter(Context context, List<Kpimodel> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public KpiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kpi_record_option, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KpiAdapter.MyViewHolder holder, int position) {
        if (mData!=null){
            if (position > -1){
                if (position == 0){
                    holder.tvkpi.setText("KPI #");
                    holder.tvkpi.setTextSize(10);
                    holder.tvkpi.setTypeface(null, Typeface.BOLD);
                    holder.tvkpides.setTextSize(10);
                    holder.tvkpides.setText("KPI Description");
                    holder.tvkpides.setTypeface(null, Typeface.BOLD);
                    holder.tvach.setTextSize(10);
                    holder.tvach.setText("Achieved");
                    holder.tvach.setTypeface(null, Typeface.BOLD);
                    holder.tvcomments.setTextSize(10);
                    holder.tvcomments.setText("Comments");
                    holder.tvcomments.setTypeface(null, Typeface.BOLD);

                }else {
                    if (position % 2 == 0) {
                        holder.itemView.setBackgroundColor(context.getColor(R.color.medium_gray)); // Example: Set background color to blue for even rows
                    } else {
                        holder.itemView.setBackgroundColor(context.getColor(R.color.lightst_gray)); // Example: Set background color to white for odd rows
                    }
                    holder.tvkpi.setText(mData.get(position-1).getKpi());

                    holder.tvkpides.setText(mData.get(position - 1).getKpides());
                    holder.tvach.setText(mData.get(position-1).getAchieved());
                    holder.tvcomments.setText(mData.get(position-1).getComment());

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
        private TextView tvkpi,tvkpides,tvach,tvcomments;
        private View v1;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvkpi=itemView.findViewById(R.id.tvkpi);
            tvkpides=itemView.findViewById(R.id.tvkpides);
            tvach=itemView.findViewById(R.id.tvach);
            tvcomments=itemView.findViewById(R.id.tvcomments);
            v1=itemView.findViewById(R.id.v1);
        }
    }
}
