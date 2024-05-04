package vn.edu.tdc.rentaka.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.ChangeThePasswordLayoutBinding;

public class ChangeThePasswordActivity extends AppCompatActivity {
    private ChangeThePasswordLayoutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_the_password_layout);
        binding = ChangeThePasswordLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
