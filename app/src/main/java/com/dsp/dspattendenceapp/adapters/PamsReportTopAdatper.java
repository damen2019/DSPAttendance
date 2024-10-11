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
import com.dsp.dspattendenceapp.models.PamsModel;
import com.dsp.dspattendenceapp.models.ProvidentFoundHistoryModel;

import java.util.List;

public class PamsReportTopAdatper extends RecyclerView.Adapter<PamsReportTopAdatper.MyViewHolder> {
    private Context context;
    private List<PamsModel> mData;

    public PamsReportTopAdatper(Context context, List<PamsModel> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public PamsReportTopAdatper.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pams_record_option, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PamsReportTopAdatper.MyViewHolder holder, int position) {
        if (mData!=null){
            if (position > -1){
                if (position == 0){
                    holder.tvselect.setVisibility(View.INVISIBLE);
                    holder.tvpositionid.setText("Position ID");
                    holder.tvpositionid.setTextSize(10);
                    holder.tvpositionid.setTypeface(null, Typeface.BOLD);
                    holder.tvappraisaldate.setTextSize(10);
                    holder.tvappraisaldate.setText("Appraisal Date");
                    holder.tvappraisaldate.setTypeface(null, Typeface.BOLD);
                    holder.tvapprasialperiod.setTextSize(10);
                    holder.tvapprasialperiod.setText("Appraisal Period");
                    holder.tvapprasialperiod.setTypeface(null, Typeface.BOLD);

                }else {
                    if (position % 2 == 0) {
                        holder.itemView.setBackgroundColor(context.getColor(R.color.medium_gray)); // Example: Set background color to blue for even rows
                    } else {
                        holder.itemView.setBackgroundColor(context.getColor(R.color.lightst_gray)); // Example: Set background color to white for odd rows
                    }
                    holder.tvpositionid.setText(mData.get(position-1).getPositionId());

                    holder.tvappraisaldate.setText(mData.get(position - 1).getAppraisalDate());
                    holder.tvapprasialperiod.setText(mData.get(position-1).getAppraisalPeriod());

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
        private TextView tvselect,tvpositionid,tvappraisaldate,tvapprasialperiod;
        private View v1;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvselect=itemView.findViewById(R.id.tvselect);
            tvpositionid=itemView.findViewById(R.id.tvpositionid);
            tvappraisaldate=itemView.findViewById(R.id.tvappraisaldate);
            tvapprasialperiod=itemView.findViewById(R.id.tvapprasialperiod);
            v1=itemView.findViewById(R.id.v1);
        }
    }
}
