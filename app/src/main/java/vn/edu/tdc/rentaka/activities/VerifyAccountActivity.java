package vn.edu.tdc.rentaka.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.VerifyAccountLayoutBinding;
import vn.edu.tdc.rentaka.models.BankCard;
import vn.edu.tdc.rentaka.models.CitizenIdentificationCard;

public class VerifyAccountActivity extends AppCompatActivity {
    private VerifyAccountLayoutBinding binding;
    boolean isCalendarCCCD = false;
    boolean isCalendarBank = false;
    boolean isUserNameCCCDValid = false;
    boolean isUserNameBankValid = false;
    boolean isNameBankValid = false;
    boolean isImageCCCDValid = false;
    boolean isImageBankValid = false;
    boolean isNumberCCCDValid = false;
    boolean isNumberBankValid = false;
    private ActivityResultLauncher<Intent> imagePickerLauncherCCCD;
    private ActivityResultLauncher<Intent> imagePickerLauncherBank;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int REQUEST_CODE_CCCD = 1;
    private static final int REQUEST_CODE_BANK = 2;
    private Uri imageUriCCCD;
    private Uri imageUriBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_account_layout);
        binding = VerifyAccountLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        stateSaveGPLX();

        // Set up the image picker for CCCD
        imagePickerLauncherCCCD = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUriCCCD = result.getData().getData();
                        binding.imageCCCD.setImageURI(imageUriCCCD);
                        isImageCCCDValid = true;
                        binding.textErrorImage.setVisibility(View.GONE);
                    }
                }
        );

        // Set up the image picker for Bank
        imagePickerLauncherBank = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUriBank = result.getData().getData();
                        binding.imageBank.setImageURI(imageUriBank);
                        isImageBankValid = true;
                        binding.textErrorImageBank.setVisibility(View.GONE);
                    }
                }
        );

        // SET HTML CHO TEXTVIEW (CUSTOM)
        String textNote = "<b>Lưu ý:</b> Để tránh phát sinh vấn đề trong quá trình sử dụng thẻ ngân hàng, người đăng ký thẻ trên hệ thống (đã xác thực chứng minh nhân dân) <b>ĐỒNG THỜI</b> phải là người sử dụng thẻ.";
        String textCCCD = "Hình chụp cần thấy <b> Ảnh đại diện</b> và <b> Số CCCD </b>";
        String textBank = "Hình chụp cần thấy <b> Ảnh đại diện</b> và <b> Số Bank </b>";
        String textNote2 = "<b><u>Vì sao tôi phải Xác thực GPLX </u></b>";

        // Sử dụng để hiển thị nội dung HTML trong một TextView // Chuyển đổi nội dung HTML thành một đối tượng Spanned
        binding.textNote.setText(Html.fromHtml(textNote, Html.FROM_HTML_MODE_COMPACT));
        binding.textCCCD.setText(Html.fromHtml(textCCCD, Html.FROM_HTML_MODE_COMPACT));
        binding.textBank.setText(Html.fromHtml(textBank, Html.FROM_HTML_MODE_COMPACT));
        binding.textNote2.setText(Html.fromHtml(textNote2, Html.FROM_HTML_MODE_COMPACT));

        // Load dữ liệu nếu có
        loadDataBank();
        loadDataCCCD();

        // Nut thoat
        // Button top back navigation
        setSupportActionBar(binding.topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.topAppBar.setNavigationOnClickListener(v -> finish());

        // Kiem tra du 12 chu so khong
        binding.editCCCD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() != 12) {
                    binding.textCCCD.setError("Số CCCD là 12 số");
                    isNumberCCCDValid = false;
                } else {
                    binding.textCCCD.setError(null);
                    isNumberCCCDValid = true;
                }
            }
        });

        binding.editBank.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 8 || s.length() > 19) {
                    binding.textBank.setError("Số tài khoản phải từ 8 đến 19 số");
                    isNumberBankValid = false;
                } else {
                    binding.textBank.setError(null);
                    isNumberBankValid = true;
                }
            }
        });

        // Kiem tra xem ngay cap CCCD co hop le khong
        binding.editTextDateIssuedCCCD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 10) {
                    int day = Integer.parseInt(s.toString().substring(0, 2));
                    int month = Integer.parseInt(s.toString().substring(3, 5)) - 1;
                    int year = Integer.parseInt(s.toString().substring(6));
                    Calendar enteredDate = Calendar.getInstance();
                    enteredDate.set(year, month, day, 0, 0, 0);
                    enteredDate.set(Calendar.MILLISECOND, 0);
                    Calendar today = Calendar.getInstance();
                    today.set(Calendar.HOUR_OF_DAY, 0);
                    today.set(Calendar.MINUTE, 0);
                    today.set(Calendar.SECOND, 0);
                    today.set(Calendar.MILLISECOND, 0);
                    if (enteredDate.after(today)) {
                        isCalendarCCCD = false;
                        binding.textInputLayoutDateIssuedCCCD.setError("Ngày cấp phải trước ngày hôm nay");
                    } else {
                        isCalendarCCCD = true;
                        binding.textInputLayoutDateIssuedCCCD.setError(null);
                    }
                }
            }
        });

        // Kiem tra xem ngay cap Bank co hop le khong
        binding.editTextBankDateIssued.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 10) {
                    int day = Integer.parseInt(s.toString().substring(0, 2));
                    int month = Integer.parseInt(s.toString().substring(3, 5)) - 1;
                    int year = Integer.parseInt(s.toString().substring(6));
                    Calendar enteredDate = Calendar.getInstance();
                    enteredDate.set(year, month, day, 0, 0, 0);
                    enteredDate.set(Calendar.MILLISECOND, 0);
                    Calendar today = Calendar.getInstance();
                    today.set(Calendar.HOUR_OF_DAY, 0);
                    today.set(Calendar.MINUTE, 0);
                    today.set(Calendar.SECOND, 0);
                    today.set(Calendar.MILLISECOND, 0);
                    if (enteredDate.after(today)) {
                        isCalendarBank = false;
                        binding.textInputLayoutBankDateIssued.setError("Ngày cấp phải trước ngày hôm nay");
                    } else {
                        isCalendarBank = true;
                        binding.textInputLayoutBankDateIssued.setError(null);
                    }
                }
            }
        });

        // Theo doi nhap ten hien thi nguoi dung cua CCCD
        binding.editTextUserNameCCCD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() < 6) {
                    isUserNameCCCDValid = false;
                    binding.textInputLayoutUserNameCCCD.setError(getString(R.string.b_n_ph_i_tr_n_6_k_t_cho_t_n_hi_n_th));
                } else {
                    isUserNameCCCDValid = true;
                    binding.textInputLayoutUserNameCCCD.setError(null);
                }
            }
        });

        // Theo doi nhap ten hien thi nguoi dung cua Bank
        binding.editTextUserNameBank.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() < 6) {
                    isUserNameBankValid = false;
                    binding.textInputLayoutUserNameBank.setError(getString(R.string.b_n_ph_i_tr_n_6_k_t_cho_t_n_hi_n_th));
                } else {
                    isUserNameBankValid = true;
                    binding.textInputLayoutUserNameBank.setError(null);
                }
            }
        });

        // Theo doi nhap ten hien thi ten Bank
        binding.editTextNameBank.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() < 4) {
                    isNameBankValid = false;
                    binding.textInputLayoutUserNameBank.setError(getString(R.string.b_n_ph_i_tr_n_4_k_t_cho_t_n_hi_n_th));
                } else {
                    isNameBankValid = true;
                    binding.textInputLayoutUserNameBank.setError(null);
                }
            }
        });

        // Tao bang chon ngay thang nam (lich) cho CCCD
        binding.editTextDateIssuedCCCD.setOnClickListener(v -> showDatePickerDialog(REQUEST_CODE_CCCD));

        // Tao bang chon ngay thang nam (lich) cho Bank
        binding.editTextBankDateIssued.setOnClickListener(v -> showDatePickerDialog(REQUEST_CODE_BANK));

        // Them anh CCCD
        binding.imageCCCD.setOnClickListener(v -> checkAndRequestPermissions(REQUEST_CODE_CCCD));

        // Them anh Bank
        binding.imageBank.setOnClickListener(v -> checkAndRequestPermissions(REQUEST_CODE_BANK));
    }

    // Phuong thuc load du lieu CCCD vao man hinh
    private void loadDataCCCD() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("citizenIdCard");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        CitizenIdentificationCard cccd = dataSnapshot.getValue(CitizenIdentificationCard.class);
                        if (cccd != null) {
                            binding.editTextUserNameCCCD.setText(cccd.getFullName());
                            binding.editTextDateIssuedCCCD.setText(cccd.getDateIssued());
                            binding.editCCCD.setText(cccd.getNumber());
                            if (cccd.getImage() != null && !cccd.getImage().isEmpty()) {
                                Glide.with(VerifyAccountActivity.this).load(cccd.getImage()).into(binding.imageCCCD);
                                isImageCCCDValid = true;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    // Phuong thuc load du lieu Bank vao man hinh
    private void loadDataBank() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("bankCard");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        BankCard bank = dataSnapshot.getValue(BankCard.class);
                        if (bank != null) {
                            binding.editTextUserNameBank.setText(bank.getFullName());
                            binding.editTextBankDateIssued.setText(bank.getDateIssued());
                            binding.editBank.setText(bank.getNumber());
                            binding.editTextNameBank.setText(bank.getBankName());
                            if (bank.getImage() != null && !bank.getImage().isEmpty()) {
                                Glide.with(VerifyAccountActivity.this).load(bank.getImage()).into(binding.imageBank);
                                isImageBankValid = true;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    // Ham mo bo chon anh
    private void openImageSelector(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (requestCode == REQUEST_CODE_CCCD) {
            imagePickerLauncherCCCD.launch(intent);
        } else if (requestCode == REQUEST_CODE_BANK) {
            imagePickerLauncherBank.launch(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openImageSelector(requestCode);
        } else {
            Toast.makeText(this, "Khong truy cap duoc", Toast.LENGTH_SHORT).show();
        }
    }

    // Phuong thuc kiem tra va yeu cau cac quyen can thiet de truy cap bo nho
    private void checkAndRequestPermissions(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    requestCode);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestCode);
        }
    }

    // Phuong thuc hien thi DatePickerDialog
    private void showDatePickerDialog(int requestCode) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(VerifyAccountActivity.this, (view, selectedYear, selectedMonth, selectedDay) -> {
            calendar.set(selectedYear, selectedMonth, selectedDay);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (requestCode == REQUEST_CODE_CCCD) {
                binding.editTextDateIssuedCCCD.setText(simpleDateFormat.format(calendar.getTime()));
            } else if (requestCode == REQUEST_CODE_BANK) {
                binding.editTextBankDateIssued.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    // Nut luu cua giay phep lai xe
    private void stateSaveGPLX() {
        binding.saveXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNumberBankValid && isImageBankValid && isNumberCCCDValid && isCalendarBank && isCalendarCCCD && isUserNameCCCDValid && isUserNameBankValid && isNameBankValid && isImageCCCDValid) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        String userId = user.getUid();
                        saveGPLXData(userId);
                        saveBankData(userId);
                    }
                } else {
                    Toast.makeText(VerifyAccountActivity.this, "Đăng kí không thành công", Toast.LENGTH_SHORT).show();
                }
                if (!isCalendarCCCD) {
                    binding.textInputLayoutDateIssuedCCCD.setError("Chưa nhập ngày cấp ");
                }
                if (!isCalendarBank) {
                    binding.textInputLayoutBankDateIssued.setError("Chưa nhập ngày cấp ");
                }
                if (!isUserNameCCCDValid) {
                    binding.textInputLayoutUserNameCCCD.setError("Chưa nhập tên người trên cccd");
                }
                if (!isUserNameBankValid) {
                    binding.textInputLayoutUserNameBank.setError("Chưa nhập tên người trên TK");
                }
                if (!isNameBankValid) {
                    binding.textInputLayoutNameBank.setError("Chưa nhập tên ngân hàng");
                }
                if (!isNumberCCCDValid) {
                    binding.editCCCD.setError("Chưa nhập số CCCD");
                }
                if (!isNumberBankValid) {
                    binding.editBank.setError("Chưa nhập số TK BANK");
                }
                if (!isImageCCCDValid) {
                    binding.textErrorImage.setVisibility(View.VISIBLE);
                }
                if (!isImageBankValid) {
                    binding.textErrorImageBank.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // Phuong thuc luu du lieu GPLX
    private void saveGPLXData(String userId) {
        String name = binding.editTextUserNameCCCD.getText().toString();
        String dateIssued = binding.editTextDateIssuedCCCD.getText().toString();
        String cccdNumber = binding.editCCCD.getText().toString();

        if (imageUriCCCD != null) {
            uploadImageAndSaveInfo(imageUriCCCD, userId, name, dateIssued, cccdNumber, "citizenIdCard", "imageCCCD");
        }
    }

    // Phuong thuc luu du lieu Bank
    private void saveBankData(String userId) {
        String name = binding.editTextUserNameBank.getText().toString();
        String dateIssued = binding.editTextBankDateIssued.getText().toString();
        String bankNumber = binding.editBank.getText().toString();
        String bankName = binding.editTextNameBank.getText().toString();

        if (imageUriBank != null) {
            uploadImageAndSaveInfo(imageUriBank, userId, name, dateIssued, bankNumber, "bankCard", "imageBank");
        }
    }

    // Phuong thuc upload anh va luu thong tin len Firebase
    private void uploadImageAndSaveInfo(Uri imageUri, String userId, String name, String dateIssued, String number, String node, String imageField) {
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child(node + "/" + userId + "/" + imageField + ".jpg");
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    saveInfoToDatabase(userId, name, dateIssued, number, imageUrl, node);
                }))
                .addOnFailureListener(e -> Toast.makeText(VerifyAccountActivity.this, "Tải ảnh lên thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Phuong thuc luu thong tin len Firebase Database
    private void saveInfoToDatabase(String userId, String name, String dateIssued, String number, String imageUrl, String node) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child(node);
        Map<String, Object> updates = new HashMap<>();
        updates.put("fullName", name);
        updates.put("dateIssued", dateIssued);
        updates.put("number", number);
        if (imageUrl != null) {
            updates.put("image", imageUrl);
        }

        databaseReference.updateChildren(updates)
                .addOnSuccessListener(aVoid -> Toast.makeText(VerifyAccountActivity.this, "Định danh cá nhân thành công", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(VerifyAccountActivity.this, "Định danh cá nhân thất bại", Toast.LENGTH_SHORT).show());
    }
}
