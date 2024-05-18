package vn.edu.tdc.rentaka.activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.ChangeThePasswordLayoutBinding;

public class ChangeThePasswordActivity extends AppCompatActivity {
    private ChangeThePasswordLayoutBinding binding;
     boolean isPasswordValid = false;
     boolean isPasswordConfirmValid = false;
     boolean isPasswordUnique = true;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_the_password_layout);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        binding = ChangeThePasswordLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Trang thai nut chinh sua mat khau
        updateChangePasswordButton();
        binding.leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Theo doi nhap text mat khau
        binding.editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Kiem tra do dai cua mat khau
                if (s != null && s.length() < 6) {
                    isPasswordValid = false;
                    binding.textInputLayoutPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
                } else if (s != null && s.length() > 24) {
                    isPasswordValid = false;
                    binding.textInputLayoutPassword.setError(getString(R.string.m_t_kh_u_kh_ng_c_qu_24_k_t));
                } else {
                    isPasswordValid = true;
                    binding.textInputLayoutPassword.setError(null);
                    checkPassword(s.toString());
                }
                //So sanh input mat khau nhap lai xem tren nhap giong duoi kh
                //khi mat khau nhap lai kh co gi thi set esle :>>
                String confirmation = binding.editTextConfirmPassword.getText().toString();
                if (!confirmation.isEmpty() && !confirmation.equals(s.toString())) {
                    isPasswordConfirmValid = false;
                    binding.textInputLayoutConfirmPassword.setError(getString(R.string.m_t_kh_u_kh_ng_kh_p));
                } else if (!confirmation.isEmpty() && confirmation.equals(s.toString())) {
                    isPasswordConfirmValid = true;
                    binding.textInputLayoutConfirmPassword.setError(null);
                    checkPassword(s.toString());
                }
                //Cap nhat nut doi mat khau
                updateChangePasswordButton();

            }
        });
        //Theo doi nhap text nhap lai mat khau
        binding.editTextConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Kiem tra do dai cua mat khau
                if (s != null && s.length() < 6) {
                    //gan lai true
                    isPasswordConfirmValid = false;
                    binding.textInputLayoutConfirmPassword.setError(getString(R.string.m_t_kh_u_ph_i_c_t_nh_t_6_k_t));
                } else if (s != null && s.length() > 24) {
                    isPasswordConfirmValid = false;
                    binding.textInputLayoutConfirmPassword.setError("Mật khẩu không được quá 24 ký tự");
                }
                //Can so sanh voi mat khau
                else {
                    isPasswordConfirmValid = true;
                    binding.textInputLayoutConfirmPassword.setError(null);
                }
                //So sanh input mat khau nhap lai xem tren nhap giong tren kh
                if (!binding.editTextPassword.getText().toString().equals(s.toString())) {
                    isPasswordConfirmValid = false;
                    binding.textInputLayoutConfirmPassword.setError(getString(R.string.m_t_kh_u_kh_ng_kh_p));
                }
                //Neu dung thi gan lai true
                else {
                    isPasswordConfirmValid = true;
                    binding.textInputLayoutConfirmPassword.setError(null);
                }

                //Cap nhat nut doi mk
                updateChangePasswordButton();
            }
        });

    }
    private void updateChangePasswordButton() {
        binding.changePassbtn.setOnClickListener(view -> {
            // Kiểm tra điều kiện trước khi cập nhật
            if (isPasswordValid && isPasswordConfirmValid && isPasswordUnique) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    String newPassword = binding.editTextPassword.getText().toString();
                    // Cập nhật mật khẩu trong FirebaseAuth
                    currentUser.updatePassword(newPassword)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // Hiển thị thông báo cập nhật mật khẩu thành công
                                    Snackbar.make(view, R.string.i_m_t_kh_u_th_nh_c_ng, Snackbar.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Snackbar.make(view, R.string.s_a_kh_ng_th_nh_c_ng, Snackbar.LENGTH_LONG).show();
                                }
                            });
                }
            } else {
                Snackbar.make(view, R.string.s_a_kh_ng_th_nh_c_ng, Snackbar.LENGTH_LONG).show();
                // Hiển thị thông báo lỗi nếu người dùng chưa nhập mật khẩu mới hoặc mật khẩu xác nhận
                if (binding.editTextPassword.getText().toString().isEmpty()) {
                    binding.textInputLayoutPassword.setError("Chưa nhập mật khẩu mới");
                }
                if (binding.editTextConfirmPassword.getText().toString().isEmpty()) {
                    binding.textInputLayoutConfirmPassword.setError("Chưa nhập xác nhận mật khẩu mới");
                }
            }
        });
    }

    private void checkPassword(final String newPassword) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Lấy email của người dùng hiện tại
            String currentEmail = currentUser.getEmail();

            // Lấy mật khẩu hiện tại của người dùng từ Firebase Authentication
            AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, newPassword);

            // Re-authenticate người dùng
            currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Nếu re-authentication thành công, mật khẩu mới trùng với mật khẩu hiện tại
                        isPasswordUnique = false;
                        binding.textInputLayoutPassword.setError("Mật khẩu mới không được trùng với mật khẩu hiện tại");
                    } else {
                        // Nếu re-authentication không thành công, mật khẩu mới không trùng với mật khẩu hiện tại
                        isPasswordUnique = true;
                        binding.textInputLayoutPassword.setError(null);
                    }
                    updateChangePasswordButton();
                }
            });
        }

}

}
