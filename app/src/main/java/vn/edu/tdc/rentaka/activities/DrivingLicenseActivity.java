package vn.edu.tdc.rentaka.activities;

import android.Manifest;
import android.app.AlertDialog;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import vn.edu.tdc.rentaka.databinding.BottomSheetDialogInformationGplxBinding;
import vn.edu.tdc.rentaka.databinding.DrivingLicenseLayoutBinding;
import vn.edu.tdc.rentaka.models.DrivingLicense;

public class DrivingLicenseActivity extends AppCompatActivity {
   private DrivingLicenseLayoutBinding binding;
     boolean isCalendar = false;
     boolean isNameValid = false;
     boolean isGPLXValid = false;
     boolean isImageGPLXValid = false;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private BottomSheetDialogInformationGplxBinding bottomSheetDialogInformationGplxBinding;
    BottomSheetDialog bottomSheetDialogDritvingLicenseData;
    private static final int STORAGE_PERMISSION_CODE = 101;
    Uri imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving_license_layout);
        binding = DrivingLicenseLayoutBinding.inflate(getLayoutInflater());
        bottomSheetDialogInformationGplxBinding = BottomSheetDialogInformationGplxBinding.inflate(getLayoutInflater(),null,false);

        //Bottom sheet
        // Tạo một đối tượng BottomSheetDialog mới
        bottomSheetDialogDritvingLicenseData = new BottomSheetDialog(
                DrivingLicenseActivity.this, R.style.BottomSheetDialogTheme
        );
        // Đặt nội dung view của BottomSheetDialog là root view của lai xe bottomSheetDialogInformationGplxBinding
        bottomSheetDialogDritvingLicenseData.setContentView(bottomSheetDialogInformationGplxBinding.getRoot());


        setContentView(binding.getRoot());
        //Set state save
        stateSaveGPLX();
        // Set up the image picker launcher
        // Khởi tạo ActivityResultLauncher để mở bộ chọn ảnh và nhận kết quả trả về
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Kiểm tra nếu kết quả trả về là thành công (RESULT_OK) và có dữ liệu (data không null)
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData(); // Lấy Uri của ảnh được chọn từ kết quả trả về
                        // Sử dụng thư viện Glide để tải và hiển thị ảnh đã chọn trong ImageView (avatar)
