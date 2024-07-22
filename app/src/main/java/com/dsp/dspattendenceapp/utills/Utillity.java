package com.dsp.dspattendenceapp.utills;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.global.Constants;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Utillity {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
    public static String getAddress(Context context, double latitude, double longitude){
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

        String aa= address+" "+city+" "+state+" "+country+" "+postalCode;
        return aa;
    }

    public static boolean isVpnActive(Context context) {
        boolean vpnactive = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network[] networks = connectivityManager.getAllNetworks();
            Log.d("TAG", "isVpnActive: "+networks.toString());
            if (networks != null) {
                for (Network network : networks) {
                    Network activeNetwork = connectivityManager.getActiveNetwork();
                    NetworkCapabilities networkCapabilitie= connectivityManager.getNetworkCapabilities(activeNetwork);
                    vpnactive= networkCapabilitie.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
//                    if (vpnactive) {
//                        Log.d("TAG", "isVpnActive: "+"Vpn connected");
//                        Toast.makeText(context, "Vpn connected", Toast.LENGTH_SHORT).show();
//                    }else {
//                        Log.d("TAG", "isVpnActive: "+"Vpn Not connected");
//
//                        Toast.makeText(context, "Vpn not connected", Toast.LENGTH_SHORT).show();
//                    }
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
        Log.d("TAG", "calculateDistanceInMeters: "+ results[0]);
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
                Preferences.deleteKey(activity,"userData");
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
            Log.d("TAG", "getAddressFromLatLng: "+e.getMessage());
            Toast.makeText(context, "Geocoder service is not available"+ e.getMessage(), Toast.LENGTH_SHORT).show();
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)
                    == PackageManager.PERMISSION_GRANTED) {

                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                boolean isAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected();
                if (!isAvailable)
                    Toast.makeText(context, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                return isAvailable;

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
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
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
        return Build.MODEL;
    }

    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getDeviceBrand() {
        return Build.BRAND;
    }

    public static String getDevice() {
        return Build.DEVICE;
    }

    public static String getDeviceProduct() {
        return Build.PRODUCT;
    }

    public static String getDeviceDisplay() {
        return Build.DISPLAY;
    }


}
