package com.dsp.dspattendenceapp.utills;

import static android.os.Build.*;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.dsp.dspattendenceapp.network.RetrofitClient.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;


import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.activites.LoginActivity;
import com.dsp.dspattendenceapp.activites.MainActivity;
import com.dsp.dspattendenceapp.global.Constants;
import com.dsp.dspattendenceapp.roomdb.dao.AttendenceLogDao;
import com.dsp.dspattendenceapp.roomdb.databases.MyDatabase;
import com.dsp.dspattendenceapp.services.UploadUnSyncAttendenceService;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import com.google.android.gms.location.LocationRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.Arrays;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Utillity {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public static String getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        String aa = address + " " + city + " " + state + " " + country + " " + postalCode;
        return aa;
    }

    public static boolean isVpnActive(Context context) {
        boolean vpnactive = false;
        if (VERSION.SDK_INT >= VERSION_CODES.M) {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network[] networks = connectivityManager.getAllNetworks();
            Log.d("TAG", "isVpnActive: " + networks.toString());
            if (networks != null) {
                for (Network network : networks) {
                    Network activeNetwork = connectivityManager.getActiveNetwork();
                    NetworkCapabilities networkCapabilitie = connectivityManager.getNetworkCapabilities(activeNetwork);
                    vpnactive = networkCapabilitie.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
                }
            }
        }
        return vpnactive;
    }


    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
        return df.format(c);
    }

    public static String getCurrentDateWithOutDay() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        return df.format(c);
    }

    public static float calculateDistanceInMeters(double startLat, double startLng, double endLat, double endLng) {
        float[] results = new float[1];
        Location.distanceBetween(startLat, startLng, endLat, endLng, results);
        Log.d("TAG", "calculateDistanceInMeters: " + results[0]);
        return results[0];
    }

    public static String formatDecimal(float number) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(number);
    }

    public static void openLogoutDialog(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.complete_order_dialog);
        dialog.setCanceledOnTouchOutside(false);

        TextView btn_ok = dialog.findViewById(R.id.yes);
        TextView btn_no = dialog.findViewById(R.id.no);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                activity.finish();
                Preferences.deleteKey(activity, "userData");
                Utillity.startActivity(activity, LoginActivity.class, Constants.FINISH_ACTIVITY);
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dailog);
        }

    }

    public static void openAlertDialog(Activity activity, String message) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.alert_order_dialog);
        dialog.setCanceledOnTouchOutside(false);

        TextView btn_ok = dialog.findViewById(R.id.yes);
        TextView tconfirm = dialog.findViewById(R.id.tconfirm);
        tconfirm.setText(message);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dailog);
        }

    }

    public static void openErrorDialog(Activity activity, String message) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.error_order_dialog);
        dialog.setCanceledOnTouchOutside(false);

        TextView btn_ok = dialog.findViewById(R.id.yes);
        TextView tconfirm = dialog.findViewById(R.id.tconfirm);
        tconfirm.setText(message);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_dailog);
        }

    }

    public static void startActivity(Activity context, Class<?> serviceClass, int status, Bundle bundle) {

        Intent intent = new Intent(context, serviceClass);
        intent.putExtras(bundle);

        if (status == Constants.FINISH_ACTIVITY) {
            context.startActivity(intent);
            context.finish();
        } else if (status == Constants.START_ACTIVITY) {
            context.startActivity(intent);
        } else if (status == Constants.CLEAR_BACK_STACK) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }

//        context.overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }

    public static void startActivity(Activity context, Class<?> serviceClass, int status) {

        Intent intent = new Intent(context, serviceClass);

        if (status == Constants.FINISH_ACTIVITY) {
            context.startActivity(intent);
            context.finish();
        } else if (status == Constants.START_ACTIVITY) {
            context.startActivity(intent);
        } else if (status == Constants.CLEAR_BACK_STACK) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }


