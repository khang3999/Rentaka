package vn.edu.tdc.rentaka.services;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.edu.tdc.rentaka.APIs.RealTimeAPI;
import vn.edu.tdc.rentaka.activities.PaymentActivity;
import vn.edu.tdc.rentaka.databinding.PaymentLayoutBinding;

public class SMSHandlingService extends Service {

    private static final String TAG = "SMSHandlingService";
    private RealTimeAPI realTimeAPI = new RealTimeAPI();
    int temp = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String sms = intent.getStringExtra("sms");
        if (sms != null) {
            Log.i(TAG, "Received SMS: " + sms);
            handleSMS(sms);
        }
        return START_NOT_STICKY;
    }

    private void handleSMS(String sms) {
        // Define the regex pattern to match the SMS format
        String regex = "car rental: ([a-zA-Z0-9_-]+) ([a-zA-Z0-9_-]+) (\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sms);

        if (matcher.find()) {
            String carId = matcher.group(1);
            String billId  = matcher.group(2);
            String moneyAmount = matcher.group(3);
            Log.i(TAG, "ID: " + temp++);

            Log.i(TAG, "Bill ID: " + billId);
            Log.i(TAG, "Car ID: " + carId);
            Log.i(TAG, "Money Amount: " + moneyAmount);

            // Handle the parsed data (e.g., save to database, update UI, etc.)
            try {
                realTimeAPI.updateBillStatusWhenCustomerPaid(billId,carId,"900000");
            }catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "SMS format not recognized");
        }
    }
}
