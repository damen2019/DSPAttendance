package com.dsp.dspattendenceapp.activites;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.databinding.ActivityMainBinding;
import com.dsp.dspattendenceapp.databinding.ActivityWebViewBinding;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.utills.EncryptionUtils;
import com.dsp.dspattendenceapp.utills.Preferences;
import com.dsp.dspattendenceapp.utills.Utillity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WebViewActivity extends BaseActivity {
    ActivityWebViewBinding binding;
    private String passwordMd5,empid,userdetailstr;
    private ValueCallback<Uri[]>upload;
    private Uri imageUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        showWaitingDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissWaitingDialog();
            }
        },3000);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        showWebView();
        initListener();

    }

    private void showWebView() {
        try {
            UserTable userTable= Preferences.getUserDetails(getApplicationContext());
            passwordMd5= Utillity.calculateMD5Hash(userTable.getUserPass());
            empid=  userTable.getEmpID();
            userdetailstr=empid+":"+passwordMd5;
            Log.d("TAG", "onKEY: "+userdetailstr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        WebSettings webSettings = binding.myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        binding.myWebView.loadUrl("https://s1test.damensp.com/MIS/Login.aspx?k="+userdetailstr);

        binding.myWebView.setWebViewClient(new WebViewClient());
        binding.myWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
                upload = filePathCallback;

                // Create an Intent to capture an image using the camera
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


                try {
                    // Create a file to store the image
                    File photoFile = createImageFile();
                    Log.d("TAG", "onShowFileChooser: "+photoFile.getAbsolutePath());
                    if (photoFile != null) {
                        imageUri = Uri.fromFile(photoFile);
                        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // Create an Intent to pick an image from the gallery
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                galleryIntent.setType("image/*");

                // Combine both intents into a chooser
                Intent chooserIntent = Intent.createChooser(galleryIntent, "Select or take a picture");
                if (captureIntent.resolveActivity(getPackageManager()) != null) {
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{captureIntent});
                }

                startActivityForResult(chooserIntent,202);
                return true;
            }
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });

    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initListener() {
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 202 && resultCode == RESULT_OK) {
            Uri[] results = null;

            if (data == null) {
                // If no data, this means the image was captured with the camera
                results = new Uri[]{imageUri};
                Log.d("TAG", "onActivityResult: "+ results);
            } else {
                // If data is not null, this means the user selected an image from the gallery
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }

            if (upload != null) {
                upload.onReceiveValue(results);
                upload = null;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted
            } else {
                // Permission was denied
                Toast.makeText(this, "Location permission is required for this feature.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}