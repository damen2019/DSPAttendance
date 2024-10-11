package com.dsp.dspattendenceapp.services;

import android.os.AsyncTask;
import android.util.Log;


import com.dsp.dspattendenceapp.models.ServerInfo;
import com.dsp.dspattendenceapp.utills.Utillity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerCheckerr extends AsyncTask<List<ServerInfo>, Void, Map<ServerInfo, Boolean>> {

    private static final String TAG = "ServerChecker";

    private ServerCheckListener listener;

    public ServerCheckerr(ServerCheckListener listener) {
        this.listener = listener;
    }

    @Override
    protected Map<ServerInfo, Boolean> doInBackground(List<ServerInfo>... serverLists) {
        if (serverLists == null || serverLists.length == 0) {
            return null;
        }

        List<ServerInfo> serverInfoList = serverLists[0];
        Map<ServerInfo, Boolean> serverStatusMap = new HashMap<>();

        for (ServerInfo serverInfo : serverInfoList) {
            String serverName = serverInfo.getName();
            String serverType = serverInfo.getType();
            String serverUrl = serverInfo.getUrl();
            if (!serverType.equals("ICMP/PING")){
                try {
                    URL url = new URL(serverUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000); // Set timeout to 5 seconds

                    InetAddress address = InetAddress.getByName(url.getHost());
                    serverInfo.setIp(address.getHostAddress());
                    serverInfo.setHostname(address.getHostName());

                    int responseCode = connection.getResponseCode();
                    serverStatusMap.put(serverInfo, responseCode == HttpURLConnection.HTTP_OK);

                } catch (IOException e) {
                    Log.e(TAG, "Error checking server status for server: " + serverName, e);
                    serverStatusMap.put(serverInfo, false);
                }
            }else {
                try {
                    InetAddress address = InetAddress.getByName(serverUrl); // Replace with your desired IP address or hostname
                    serverInfo.setIp(address.getHostAddress());
                    serverInfo.setHostname(address.getHostName());
                    boolean isServerUp = Utillity.isServerUp(address.getHostAddress());
                    serverStatusMap.put(serverInfo, isServerUp);
                } catch (IOException e) {
                    Log.e(TAG, "Error checking ICMP/PING server status", e);
                    serverStatusMap.put(serverInfo, false);
                }
            }

        }

        return serverStatusMap;
    }

    @Override
    protected void onPostExecute(Map<ServerInfo, Boolean> serverStatusMap) {
        if (listener != null) {
            listener.onServerCheckComplete(serverStatusMap);
        }
    }

    public interface ServerCheckListener {
        void onServerCheckComplete(Map<ServerInfo, Boolean> serverStatusMap);
    }
}
