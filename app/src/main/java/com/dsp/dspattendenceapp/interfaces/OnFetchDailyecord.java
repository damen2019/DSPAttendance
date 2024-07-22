package com.dsp.dspattendenceapp.interfaces;

import com.dsp.dspattendenceapp.roomdb.table.AttendenceTable;

import java.util.List;

public interface OnFetchDailyecord {
    void onFetch(List<AttendenceTable> list);
}
