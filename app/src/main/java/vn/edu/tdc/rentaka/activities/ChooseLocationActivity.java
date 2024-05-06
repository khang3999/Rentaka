package vn.edu.tdc.rentaka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.ChooseLocationLayoutBinding;
import vn.edu.tdc.rentaka.fragments.HomeFragment;

public class ChooseLocationActivity extends AppCompatActivity {
    private ChooseLocationLayoutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khoi tao binding
        binding = ChooseLocationLayoutBinding.inflate(getLayoutInflater());
        // Gán view cho binding
        setContentView(binding.getRoot());

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseLocationActivity.this, MainActivity.class);
                intent.putExtra("city", binding.edtChooseLocation.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                Log.d("goi", "onClick: "+intent.getStringExtra("city"));
                startActivity(intent);
            }
        });
    }
}