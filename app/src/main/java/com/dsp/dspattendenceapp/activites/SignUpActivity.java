package com.dsp.dspattendenceapp.activites;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dsp.dspattendenceapp.R;
import com.dsp.dspattendenceapp.databinding.ActivitySignUpBinding;
import com.dsp.dspattendenceapp.roomdb.dao.UserDao;
import com.dsp.dspattendenceapp.roomdb.databases.MyDatabase;
import com.dsp.dspattendenceapp.roomdb.table.UserTable;
import com.dsp.dspattendenceapp.utills.Utillity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private Uri uri;

    MyDatabase myDb;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initView();
        initDb();
        initClickListener();
    }

    private void initView() {
        binding.etuserid.setText("101"+ Utillity.generateRandomNumber());
    }

    private void initDb() {
        myDb= Room.databaseBuilder(getApplicationContext(),MyDatabase.class,"usertable")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        userDao=myDb.getUserDao();
    }
    private void initClickListener() {
        binding.logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             openDialog();
            }
        });
        binding.tvadduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validation()){
//                    UserTable userTable=new UserTable(binding.etusername.getText().toString().trim(),binding.etuserlastname.getText().toString().trim(),binding.etusermail.getText().toString().trim(),binding.etuserdesigination.getText().toString().trim(),binding.etusernic.getText().toString().trim(),binding.etusernumber.getText().toString().trim(),binding.etuserid.getText().toString().trim(),binding.etpassword.getText().toString().trim());
//                    userDao.insertUser(userTable);
                    finish();
                }
            }
        });
    }

    private boolean Validation() {
        if (binding.etusername.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter user name!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.etuserlastname.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter user lastname!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.etusermail.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter user email!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.etuserdesigination.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter user designation!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.etusernic.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter user cnic!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.etuserid.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter user id!", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.etpassword.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please enter user password!", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    private void openDialog() {
                final BottomSheetDialog dialog = new BottomSheetDialog(SignUpActivity.this, R.style.AppBottomSheetDialogTheme);
        dialog.setContentView(R.layout.bottomsheet_profile);
        dialog.show();

        dialog.getWindow()
                .findViewById(R.id.design_bottom_sheet)
                .setBackgroundResource(android.R.color.transparent);

        Button cancel = dialog.findViewById(R.id.cancel);
        TextView tv_gallery = dialog.findViewById(R.id.tv_gallery);
        TextView tv_camera = dialog.findViewById(R.id.tv_camera);

        dialog.setCancelable(true);


        tv_gallery.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            dialog.dismiss();
            ImagePicker.Companion.with(SignUpActivity.this).galleryOnly()
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                    .start(1010);
        });

        tv_camera.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            dialog.dismiss();
            ImagePicker.Companion.with(SignUpActivity.this)
                    .cameraOnly()
                    .compress(1024)   //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080) //Final image resolution will be less than 1080 x 1080(Optional)
                    .start();
        });

        cancel.setOnClickListener(v -> dialog.dismiss());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            Picasso.with(getApplicationContext())
                    .load(uri)
                    .into((ImageView) binding.logo, new Callback() {
                        @Override
                        public void onSuccess() {
                            // Image loaded successfully, hide progress bar
                            binding.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            // Error occurred while loading image, hide progress bar
                            binding.progressBar.setVisibility(View.GONE);
                        }
                    });

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}