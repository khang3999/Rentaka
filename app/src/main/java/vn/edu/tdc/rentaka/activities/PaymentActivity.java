package vn.edu.tdc.rentaka.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.PaymentLayoutBinding;
import vn.edu.tdc.rentaka.databinding.RentalDetailLayoutBinding;

public class PaymentActivity extends AppCompatActivity {

    private PaymentLayoutBinding paymentBinding;
    public boolean isButtonEnabled = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        paymentBinding = PaymentLayoutBinding.inflate(getLayoutInflater());
        setContentView(paymentBinding.getRoot());

        paymentBinding.btnConfirmRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isButtonEnabled) {
                    new AlertDialog.Builder(PaymentActivity.this)
                            .setTitle("Button Disabled")
                            .setMessage("This button is currently disabled.")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                    return;

                } else {
                    new AlertDialog.Builder(PaymentActivity.this)
                            .setTitle("Button Enable")
                            .setMessage("This button is currently disabled.")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                    return;
                }
            }
        });

    }

}