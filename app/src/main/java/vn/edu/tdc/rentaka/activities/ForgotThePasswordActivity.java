package vn.edu.tdc.rentaka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.ForgotThePasswordLayoutBinding;

public class ForgotThePasswordActivity extends AppCompatActivity {

    boolean isEmailValid = false;
    private ForgotThePasswordLayoutBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_the_password_layout);
        binding = ForgotThePasswordLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get email from LoginActivity
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        binding.editTextEmail.setText(email);

        // Kiểm tra
        validateEmail(email);

        // Cập nhật trạng thái nút
        updateButtonState();

        // Out
        binding.leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Kiểm tra
                validateEmail(s.toString());
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmailValid) {
                    sendPasswordResetEmail(binding.editTextEmail.getText().toString());
                } else {
                    Toast.makeText(ForgotThePasswordActivity.this, "Gửi email thất bại", Toast.LENGTH_SHORT).show();
                    if (binding.editTextEmail.getText().toString().isEmpty()) {
                        binding.textInputLayoutEmail.setError("Chưa nhập email");
                    }
                }
            }
        });
    }
    //Ham gui email
    private void sendPasswordResetEmail(String email) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ForgotThePasswordActivity.this, "Email khôi phục mật khẩu đã được gửi", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotThePasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ForgotThePasswordActivity.this, "Lỗi khi gửi email khôi phục mật khẩu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Regex email
    public void validateEmail(String email) {
        // So sánh regex email
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (email.matches(regex)) {
            isEmailValid = true;
            binding.textInputLayoutEmail.setError(null);
        } else {
            isEmailValid = false;
            binding.textInputLayoutEmail.setError("Email không hợp lệ");
        }
        // Cập nhật trạng thái nút
        updateButtonState();
    }

    private void updateButtonState() {
        binding.btnSave.setEnabled(isEmailValid);
    }
}
