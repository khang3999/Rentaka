package vn.edu.tdc.rentaka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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

        // Set event for back button
        setSupportActionBar(binding.topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListCarSearchActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                // Thiết lập animation
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
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
        } else {
            binding.date.setText(intent.getStringExtra(""));
        }
    }
}