package com.dsp.dspattendenceapp.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.interfaces.OnServerClick;
import com.dsp.dspattendenceapp.models.ServerInfoModel;

import java.util.ArrayList;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.MyViewHolder> {
    private ArrayList<ServerInfoModel> list;
    private Context context;
    private OnServerClick listener;

    public ServerAdapter(ArrayList<ServerInfoModel> list, Context context, OnServerClick listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_server_record, parent, false);
        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        if (list != null){
            holder.tvvname.setText(list.get(position).getHostname());
            if (!list.get(position).getType().equals("ICMP/PING")){
                holder.tvip.setText(list.get(position).getIp());
            }else {
                holder.tvip.setVisibility(View.GONE);
            }
            holder.tvvnamee.setText(list.get(position).getName());
            if (list.get(position).isStatus()){
                holder.tvstatus.setTextColor(ContextCompat.getColor(context,R.color.dark_green));
                holder.tvstatus.setText("Normal");
            }else {
                holder.tvstatus.setTextColor(ContextCompat.getColor(context, R.color.dark_red));
                holder.tvstatus.setText("Down");
            }

        }
        holder.relmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvvname,tvip,tvstatus,tvvnamee;
        RelativeLayout relmain;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvvname=itemView.findViewById(R.id.tvvname);
            tvip=itemView.findViewById(R.id.tvip);
            tvstatus=itemView.findViewById(R.id.tvstatus);
            relmain=itemView.findViewById(R.id.relmain);
            tvvnamee=itemView.findViewById(R.id.tvvnamee);
        }
    }
}