//        context.overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }


    public static String getInitials(String fullName) {
        // Split the full name into individual words
        String[] words = fullName.split("\\s+");

        StringBuilder initials = new StringBuilder();

        // Extract the first character of each word and append to initials
        for (String word : words) {
            if (!word.isEmpty()) {
                initials.append(word.charAt(0));
            }
        }

        return initials.toString().toUpperCase(); // Convert to uppercase if needed
    }


    public static String getAddressFromLatLng(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressStringBuilder = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressStringBuilder.append(address.getAddressLine(i)).append("\n");
                }
                return addressStringBuilder.toString();
            } else {
                return "No address found for the given coordinates";
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("TAG", "getAddressFromLatLng: " + e.getMessage());
            Toast.makeText(context, "Geocoder service is not available" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static String getCurrentTime() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // Format the current time
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute, second);
    }

    public static int generateRandomNumber() {
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }

    public static boolean isNetworkAvailable(Context context) {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                    == PackageManager.PERMISSION_GRANTED) {

                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                boolean isAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected();
                if (!isAvailable) {
                    return isAvailable;
                } else {
                    return isAvailable;
                }
            }
        }

        return false;
    }

    public static String convertToString(Object obj) {
        return new Gson().toJson(obj);
    }

    public static Object convertObject(String objString, Class<?> serviceClass) {
        Gson gson = new Gson();
        return gson.fromJson(objString, serviceClass);
    }

    public static String getDeviceName() {
        String manufacturer = MANUFACTURER;
        String model = MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isLowerCase(first)) {
            return Character.toUpperCase(first) + s.substring(1);
        } else {
            return s;
        }
    }

    public static String getDeviceModel() {
        return MODEL;
    }

    public static String getDeviceManufacturer() {
        return MANUFACTURER;
    }

    public static String getDeviceBrand() {
        return BRAND;
    }

    public static String getDevice() {
        return DEVICE;
    }

    @RequiresApi(api = VERSION_CODES.O)
    public static String getSerial() {
        return Build.getSerial();
    }


    public static String getDeviceProduct() {
        return PRODUCT;
    }

    public static String getDeviceDisplay() {
        return DISPLAY;
    }

    @RequiresApi(api = VERSION_CODES.O)
    public static String convertDateFormat(String inputDate) {
        // Parse the input date string into a LocalDate object
        LocalDate date = LocalDate.parse(inputDate.substring(0, 10)); // Extracting "2020-05-20"

        // Create a formatter for the desired output format
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy");

        // Format the LocalDate object using the formatter
        return date.format(outputFormatter);
    }

    @RequiresApi(api = VERSION_CODES.O)
    public static String formatTime(String inputDateTime) {
        // Parse the input datetime string into a LocalDateTime object
        LocalDateTime dateTime = LocalDateTime.parse(inputDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // Create a formatter for the desired output format
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Format the LocalDateTime object using the formatter
        return dateTime.format(outputFormatter);
    }

    @RequiresApi(api = VERSION_CODES.O)
    public static String getCurrentDateFormatted() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Format the current date
        return currentDate.format(formatter);
    }

    @RequiresApi(api = VERSION_CODES.O)
    public static void clearPreferencesIfDateChanged(Context context) {
        String lastSavedDate = "";
        if (Preferences.getSharedPrefValue(context, "datevalue") != null) {
            lastSavedDate = Preferences.getSharedPrefValue(context, "datevalue");
        }

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        if (!currentDate.equals(lastSavedDate)) {
            Preferences.saveSharedPrefValue(context, "datevalue", Utillity.getCurrentDateFormatted());
            Preferences.saveSharedPrefValue(context, "status", "clockout");
        }

    }


    @RequiresApi(api = VERSION_CODES.O)
    public static String getCurrentDateTimeFormatted() {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Define the desired format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

        // Format the current date and time
        return currentDateTime.format(formatter);
    }


    public static int getCurrentDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static boolean isLocationFromMockProvider(Location location) {
        if (location != null) {
            Boolean isMock = location.isFromMockProvider();
            if (isMock != null) return isMock;
            else return false;
        } else return false;
    }

    public static void request_location(final Activity activity) {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          return;
        }
        LocationServices.getFusedLocationProviderClient(activity).requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }

    public static boolean Check_USer_Location_PERMSIISSION(final Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            }
            return false;
        } else {
            return true;
        }
    }

    public static boolean isServerUp(String ipAddress) {
        try {
            Process process = Runtime.getRuntime().exec("ping -n 1 " + ipAddress); // Linux/Mac
            // For Windows, use: Runtime.getRuntime().exec("ping -n 1 " + ipAddress);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("1 packets transmitted, 1 received")) {
                    return true;
                }
            }
            reader.close();

            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String checkProtocol(String urlString) {
        try {
            URL url = new URL(urlString);
            String protocol = url.getProtocol();
            return protocol;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isDeviceRooted() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    public static int getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

//    public static long getNetworkTime() {
//        SntpClient sntpClient = new SntpClient();
//        if (sntpClient.requestTime("time.google.com", 30000)) {
//            return sntpClient.getNtpTime();
//        } else {
//            return System.currentTimeMillis(); // Fallback to device time
//        }
//    }

    public static long getNetworkTime() {
        SntpClient sntpClient = new SntpClient();
        if (sntpClient.requestTime()) {
            return sntpClient.getNtpTime();
        } else {
            return System.currentTimeMillis(); // Fallback to device time if NTP fails
        }
    }

    public static String formatTimee(long timeInMillis) {
        // Create a SimpleDateFormat instance with the desired format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());

        // Convert the long time to a Date object
        Date date = new Date(timeInMillis);

        // Format the date into the desired string format
        return sdf.format(date);
    }

    public static String getCurrentDateTimePlusMinutes(int minutesToAdd) {
        // Create a date format
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy HH:mm");

        // Get current date and time
        Date now = new Date();

        // Add the specified minutes to the current time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE, minutesToAdd);

        // Return the new time after adding the minutes
        return sdf.format(calendar.getTime());
    }


    public static String calculateMD5Hash(String input) {
        try {
            // Convert the input string to UTF-16 (Java uses UTF-16 for Strings)
            byte[] data = input.getBytes(StandardCharsets.UTF_16LE); // Equivalent to UTF-16 in C#

            // Create an instance of MessageDigest for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data);
            byte[] hashData = md.digest();

            // Convert the byte array to a hexadecimal string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashData) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


//
//    public static String saveDAte() {
////        TimeFetcher timeFetcher = new TimeFetcher();
//         String timeDateClick;
//
//        // Fetch the network time asynchronously
//        TimeFetcher.getNetworkTime(new TimeFetcher.OnTimeFetchedListener() {
//            @Override
//            public void onTimeFetched(long networkTime) {
//                // Use the networkTime here
//                timeDateClick = formatTimee(networkTime);
//            }
//        });
//    }

    public static String getNextDayDate() {
        // Create a date format with the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

        // Get the current date
        Calendar calendar = Calendar.getInstance();

        // Add one day to the current date
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        // Get the new date
        Date nextDay = calendar.getTime();

        // Return the formatted date
        return dateFormat.format(nextDay);
    }

    public static String encryptUrlParams(String empid, String pass, String dt) {
        String ENCRYPTION_KEY = "dsp2024";
        try {
            // Concatenate the parameters
            String clearText = empid + "►" + pass + "►" + dt;

            // Convert clear text and key to byte arrays
            byte[] clearBytes = clearText.getBytes(StandardCharsets.UTF_8);
            byte[] keyBytes = ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8);

            // Perform XOR encryption
            for (int i = 0; i < clearBytes.length; i++) {
                clearBytes[i] ^= keyBytes[i % keyBytes.length]; // XOR each byte with the key
            }

            // Base64 encode the encrypted bytes
            String base64Encoded = Base64.encodeToString(clearBytes, Base64.NO_WRAP);

            // URL encode the result
            return URLEncoder.encode(base64Encoded, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptUrlParams(String encryptedText) {
        String ENCRYPTION_KEY = "dsp2024";
        try {
            // URL decode the input
            String urlDecodedText = URLDecoder.decode(encryptedText, "UTF-8");

            // Base64 decode the input
            byte[] cipherBytes = Base64.decode(urlDecodedText, Base64.NO_WRAP);
            byte[] keyBytes = ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8);

            // Perform XOR decryption
            for (int i = 0; i < cipherBytes.length; i++) {
                cipherBytes[i] ^= keyBytes[i % keyBytes.length];
            }

            return new String(cipherBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void clearAttLogIfNewMonth(Context context, AttendenceLogDao db) {
        // Get current month and year
        Calendar calendar = Calendar.getInstance();
        int currentMonth = calendar.get(Calendar.MONTH);  // Months are 0-based, January = 0
        int currentYear = calendar.get(Calendar.YEAR);

        SharedPreferences preferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        int lastClearedMonth = preferences.getInt("last_cleared_month", -1);
        int lastClearedYear = preferences.getInt("last_cleared_year", -1);

        // Check if it's a new month or a new year
        if (currentMonth != lastClearedMonth || currentYear != lastClearedYear) {
            // Clear the database
            db.clearTable();

            // Save the new month and year in SharedPreferences
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("last_cleared_month", currentMonth);
            editor.putInt("last_cleared_year", currentYear);
            editor.apply();

            Log.d("DatabaseClear", "Database cleared for new month");
        }
    }






}



