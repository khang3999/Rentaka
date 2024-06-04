package vn.edu.tdc.rentaka.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import vn.edu.tdc.rentaka.APIs.RealTimeAPI;
import vn.edu.tdc.rentaka.databinding.SupportLayoutBinding;

public class SupportFragment extends AbstractFragment{
    private SupportLayoutBinding binding;
    private static final int REQUEST_CALL_PERMISSION = 1;

    private RealTimeAPI realTimeAPI = new RealTimeAPI();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SupportLayoutBinding.inflate(getLayoutInflater());
        View fragment = null;
        fragment = binding.getRoot();

        Button callButton = binding.callButton;
        Button chatButton = binding.messageButton;

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    makePhoneCall();
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openChatBox();
            }
        });
        return fragment;
    }

    private void makePhoneCall() {
        String phoneNumber = "tel:1234567890"; // Replace with your desired phone number
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            startCall(phoneNumber);
        }
    }

    private void startCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(phoneNumber));
        startActivity(callIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void openChatBox() {
        String phoneNumber = "1234567890"; // Replace with your desired phone number
        Uri smsUri = Uri.parse("smsto:" + phoneNumber);
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
        // Optional: Pre-fill the message content
        smsIntent.putExtra("sms_body", "Hello, I need support regarding...");
        startActivity(smsIntent);
    }

}
