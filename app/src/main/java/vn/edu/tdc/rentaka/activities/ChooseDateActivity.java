package vn.edu.tdc.rentaka.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.datepicker.MaterialDatePicker;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.ChooseDateLayoutBinding;

public class ChooseDateActivity extends AppCompatActivity {
    // Properties
    private ChooseDateLayoutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChooseDateLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.timeCheckin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}