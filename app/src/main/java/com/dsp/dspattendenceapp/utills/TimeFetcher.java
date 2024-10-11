package com.dsp.dspattendenceapp.utills;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimeFetcher {


    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public void getNetworkTime(OnTimeFetchedListener listener) {
        executorService.execute(() -> {
            SntpClient sntpClient = new SntpClient();
            final long networkTime;
            if (sntpClient.requestTime()) {
                networkTime = sntpClient.getNtpTime();
            } else {
                networkTime = System.currentTimeMillis(); // Fallback to device time if NTP fails
            }

            // Run the callback on the main thread
            mainHandler.post(() -> listener.onTimeFetched(networkTime));
        });
    }

    public interface OnTimeFetchedListener {
        void onTimeFetched(long networkTime);
    }

}
