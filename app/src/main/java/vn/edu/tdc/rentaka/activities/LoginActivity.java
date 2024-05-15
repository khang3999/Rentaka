package vn.edu.tdc.rentaka.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.LoginLayoutBinding;
import vn.edu.tdc.rentaka.databinding.MyAccountLayoutBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginLayoutBinding binding;
    private boolean isEmailValid = false;
    private boolean isPassword = false;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        binding = LoginLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        stateLogin();
        //Customs textview register
        String text_register = "<u><b>Đăng ký ngay</b></u>";
        binding.textViewRegister.setText(Html.fromHtml(text_register, Html.FROM_HTML_MODE_COMPACT));
        //Custome error textview
        //Theo doi nhap text
        binding.editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //so sanh regex email
                String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
                //roi sao nua
                if (s.toString().matches(regex)) {
                    isEmailValid = true;
                    binding.textInputLayoutEmail.setError(null);
                } else {
                    isEmailValid = false;
                    binding.textInputLayoutEmail.setError("Email không hợp lệ");
                }
            //Cap nhat trang thai dang nhap
            stateLogin();
        }
        });
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
                if (s != null && s.length() < 6 ){
                    isPassword = false;
                    binding.textInputLayoutPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
                }
                else if (s != null && s.length() > 24){
                    isPassword = false;
                    binding.textInputLayoutPassword.setError("Mật khẩu không được quá 24 ký tự");
                }
                else {
                    isPassword = true;
                    binding.textInputLayoutPassword.setError(null);
                }
                stateLogin();
            }
        });
//Nut dang ky
        binding.textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    public void stateLogin (){
//Nut dang nhap
            binding.btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isEmailValid && isPassword){
                        loginUser(binding.editTextEmail.getText().toString(), binding.editTextPassword.getText().toString());

                    }
                    else if (isEmailValid == false && isPassword == false){
                        Snackbar snackbar = Snackbar.make(view, "Xin bạn hãy nhập mật khẩu hoặc tài khoản", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                    else  {
                        Snackbar snackbar = Snackbar.make(view, R.string.sai_m_t_kh_u_ho_c_t_i_kho_n, Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
            });

    }
    //đăng nhâp
    private void loginUser(String email, String password) {
        //Hien thi ben user neu laod mang
        binding.btnSave.setText("Loading...");
        binding.progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                       // Dang nhap thanh cong
                        binding.progressBar.setVisibility(View.GONE);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        //Khi sai mk , tk
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Mât khẩu sai hoặc tài khoản không tồn tại!",
                                Toast.LENGTH_SHORT).show();
                        binding.btnSave.setText(R.string.dang_nhap);
                    }
                });
    }


}