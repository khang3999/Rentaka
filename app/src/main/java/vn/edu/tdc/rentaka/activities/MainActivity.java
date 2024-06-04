package vn.edu.tdc.rentaka.activities;
import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.rentaka.APIs.FirebaseAPI;
import vn.edu.tdc.rentaka.APIs.RealTimeAPI;
import vn.edu.tdc.rentaka.APIs.StorageAPI;
import vn.edu.tdc.rentaka.R;

import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;

import vn.edu.tdc.rentaka.databinding.MainLayoutBinding;
import vn.edu.tdc.rentaka.fragments.AbstractFragment;
import vn.edu.tdc.rentaka.fragments.HistoryFragment;
import vn.edu.tdc.rentaka.fragments.HomeFragment;
import vn.edu.tdc.rentaka.fragments.NewsFragment;
import vn.edu.tdc.rentaka.fragments.NotificationFragment;
import vn.edu.tdc.rentaka.fragments.PersonalProfileFragment;
import vn.edu.tdc.rentaka.fragments.SupportFragment;
import vn.edu.tdc.rentaka.models.Car;
import vn.edu.tdc.rentaka.models.City;
import vn.edu.tdc.rentaka.models.Date;
import vn.edu.tdc.rentaka.models.Discount;
import vn.edu.tdc.rentaka.models.Service;
import vn.edu.tdc.rentaka.models.Status;
import vn.edu.tdc.rentaka.receivers.SMSReceiver;
import vn.edu.tdc.rentaka.services.SMSHandlingService;


public class MainActivity extends AppCompatActivity {
    FirebaseAPI firebaseAPI = new FirebaseAPI();
    RealTimeAPI realTimeAPI = new RealTimeAPI();
    StorageAPI storageAPI = new StorageAPI();
    //Properties
    private AbstractFragment fragment;
    private int currentFragment = 0;
    private int preFragment = 0;
    private MainLayoutBinding binding;
    // doi tuong dung de dan fragment vao khung man hinh
    private FragmentTransaction transaction;


    private String ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private SMSReceiver smsReceiver;
    private Intent smsService;
    private IntentFilter intentFilter;
    private int REQUEST_CODE = 666;
    // Set color when click
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);



        //Khoi tao binding
        binding = MainLayoutBinding.inflate(getLayoutInflater());
        // Gán view cho binding
        setContentView(binding.getRoot());

        fragment = new HomeFragment();
        updateUI();

        binding.bottomMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                preFragment = currentFragment;
                if (menuItem.getItemId() == R.id.homeItemMenu){
                    currentFragment = 0;
                } else if(menuItem.getItemId() == R.id.notificationItemMenu){
                    currentFragment = 1;
                } else if(menuItem.getItemId() == R.id.historyItemMenu){
                    currentFragment = 2;
                } else if(menuItem.getItemId() == R.id.supportItemMenu){
                    currentFragment = 3;
                } else if(menuItem.getItemId() == R.id.profileItemMenu){
                    currentFragment = 4;
                }
                updateUI();
                return true;
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }



    private void updateUI() {
        // Set title
        // Neu da ton tai thi tai su dung
        if (getSupportFragmentManager().findFragmentByTag(currentFragment + "") != null) {
            fragment = (AbstractFragment) getSupportFragmentManager().findFragmentByTag(currentFragment + "");
        }
        // Neu chua ton tai thi tao moi fragment
        else {
            // Tao doi tuong fragment tuong ung
            if (currentFragment == 0) {
                fragment = new HomeFragment();
            } else if (currentFragment == 1) {
                fragment = new NotificationFragment();
            } else if (currentFragment == 2) {
                fragment = new HistoryFragment();
            } else if (currentFragment == 3) {
                fragment = new SupportFragment();
            } else if (currentFragment == 4) {
                fragment = new PersonalProfileFragment();
            }
        }

        // CHUAN BI CHO TRANSACTION
        //Lay doi tuong fragment transaction
        transaction = getSupportFragmentManager().beginTransaction();
        // Set annimation change fragment
        if (currentFragment > preFragment){
            // 2 tham số tuong ứng với 2 fragment: tham số 1 hieu ung cho man hinh hien thi, tham so 2 hieu ung cho man hinh exit
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        } else {
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        }

        // Do du lieu vao doi tuong va dan doi tuong fragment vao khung man hinh,
        // voi tham so dau tien la id cua khung chua fragment o layout )
        transaction.replace(R.id.fragmentContainer, fragment, currentFragment + "");
        // Dua fragment vao trong Stack neu chua ton tai
        if (getSupportFragmentManager().findFragmentByTag(currentFragment + "") == null) {
            transaction.addToBackStack(null);
        }
        // Yeu cau thuc hien transaction
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == storageAPI.PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            storageAPI.filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                storageAPI.filePath);
                ImageView imageView = (ImageView) findViewById(R.id.recipient_img);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }

    }

    private boolean checkPermission(String permission) {
        int check = checkSelfPermission(permission);
        return check == PackageManager.PERMISSION_DENIED ? false : true;
    }

    //Dang ky receiver
    private void implementPermission() {
        smsService = new Intent(this, SMSHandlingService.class);
        smsReceiver = new SMSReceiver();
        intentFilter = new IntentFilter(ACTION);
        registerReceiver(smsReceiver, intentFilter);
    }

    //Xu ly sau khi nguoi dung cap quyen
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && permissions.length == grantResults.length) {
            for (int check : grantResults) {
                if (check != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
            }
            implementPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // check required permission
        if (checkPermission(Manifest.permission.RECEIVE_SMS)) {
//            implementPermission();
        } else {
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_CODE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        startForegroundService(smsService);
    }



}

