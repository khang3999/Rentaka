package vn.edu.tdc.rentaka.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.ListCarSearchLayoutBinding;

public class ListCarSearchActivity extends AppCompatActivity {
    private ListCarSearchLayoutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ListCarSearchLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent.hasExtra("location")){
            binding.location.setText(intent.getStringExtra("location"));
        }
        if (intent.hasExtra("date")){
            binding.date.setText(intent.getStringExtra("date"));
        }
    }
}