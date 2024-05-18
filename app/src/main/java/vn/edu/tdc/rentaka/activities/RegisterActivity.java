package vn.edu.tdc.rentaka.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.BottomSheetOtpPhoneLayoutBinding;
import vn.edu.tdc.rentaka.databinding.RegisterLayoutBinding;
import vn.edu.tdc.rentaka.models.UserModel;

public class RegisterActivity extends AppCompatActivity {
    //Code by syste
    String codeBySystem;
    private RegisterLayoutBinding binding;

    BottomSheetOtpPhoneLayoutBinding bottomSheetOtpPhoneLayoutBinding;
    // dat 1 bien de tao dialog
    BottomSheetDialog bottomSheetDialogIDMESS;

    //Cho tat ca deu chua hop le
    private boolean isPhoneValid = false;
    private boolean isNameValid = false;
    private boolean isPasswordValid = false;
    private boolean isPasswordConfirmValid = false;
    private boolean isEmailValid = false;
    //Kiem tra so dien thoai co duy nhat khong
    boolean isPhoneUnique = true;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        //Register activity
        binding = RegisterLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        //databe
        database = FirebaseDatabase.getInstance();
        //lay bang
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //Custom luc dau chua nhap gi thi nut dang ky se bi vo hieu hoa
        updateRegisterButton();
        //Customs textview chinh sach
        String text_policy = "Tôi đã đọc và đồng ý với <u>Chính sách& quy định </u> và <u> Chính sách bảo vệ dữ liệu cá nhân </u>của Mioto";
        binding.rangbuoc.setText(Html.fromHtml(text_policy, Html.FROM_HTML_MODE_COMPACT));
        //Regex cutom input
        //Theo doi nhap text phone
        binding.editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                //Kiem tra do dai cua so dien thoai
                if (s != null && s.length() != 10) {
                    isPhoneValid = false;
                    binding.textInputLayoutPhone.setError(getString(R.string.so_dien_thoai));
                } else {
                    String phone = s.toString();
                    isPhoneValid = true;
                    binding.textInputLayoutPhone.setError(null);
                    checkPhoneNumber(phone);
                }
                //Cap nhat nut dang ky
                updateRegisterButton();
            }
        });
        //Theo doi nhap text ten hien thi
        binding.editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Kiem tra do dai cua ten hien thi
                if (s != null && s.length() < 6) {
                    isNameValid = false;
                    binding.textInputLayoutName.setError(getString(R.string.b_n_ph_i_tr_n_6_k_t_cho_t_n_hi_n_th));
                } else {
                    isNameValid = true;
                    binding.textInputLayoutName.setError(null);
                }
                //Cap nhat nut dang ky
                updateRegisterButton();
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
                }
                //So sanh input mat khau nhap lai xem tren nhap giong duoi kh
                //khi mat khau nhap lai kh co gi thi set esle :>>
                String confirmation = binding.editTextPasswordConfirm.getText().toString();
                if (!confirmation.isEmpty() && !confirmation.equals(s.toString())) {
                    isPasswordConfirmValid = false;
                    binding.textInputLayoutPasswordConfirm.setError(getString(R.string.m_t_kh_u_kh_ng_kh_p));
                } else if (!confirmation.isEmpty() && confirmation.equals(s.toString())) {
                    isPasswordConfirmValid = true;
                    binding.textInputLayoutPasswordConfirm.setError(null);
                }
                //Cap nhat nut dang ky
                updateRegisterButton();
            }
        });
        //Theo doi nhap text nhap lai mat khau
        binding.editTextPasswordConfirm.addTextChangedListener(new TextWatcher() {
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
                    binding.textInputLayoutPasswordConfirm.setError(getString(R.string.m_t_kh_u_ph_i_c_t_nh_t_6_k_t));
                } else if (s != null && s.length() > 24) {
                    isPasswordConfirmValid = false;
                    binding.textInputLayoutPasswordConfirm.setError("Mật khẩu không được quá 24 ký tự");
                }
                //Can so sanh voi mat khau
                else {
                    isPasswordConfirmValid = true;
                    binding.textInputLayoutPasswordConfirm.setError(null);
                }
                //So sanh input mat khau nhap lai xem tren nhap giong tren kh
                if (!binding.editTextPassword.getText().toString().equals(s.toString())) {
                    isPasswordConfirmValid = false;
                    binding.textInputLayoutPasswordConfirm.setError(getString(R.string.m_t_kh_u_kh_ng_kh_p));
                }
                //Neu dung thi gan lai true
                else {
                    isPasswordConfirmValid = true;
                    binding.textInputLayoutPasswordConfirm.setError(null);
                }

                //Cap nhat nut dang ky
                updateRegisterButton();
            }

        });
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
                String regex ="^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$" ;
                //roi sao nua
                if (s.toString().matches(regex)) {
                    isEmailValid = true;
                    binding.textInputLayoutEmail.setError(null);
                } else {
                    isEmailValid = false;
                    binding.textInputLayoutEmail.setError("Email không hợp lệ");
                }
            }
        });
        //Close dang ky
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //Vô hieu hoa dang ky neu kh dung cac dieu kien tren
    private void updateRegisterButton() {
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPhoneValid && isNameValid && isPasswordValid && isPasswordConfirmValid && isEmailValid && isPhoneUnique) {
                    //**********************************************************************
                    //Hàm dang ki neu dung dc otp chua qua 10sms
                    //Hien thi boom sheet
                    logicBottomSheet();
                    //Gui yeu cau OTP
                    String phone = binding.editTextPhone.getText().toString();
                    if (phone.startsWith("0")) {
                        phone = "+84" + phone.substring(1);
                    }
                    Toast.makeText(RegisterActivity.this, phone, Toast.LENGTH_SHORT).show();
                    //Gui ma otp
                    sendRequestOTP(phone);
                    //**************************************************************************
//**************************************************************************
                    //Hàm dang ki neu otp qua 10sms
//                    RegisterAPI();
//**************************************************************************

                } else {
                    //Hien thi thong bao khong dang ky thanh cong
                    Snackbar snackbar = Snackbar.make(view, R.string.dang_ki_khhong_thanh_cong, Snackbar.LENGTH_LONG);
//                    snackbar.setBackgroundTint(ContextCompat.getColor(RegisterActivity.this, R.color.mau_do_nhe));
//                    snackbar.setTextColor(ContextCompat.getColor(RegisterActivity.this,R.color.maudo));
                    snackbar.show();
                    //Hien thi cho cac input null
                    if (binding.editTextPhone.getText().toString().isEmpty()) {
                        binding.textInputLayoutPhone.setError("Chưa nhập số điện thoại");
                    }
                    if (binding.editTextName.getText().toString().isEmpty()) {
                        binding.textInputLayoutName.setError("Chưa nhập tên hiển thị");
                    }
                    if (binding.editTextPassword.getText().toString().isEmpty()) {
                        binding.textInputLayoutPassword.setError("Chưa nhập mật khẩu ");
                    }
                    if (binding.editTextPasswordConfirm.getText().toString().isEmpty()) {
                        binding.textInputLayoutPasswordConfirm.setError("Chưa nhập xác nhận mật khẩu");
                    }
                    if (binding.editTextEmail.getText().toString().isEmpty()) {
                        binding.textInputLayoutEmail.setError("Chưa nhập email");
                    }
                    ;
                }
            }
        });
    }

    private void logicBottomSheet() {

//        Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
        //Tao dialog moi
        bottomSheetDialogIDMESS = new BottomSheetDialog(
                RegisterActivity.this, R.style.BottomSheetDialogTheme
        );
        //Lay layout
        bottomSheetOtpPhoneLayoutBinding = BottomSheetOtpPhoneLayoutBinding.inflate(getLayoutInflater(), null, false);
        //Set noi dung view cua dialog la root view cua bottomSheetIDMESSLayoutBinding
        bottomSheetDialogIDMESS.setContentView(bottomSheetOtpPhoneLayoutBinding.getRoot());
        //Set data len text view
        bottomSheetOtpPhoneLayoutBinding.soDtTextView.setText(binding.editTextPhone.getText().toString());

        //Set mot OnClickListener cho nut dong trong bottomSheetIDMESSLayoutBinding
        bottomSheetDialogIDMESS.show();

        //Set nhap otp
        setupOtpInputs();
        //Close
        bottomSheetOtpPhoneLayoutBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogIDMESS.dismiss();
            }
        });


    }
   //Set up nut otp
   private void setupOtpInputs() {
       // Khai báo và khởi tạo mảng các trường EditText.
       EditText[] otpInputs = new EditText[]{
               bottomSheetOtpPhoneLayoutBinding.otp1,
               bottomSheetOtpPhoneLayoutBinding.otp2,
               bottomSheetOtpPhoneLayoutBinding.otp3,
               bottomSheetOtpPhoneLayoutBinding.otp4,
               bottomSheetOtpPhoneLayoutBinding.otp5,
               bottomSheetOtpPhoneLayoutBinding.otp6
       };

       // Duyệt qua từng trường EditText trong mảng để thiết lập TextWatcher cho chúng.
       for (int i = 0; i < otpInputs.length; i++) {
           final int index = i; // Lưu lại chỉ số của trường hiện tại để sử dụng trong TextWatcher.

           // Thiết lập TextWatcher cho từng trường EditText.
           otpInputs[i].addTextChangedListener(new TextWatcher() {
               @Override
               public void beforeTextChanged(CharSequence s, int start, int count, int after) {

               }

               @Override
               public void onTextChanged(CharSequence s, int start, int before, int count) {

               }

               @Override
               public void afterTextChanged(Editable s) {
                   // Kiểm tra nếu độ dài của chuỗi vừa nhập là 1 và không phải trường cuối cùng.
                   if (s.length() == 1 && index < otpInputs.length - 1) {
                       // Chuyển focus đến trường EditText tiếp theo.
                       otpInputs[index + 1].requestFocus();
                   }
                   else if (s.length() == 0 && index > 0) {
                       // Nếu ký tự bị xóa và trường hiện tại là trống, chuyển focus ngược lại trường trước đó.
                       otpInputs[index - 1].requestFocus();
                   }
               }
           });
       }
   }



    //Yeu cau lay OTP
    private void sendRequestOTP(String phone) {
        //Lay tu doc firebase
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        bottomSheetOtpPhoneLayoutBinding.otp1.setText(String.valueOf(code.charAt(0)));
                        bottomSheetOtpPhoneLayoutBinding.otp2.setText(String.valueOf(code.charAt(1)));
                        bottomSheetOtpPhoneLayoutBinding.otp3.setText(String.valueOf(code.charAt(2)));
                        bottomSheetOtpPhoneLayoutBinding.otp4.setText(String.valueOf(code.charAt(3)));
                        bottomSheetOtpPhoneLayoutBinding.otp5.setText(String.valueOf(code.charAt(4)));
                        bottomSheetOtpPhoneLayoutBinding.otp6.setText(String.valueOf(code.charAt(5)));

                    }

                }


                private void setFocusChange(EditText current, EditText next) {

                }


                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng dang ki thành công, cập nhật giao diện hoặc chuyển hướng người dùng
                            RegisterAPI();
                        } else {
                            // Đăng nhập thất bại, hiển thị lỗi cho người dùng
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(RegisterActivity.this, "Mã không hợp lệ. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    public void callNextScreenFromOTP(View view) {
        String otp1 = bottomSheetOtpPhoneLayoutBinding.otp1.getText().toString();
        String otp2 = bottomSheetOtpPhoneLayoutBinding.otp2.getText().toString();
        String otp3 = bottomSheetOtpPhoneLayoutBinding.otp3.getText().toString();
        String otp4 = bottomSheetOtpPhoneLayoutBinding.otp4.getText().toString();
        String otp5 = bottomSheetOtpPhoneLayoutBinding.otp5.getText().toString();
        String otp6 = bottomSheetOtpPhoneLayoutBinding.otp6.getText().toString();
        String code = otp1 + otp2 + otp3 + otp4 + otp5 + otp6;


        if (code.length() == 6) {
            verifyCode(code);
        } else {
            Toast.makeText(this, "Vui lòng nhập đủ OTP", Toast.LENGTH_SHORT).show();
        }
    }
    //Xu ly dang ki tren fire base
    public void RegisterAPI() {
        String phone = binding.editTextPhone.getText().toString();
        String pass = binding.editTextPassword.getText().toString();
        String username = binding.editTextName.getText().toString();
        String email = binding.editTextEmail.getText().toString();
        //Hien thi ben user neu laod mang
        binding.btnSave.setText("Loading...");
        binding.progressBar.setVisibility(View.VISIBLE);

        //create user
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Get current date
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            String currentDate = sdf.format(new Date());
                            //Cac du lieu chua xu ly
                            String gender ="None";
                            String imageUser ="";
                            String address= "";
                            String birthday= "00/00/0000";
                            // Registration successful
                            UserModel user = new UserModel(phone, username, email, gender,currentDate,imageUser,address,birthday);
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user);
                            //an load
                            binding.progressBar.setVisibility(View.GONE);
                            //Chuyen ve login
                            Toast.makeText(RegisterActivity.this, R.string.ng_k_th_nh_c_ng, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            // Dang ki that bai
                            binding.progressBar.setVisibility(View.GONE);
                            Exception e = task.getException();
                            Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            binding.btnSave.setText("Đăng Kí");
                        }
                    }
                });


    }

    // Kiểm tra số điện thoại tren fire base
    private void checkPhoneNumber(final String phone) {
        DatabaseReference usersRef = database.getReference().child("Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean phoneExists = false;
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String existingPhone = userSnapshot.child("phone").getValue(String.class);
                    if (phone.equals(existingPhone)) {
                        phoneExists = true;
                        break;
                    }
                }
                if (phoneExists) {
                    //Trung
                    Toast.makeText(RegisterActivity.this, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show();
                    isPhoneUnique = false;
                    binding.textInputLayoutPhone.setError(getString(R.string.s_i_n_tho_i_t_n_t_i));
                } else {
                    // khong trung
                    Toast.makeText(RegisterActivity.this, "Số điện thoại hợp lệ", Toast.LENGTH_SHORT).show();
                    isPhoneUnique = true;
                    binding.textInputLayoutPhone.setError(null);
                }

                // cap nhati
                updateRegisterButton();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }



}
