package com.dsp.dspattendenceapp.services;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyWebChromeClient extends WebChromeClient {

    private ValueCallback<Uri[]> filePathCallback;
    private Activity activity;

    private Uri captureImageUri; // Define captureImageUri here
    private static final int REQUEST_SELECT_FILE = 100;

    public MyWebChromeClient(Activity activity) {
        this.activity = activity;
    }

    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        this.filePathCallback = filePathCallback;

        // Create an Intent to capture an image
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check if there's a camera activity to handle the intent
        if (captureIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                // Create a file to store the image
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Handle the error
            }

            if (photoFile != null) {
                captureImageUri = Uri.fromFile(photoFile);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, captureImageUri);
            }
        }

        // Create an Intent to pick an image from the gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");

        // Combine both intents into a chooser
        Intent chooserIntent = Intent.createChooser(galleryIntent, "Select or take a picture");
        if (captureIntent.resolveActivity(activity.getPackageManager()) != null) {
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{captureIntent});
        }

        // Start the activity to pick an image or capture a photo
        activity.startActivityForResult(chooserIntent, REQUEST_SELECT_FILE);
        return true;
    }

    // Handle the result
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri[] results = null;
        if (requestCode == REQUEST_SELECT_FILE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                // If data is null, this means the image was captured with the camera
                results = new Uri[]{captureImageUri};
            } else {
                // If data is not null, this means the user selected an image from the gallery
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        filePathCallback.onReceiveValue(results);
        filePathCallback = null;
    }

    // Helper method to create a file for the captured image
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

}
