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
import com.dsp.dspattendenceapp.models.ProvidentFoundHistoryModel;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceLogTable;
import com.dsp.dspattendenceapp.utills.Utillity;

import java.util.List;

public class ProvidentFoundHistoryAdapter extends RecyclerView.Adapter<ProvidentFoundHistoryAdapter.MyViewHolder> {

    private Context context;
    private List<ProvidentFoundHistoryModel> mData;

    public ProvidentFoundHistoryAdapter(Context context, List<ProvidentFoundHistoryModel> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ProvidentFoundHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pf_history_record_option, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProvidentFoundHistoryAdapter.MyViewHolder holder, int position) {
        if (mData!=null){
            if (position > -1){
                if (position == 0){
                    holder.tvyearmonth.setTextSize(10);
                    holder.tvyearmonth.setText("Year-Month");
                    holder.tvyearmonth.setTypeface(null, Typeface.BOLD);
                    holder.tvdescription.setText("Description");
                    holder.tvdescription.setTextSize(10);
                    holder.tvdescription.setTypeface(null, Typeface.BOLD);
                    holder.tvamount.setTextSize(10);
                    holder.tvamount.setText("Amount");
                    holder.tvamount.setTypeface(null, Typeface.BOLD);
                    holder.tvbalance.setTextSize(10);
                    holder.tvbalance.setText("Balance");
                    holder.tvbalance.setTypeface(null, Typeface.BOLD);

                }else {
                    if (position % 2 == 0) {
                        holder.itemView.setBackgroundColor(context.getColor(R.color.medium_gray)); // Example: Set background color to blue for even rows
                    } else {
                        holder.itemView.setBackgroundColor(context.getColor(R.color.lightst_gray)); // Example: Set background color to white for odd rows
                    }
                    holder.tvyearmonth.setText(mData.get(position-1).getYear_Month());

                    holder.tvdescription.setText(mData.get(position - 1).getDescription());
                    holder.tvamount.setText(String.valueOf(mData.get(position-1).getAmount()));
                    holder.tvbalance.setText(String.valueOf(mData.get(position-1).getBalance()));

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
        private TextView tvyearmonth,tvdescription,tvamount,tvbalance;
        private View v1;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvyearmonth=itemView.findViewById(R.id.tvyearmonth);
            tvdescription=itemView.findViewById(R.id.tvdescription);
            tvamount=itemView.findViewById(R.id.tvamount);
            tvbalance=itemView.findViewById(R.id.tvbalance);
            v1=itemView.findViewById(R.id.v1);
        }
    }
}
