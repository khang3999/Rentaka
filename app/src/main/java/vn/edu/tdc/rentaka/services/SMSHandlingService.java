package vn.edu.tdc.rentaka.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.edu.tdc.rentaka.activities.PaymentActivity;
import vn.edu.tdc.rentaka.databinding.PaymentLayoutBinding;

public class SMSHandlingService extends Service {

    private static final String TAG = "SMSHandlingService";
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
        String regex = "car rental: ([a-zA-Z0-9_-]+) (\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sms);

        if (matcher.find()) {
            String reservationId = matcher.group(1);
            String moneyAmount = matcher.group(2);
            Log.i(TAG, "ID: " + temp++);

            Log.i(TAG, "Reservation ID: " + reservationId);
            Log.i(TAG, "Money Amount: " + moneyAmount);

            // Handle the parsed data (e.g., save to database, update UI, etc.)


        } else {
            Log.e(TAG, "SMS format not recognized");
        }
    }
}
