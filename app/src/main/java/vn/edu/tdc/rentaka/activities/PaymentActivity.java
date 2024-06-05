package vn.edu.tdc.rentaka.activities;

import android.os.Bundle;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        paymentBinding = PaymentLayoutBinding.inflate(getLayoutInflater());
        setContentView(paymentBinding.getRoot());


    }

}