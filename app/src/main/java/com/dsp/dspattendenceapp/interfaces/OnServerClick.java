package com.dsp.dspattendenceapp.interfaces;

import com.dsp.dspattendenceapp.models.ServerInfoModel;
import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;

import java.util.List;

public interface OnServerClick {
    void onClick(ServerInfoModel serverInfoModel);
}