//                        Glide.with(this).load(imageUri).into(binding.imageGPLX);
                        binding.imageGPLX.setImageURI(imageUri);
                        //Cap nhat trang thai
                        isImageGPLXValid = true;
                        binding.textErrorImage.setVisibility(View.GONE);
                    }
                }
        );
        //SET HTML CHO TEXTVIEW (CUSTOM )
        String textNote = "<b>Lưu ý:</b>Để tránh phát sinh vấn đề trong quá trình thuê xe, <u>người đặt xe</u> trên Mioto (đã xác thực giấy phép lái xe) <b>ĐỒNG THỜI</b> phải là <u>người nhận xe</u>.";
        String textGPLX = "Hình chụp cần thấy <b> Ảnh đại diện</b> và <b> Số GPLX </b>";
        String textNote2 = "<b><u>Vì sao tôi phải Xác thực GPLX </u></b>";
        //Sử dụng để hiển thị nội dung HTML trong một TextView //Chuyển đổi nội dung HTML thành một đối tượng Spanned
        binding.textNote.setText(Html.fromHtml(textNote, Html.FROM_HTML_MODE_COMPACT));
        binding.textGPLX.setText(Html.fromHtml(textGPLX, Html.FROM_HTML_MODE_COMPACT));
        binding.textNote2.setText(Html.fromHtml(textNote2, Html.FROM_HTML_MODE_COMPACT));

        //Hien thi thong tin gplx
        binding.textNote2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bottomSheetDialogDritvingLicenseData.show();
            }
        });
        //An button
        bottomSheetDialogInformationGplxBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogDritvingLicenseData.dismiss();
            }
        });
        bottomSheetDialogInformationGplxBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogDritvingLicenseData.dismiss();
            }
        });
        // load du lieu vao man hinh user
        loadData();
        //Nut thoat
        //Button top back navigation
        setSupportActionBar(binding.topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Button top back navigation

        //Kiem tra du 12 chu so khong
        binding.editGPLX.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                //Kiem tra do dai cua so GPLX
                if (s != null && s.length() != 12) {
                    binding.textInputLayoutGPLX.setError("Số GPLX là 12 số");
                    isGPLXValid = false;
                } else {
                    binding.textInputLayoutGPLX.setError(null);
                    isGPLXValid = true;
                }
            }
        });

        //Kiem tra xem ngay cap co hop le khong
        binding.editTextDateIssued.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

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
                            isCalendar = false;
                            binding.textInputLayoutDateIssued.setError("Ngày cấp phải trước ngày hôm nay");
                        } else {
                            isCalendar = true;
                            binding.textInputLayoutDateIssued.setError(null);
                        }
                    }
                }
        });
        //       Theo doi nhap text ten hien thi
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

            }
        });
        //Xu ly xem anh da them chua

        // Tao bang chon ngay thang nam (lich)
        binding.editTextDateIssued.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một phiên bản DatePickerDialog mới
                Calendar calendar = Calendar.getInstance();
                //Lay ngay thang nam hien tai
                int day = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DrivingLicenseActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Gan lai cac ngay thang nam minh chon
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        //Gan ngay thang nam vao edit text
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        binding.editTextDateIssued.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, year, month, day);
                // Show ra dialog
                datePickerDialog.show();
            }
        });
        // Them anh
        binding.imageGPLX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkAndRequestPermissions();
            }
        });
    }
    //Xu ly du lieu vao man hinh
    private void loadData() {
        // Lấy ID của người dùng hiện tại
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users")
                    .child(userId).child("drivingLicense");

            // Đọc dữ liệu từ Firebase
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Lấy thông tin giấy phép lái xe từ dataSnapshot
                        DrivingLicense gplx = dataSnapshot.getValue(DrivingLicense.class);

                        if (gplx != null) {
                            // Hiển thị thông tin lên giao diện người dùng
                            binding.editTextName.setText(gplx.getFullName());
                            binding.editTextDateIssued.setText(gplx.getDateIssued());
                            binding.editGPLX.setText(gplx.getNumber());

                            // Nếu có ảnh, hiển thị ảnh bằng Glide
                            if (gplx.getImage() != null && !gplx.getImage().isEmpty()) {
                                Glide.with(DrivingLicenseActivity.this).load(gplx.getImage()).into(binding.imageGPLX);
                                isImageGPLXValid = true;
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Xử lý lỗi nếu cần thiết
                }
            });
        }
    }


    // Ham mo bo chon anh
    private void openImageSelector() {

        //Intent.ACTION_PICK được sử dụng để mở ứng dụng chọn ảnh
        // Tạo Intent để mở bộ chọn ảnh của hệ thống
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Sử dụng ActivityResultLauncher để khởi chạy Intent, mở bộ chọn ảnh
        imagePickerLauncher.launch(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageSelector(); // Mở bộ chọn ảnh khi quyền được cấp
            } else {
                Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Phương thức kiểm tra và yêu cầu các quyền cần thiết để truy cập bộ nhớ
    private void checkAndRequestPermissions() {
        // Kiểm tra nếu phiên bản Android từ API 33 (Android 13) trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Yêu cầu quyền đọc hình ảnh
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    STORAGE_PERMISSION_CODE);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Kiểm tra nếu phiên bản Android từ API 29 (Android 10) trở lên
            // Yêu cầu quyền đọc bộ nhớ ngoài
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
    }
    //Nut luu cua giay phep lai xe
    private void stateSaveGPLX(){
      binding.saveGPLX.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if (isCalendar && isNameValid && isGPLXValid && isImageGPLXValid){
                  //Luu du lieu
                  saveGPLXData();
                  finish();
              }
              else {
                  Toast.makeText(DrivingLicenseActivity.this, "Đăng kí giấy phép lái xe không thành công", Toast.LENGTH_SHORT).show();
              }
              if (!isCalendar){
                  binding.textInputLayoutDateIssued.setError("Chưa nhập ngày cấp phép");
              }
              if (!isNameValid){
                  binding.textInputLayoutName.setError("Chưa nhập tên");
              }
              if (!isGPLXValid){
                  binding.textInputLayoutGPLX.setError("Chưa nhập số GPLX");
              }
              if (!isImageGPLXValid){
                  binding.textErrorImage.setVisibility(View.VISIBLE);
              }

          }
          //Luu len server va len firebase
          private void saveGPLXData() {
              FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
              if (user != null) {
                  String userId = user.getUid();
                  String name = binding.editTextName.getText().toString();
                  String birthday = binding.editTextDateIssued.getText().toString();
                  String gplxNumber = binding.editGPLX.getText().toString();

                  // Neu co anh
                  if (imageUri != null) {
                      // Upload anh lên Firebase Storage
                      uploadImageAndSaveInfo(imageUri, userId, name, birthday, gplxNumber);
                  } else {
                      // Không có ảnh, chỉ lưu thông tin
                      saveInfoToDatabase(userId, name, birthday, gplxNumber, null);
                  }
              }
          }

          private void uploadImageAndSaveInfo(Uri imageUri, String userId, String name, String birthday, String gplxNumber) {
              StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("users/" + userId ).child("gplx").child("license.jpg");
              fileRef.putFile(imageUri)
                      .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                          String imageUrl = uri.toString();
                          saveInfoToDatabase(userId, name, birthday, gplxNumber, imageUrl);
                      }))
                      .addOnFailureListener(e -> Toast.makeText(DrivingLicenseActivity.this, "Tải ảnh lên thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
          }

          private void saveInfoToDatabase(String userId, String name, String dateIssued, String gplxNumber, String imageUrl) {
              DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId).child("drivingLicense");
              Map<String, Object> updates = new HashMap<>();
              updates.put("fullName", name);
              updates.put("dateIssued", dateIssued);
              updates.put("number", gplxNumber);
              if (imageUrl != null) {
                  updates.put("image", imageUrl);
              }
                databaseReference.setValue(updates);
              databaseReference.updateChildren(updates)
                      .addOnSuccessListener(aVoid -> Toast.makeText(DrivingLicenseActivity.this, "Đăng kí giấy phép lái xe thành công", Toast.LENGTH_SHORT).show())
                      .addOnFailureListener(e -> Toast.makeText(DrivingLicenseActivity.this, "Đăng kí giấy phép lái xe thất bại", Toast.LENGTH_SHORT).show());
          }

      });
    }


}