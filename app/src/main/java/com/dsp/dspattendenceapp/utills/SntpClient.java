package com.dsp.dspattendenceapp.utills;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SntpClient {
    private static final String NTP_SERVER = "time.google.com"; // NTP server
    private static final int NTP_PORT = 123;
    private static final int TIMEOUT = 30000;

    // Number of seconds between 1900 and 1970
    private static final long OFFSET_1900_TO_1970 = 2208988800L;
    private static final long NTP_TIMESTAMP_TO_MS = 1000L;

    private long ntpTime;
    private long ntpTimeReference;

    public boolean requestTime() {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT);

            InetAddress address = InetAddress.getByName(NTP_SERVER);
            byte[] buffer = new byte[48];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, NTP_PORT);

            // Set up the NTP request
            buffer[0] = 0x1B; // NTP request header

            socket.send(packet);
            socket.receive(packet);
            socket.close();

            // Extract the NTP time from the response
            long transmitTimeSeconds = ((buffer[43] & 0xFFL) | ((buffer[42] & 0xFFL) << 8)
                    | ((buffer[41] & 0xFFL) << 16) | ((buffer[40] & 0xFFL) << 24)) - OFFSET_1900_TO_1970;

            long transmitTimeFraction = ((buffer[47] & 0xFFL) | ((buffer[46] & 0xFFL) << 8)
                    | ((buffer[45] & 0xFFL) << 16) | ((buffer[44] & 0xFFL) << 24));

            // Convert the NTP time to milliseconds
            ntpTime = (transmitTimeSeconds * NTP_TIMESTAMP_TO_MS) + ((transmitTimeFraction * NTP_TIMESTAMP_TO_MS) >> 32);
            ntpTimeReference = System.currentTimeMillis();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public long getNtpTime() {
        return ntpTime + (System.currentTimeMillis() - ntpTimeReference);
    }
}
