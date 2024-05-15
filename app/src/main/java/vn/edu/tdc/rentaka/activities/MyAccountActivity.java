package vn.edu.tdc.rentaka.activities;

import vn.edu.tdc.rentaka.R;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import vn.edu.tdc.rentaka.adapters.MyAccountAdapter;
import vn.edu.tdc.rentaka.databinding.BottomSheetEditAccountLayoutBinding;
import vn.edu.tdc.rentaka.databinding.BottomSheetPhoneNumberLayoutBinding;
import vn.edu.tdc.rentaka.databinding.MyAccountLayoutBinding;
import vn.edu.tdc.rentaka.models.MyAccountModel;

public class MyAccountActivity extends AppCompatActivity {
    // Khai báo biến
    MyAccountAdapter adapter;
    boolean isNameValid = false;
    boolean isCalendar = false;
    private MyAccountLayoutBinding binding;
    private BottomSheetEditAccountLayoutBinding bottomSheetEditAccountLayoutBinding;
    BottomSheetDialog bottomSheetDialogEdit;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int PICK_IMAGE_REQUEST = 102;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    //Dat 1 bien la URI
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MyAccountLayoutBinding.inflate(getLayoutInflater());
        //Lay man hinh nen de len man hinh chinh
        bottomSheetEditAccountLayoutBinding = BottomSheetEditAccountLayoutBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());
        //Cap nhat button
        updateStateButtonEdit();

        // Set up the image picker launcher
        // Khởi tạo ActivityResultLauncher để mở bộ chọn ảnh và nhận kết quả trả về
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Kiểm tra nếu kết quả trả về là thành công (RESULT_OK) và có dữ liệu (data không null)
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData(); // Lấy Uri của ảnh được chọn từ kết quả trả về
                        // Sử dụng thư viện Glide để tải và hiển thị ảnh đã chọn trong ImageView (avatar)
                        Glide.with(this).load(imageUri).into(bottomSheetEditAccountLayoutBinding.avatar);
                    }
                }
        );

        //Gan adapter
        updateAdapterData("","");

        //Nut out
        binding.leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Bottom sheet
        // Tạo một đối tượng BottomSheetDialog mới
        bottomSheetDialogEdit = new BottomSheetDialog(
                MyAccountActivity.this, R.style.BottomSheetDialogTheme
        );

// Đặt nội dung view của BottomSheetDialog là root view của bottomSheetEditAccountLayoutBinding
        bottomSheetDialogEdit.setContentView(bottomSheetEditAccountLayoutBinding.getRoot());


        //Button show edit bottom sheet
        binding.rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Xu ly edit
                // Gan du lieu vao bottom sheet edit
                informationUserEdit();
                bottomSheetDialogEdit.show();

            }
        });

        //Button close bottom sheet edit
        bottomSheetEditAccountLayoutBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogEdit.dismiss();
            }
        });

        //Gan cac thong tin user tu firebase vao man hinh chinh
        informationUser();

        // Xu ly nhap gender
        selectGender();

        // Xu ly nhap ngay sinh
        selectDate();

        //Xu ly anh (button them anh)
        // Method to handle camera button click - checks permissions and opens image selector
        bottomSheetEditAccountLayoutBinding.addCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
                    openImageSelector();
                }
            }
        });

    }
    //Cap nhat trang thai cua nut edit
    public void updateStateButtonEdit(){
        //Xu ly save ben edit bottom sheet
        bottomSheetEditAccountLayoutBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCalendar && isNameValid){
                    //Xu ly firebase nut save
                    saveUserInformation();
                }
                else{
                    Toast.makeText(MyAccountActivity.this, "Chỉnh sửa không thành công", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

//Cap nhat adapter recycler view
    public void updateAdapterData(String email, String phone) {
        ArrayList<MyAccountModel> dataFuc = new ArrayList<>();
        dataFuc.add(new MyAccountModel("Giấy phép lái xe", "Thêm giấy phép lái xe"));
        dataFuc.add(new MyAccountModel("Điện thoại",!phone.isEmpty() ? phone : "Thêm số điện thoại"));
        dataFuc.add(new MyAccountModel("Email", !email.isEmpty() ? email : "Thêm email"));
        dataFuc.add(new MyAccountModel("Facebook", "Thêm facebook"));
        dataFuc.add(new MyAccountModel("Google", "Liên kết ngay"));

        adapter = new MyAccountAdapter(this, dataFuc);
        // Tạo đối tượng layout manager của Recycler View
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rycAcount.setLayoutManager(layoutManager);
        binding.rycAcount.setAdapter(adapter);

        // Chuyen cac man hinh khac
        adapter.setOnItemClickListener(new MyAccountAdapter.OnItemClickListener() {
            @Override
            public void onClickListener(int position) {
                switch (position) {
                    case 0:
                        Intent intent1 = new Intent(MyAccountActivity.this, DrivingLicenseActivity.class);
                        // intent1.putExtra("name",data1.get(position).getContent());
                        startActivity(intent1);
                        break;
                    case 1:
                        Toast.makeText(MyAccountActivity.this, "Tính năng sắp được ra mắt (Phone)", Toast.LENGTH_SHORT).show();

                        break;
                    case 2:
                        Toast.makeText(MyAccountActivity.this, "Tính năng sắp được ra mắt (Email)", Toast.LENGTH_SHORT).show();

                        break;
                    case 3:
                        Toast.makeText(MyAccountActivity.this, "Tính năng này sắp được ra mắt (Facebook)", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(MyAccountActivity.this, "Tính năng này sắp được ra mắt (Google)", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MyAccountActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

    }

    // Phương thức kiểm tra và yêu cầu các quyền cần thiết để truy cập bộ nhớ
    private boolean checkAndRequestPermissions() {
        // Kiểm tra nếu phiên bản Android từ API 33 (Android 13) trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Từ Android 13 (API 33) trở lên, sử dụng các quyền phương tiện cụ thể hơn
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                // Yêu cầu người dùng cấp quyền đọc hình ảnh
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        STORAGE_PERMISSION_CODE);
                return false; // Quyền chưa được cấp, yêu cầu quyền và trả về false
            }
            // Kiểm tra nếu phiên bản Android từ API 29 (Android 10) trở lên
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Từ Android 10 (API 29) trở lên, mô hình lưu trữ theo phạm vi được áp dụng
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Yêu cầu người dùng cấp quyền đọc bộ nhớ ngoài
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE);
                // Quyền chưa được cấp, yêu cầu quyền và trả về false
                return false;
            }
        }
        // Nếu tất cả các quyền cần thiết đã được cấp, trả về true
        return true;
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

    // Ham mo bo chon anh
    private void openImageSelector() {
        //Intent.ACTION_PICK được sử dụng để mở ứng dụng chọn ảnh
        // Tạo Intent để mở bộ chọn ảnh của hệ thống
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Sử dụng ActivityResultLauncher để khởi chạy Intent, mở bộ chọn ảnh
        imagePickerLauncher.launch(intent);
    }


        //Chon gioi tinh
        public void selectGender() {
            // Set OnClickListener for the gender TextView
            final int[] checkedItem = {-1};
            // xử lý nút để mở hộp thoại cảnh báo với lựa chọn mục duy nhất khi được nhấp vào
            bottomSheetEditAccountLayoutBinding.editTextGender.setOnClickListener(v -> {
                // instance của trình tạo AlertDialog để xây dựng hộp thoại cảnh báo
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyAccountActivity.this);
                // tiêu đề của hộp thoại cảnh báo
                alertDialog.setTitle("Select Gender");

                // danh sách các mục được hiển thị cho người dùng
                final String[] listItems = new String[]{"Male", "Female"};

                // xây dựng hộp thoại cảnh báo với lựa chọn một mục duy nhất
                alertDialog.setSingleChoiceItems(listItems, checkedItem[0], (dialog, which) -> {
                    // cập nhật mục đã chọn được người dùng chọn sao cho nên chọn mục đó
                    // khi người dùng mở hộp thoại vào lần tiếp theo và chuyển thể hiện sang phương thức setSingleChoiceItems
                    checkedItem[0] = which;

                    // cập nhật TextView để xem trước mục đã chọn
                    bottomSheetEditAccountLayoutBinding.editTextGender.setText(listItems[which]);
                    // khi được chọn một mục, hộp thoại sẽ được đóng bằng phương thức loại bỏ
                    dialog.dismiss();
                });

                // tạo và xây dựng phiên bản AlertDialog bằng phiên bản trình tạo AlertDialog
                AlertDialog customAlertDialog = alertDialog.create();

                // hiển thị hộp thoại cảnh báo khi nhấn nút
                customAlertDialog.show();
            });
        }

        //Chon ngay sinh
        public void selectDate(){

        // Set OnClickListener for the birthday TextView
        bottomSheetEditAccountLayoutBinding.editTextBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo một phiên bản DatePickerDialog mới
                Calendar calendar = Calendar.getInstance();
                //Lay ngay thang nam hien tai
                int day = calendar.get(Calendar.DATE);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MyAccountActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Gan lai cac ngay thang nam minh chon
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        //Gan ngay thang nam vao edit text
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        bottomSheetEditAccountLayoutBinding.editTextBirthday.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, year, month, day);
                // Show ra dialog
                datePickerDialog.show();
            }
        });
        //Kiem tra xem ngay sinh co hop le khong
        bottomSheetEditAccountLayoutBinding.editTextBirthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 10) {
                    // Lay ngay thang nam sinh tu ng nhap
                    int birthYear = Integer.parseInt(s.toString().substring(6));
                    int birthMonth = Integer.parseInt(s.toString().substring(3, 5));
                    int birthDay = Integer.parseInt(s.toString().substring(0, 2));
                    //Lay ngay hien tai de so sanh
                    Calendar today = Calendar.getInstance();
                    //Tinh tuoi
                    int age = today.get(Calendar.YEAR) - birthYear;
                    // Kiểm tra xem ngày hiện tại có trước sinh nhật năm nay không
                    //17/8/2004  15/5/2024
                    if (today.get(Calendar.MONTH) < birthMonth ||
                            (today.get(Calendar.MONTH) == birthMonth && today.get(Calendar.DAY_OF_MONTH) < birthDay)) {
                        age--;
                    }

                    if (age < 14) {
                        isCalendar = false;
                        bottomSheetEditAccountLayoutBinding.textInputLayoutBirthday.setError("Bạn phải trên 14 tuổi");
                    } else {
                        isCalendar = true;
                        bottomSheetEditAccountLayoutBinding.textInputLayoutBirthday.setError(null);
                    }
                } else {
                    isCalendar = false;
                    bottomSheetEditAccountLayoutBinding.textInputLayoutBirthday.setError("Định dạng ngày không hợp lệ");
                }
                //Cap nhat trang thai
                updateStateButtonEdit();
            }
        });
        //       Theo doi nhap text ten hien thi
            bottomSheetEditAccountLayoutBinding.editTextName.addTextChangedListener(new TextWatcher() {
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
                        bottomSheetEditAccountLayoutBinding.textInputLayoutName.setError(getString(R.string.b_n_ph_i_tr_n_6_k_t_cho_t_n_hi_n_th));
                    } else {
                        isNameValid = true;
                        bottomSheetEditAccountLayoutBinding.textInputLayoutName.setError(null);
                    }
                    //cap nhat trang thai
                    updateStateButtonEdit();
                }
            });

    }

     //Gan thong tin user vao man hinh chinh
    public void informationUser(){
        //Gan ten va image ten user tu firebase ve
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String imageUrl = dataSnapshot.child("imageUser").getValue(String.class);
                        String dateCreateAccount = dataSnapshot.child("registrationDate").getValue(String.class);
                        String phone = dataSnapshot.child("phone").getValue(String.class);
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String birthday = dataSnapshot.child("birthday").getValue(String.class);
                        String gender = dataSnapshot.child("gender").getValue(String.class);
                        //Set ten nguoi dung
                        binding.nameTextView.setText(name);
                        //Set gioi tinh nguoi dung
                        binding.gender.setText(gender);
                        //Set ngay sinh nguoi dung
                        binding.birthday.setText(birthday);
                        //Set ngay tao tai khoan
                        binding.joinedDateTextView.setText("Ngày tham gia : " + dateCreateAccount);
                        // Load image  using Glide
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(MyAccountActivity.this)
                                    .load(imageUrl)
                                    .into(binding.avatar);
                        } else {
                            //Set anh mac dinh
                            binding.avatar.setImageResource(R.drawable.avatar);
                        }
                        //Set phone email vao adapter
                        updateAdapterData(email != null ? email : "", phone != null ? phone : "");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Tag",databaseError.getMessage());
                }
            });
        }
    }

    // Do du lieu vao bottom sheet edit
    public void informationUserEdit(){
        //Gan ten va image ten user tu firebase ve
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String imageUrl = dataSnapshot.child("imageUser").getValue(String.class);
                        String birthday = dataSnapshot.child("birthday").getValue(String.class);
                        String gender = dataSnapshot.child("gender").getValue(String.class);
                        //Gan ten
                        bottomSheetEditAccountLayoutBinding.editTextGender.setText(gender);
                        bottomSheetEditAccountLayoutBinding.editTextBirthday.setText(birthday);
                        bottomSheetEditAccountLayoutBinding.editTextName.setText(name);
                        // Load image uri using Glide
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(MyAccountActivity.this)
                                    .load(imageUrl)
                                    .into(bottomSheetEditAccountLayoutBinding.avatar);
                        } else {
                            //Set anh mac dinh
                            bottomSheetEditAccountLayoutBinding.avatar.setImageResource(R.drawable.avatar);
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Tag",databaseError.getMessage());
                }
            });
        }
//    Test save

    }

    //Lay du lieu tu ng dung
    private void saveUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        String name = bottomSheetEditAccountLayoutBinding.editTextName.getText().toString();
        String gender = bottomSheetEditAccountLayoutBinding.editTextGender.getText().toString();
        String birthday = bottomSheetEditAccountLayoutBinding.editTextBirthday.getText().toString();
        // Kiểm tra xem Uri hình ảnh có sẵn không
        if (imageUri != null) {
            // Phương thức uploadImageAndUpdateInfo tải ảnh lên và cập nhật thông tin người dùng
            uploadImageAndUpdateInfo(imageUri, userId, name, gender, birthday);
        } else {
            updateUserInfo(userId, name, gender, birthday, null);
        }
    }

    // Phương thức upload anh len Storage
    private void uploadImageAndUpdateInfo(Uri imageUri, String userId, String name, String gender, String birthday) {
        // Tạo tham chiếu đến vị trí lưu trữ của Firebase Storage, sử dụng userId để xác định thư mục lưu trữ
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("users/" + userId + "/profile.jpg");
        // Tải tệp ảnh lên vị trí đã chỉ định
        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Khi tải lên thành công, lấy URL của ảnh đã tải lên
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        // Gọi phương thức updateUserInfo để cập nhật thông tin người dùng với URL của ảnh
                        updateUserInfo(userId, name, gender, birthday, imageUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Nếu việc tải lên thất bại, hiển thị thông báo lỗi
                    Toast.makeText(MyAccountActivity.this, "Tải ảnh lên thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
     // Day thong tin len firebase
     // Phương thức updateUserInfo cập nhật thông tin người dùng trong cơ sở dữ liệu
     private void updateUserInfo(String userId, String name, String gender, String birthday, String imageUrl) {
         bottomSheetEditAccountLayoutBinding.btnSave.setText("Đang lưu...");

         // Tạo tham chiếu đến vị trí người dùng trong Firebase Database
         DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

         // Tạo một map để lưu trữ các cập nhật
         Map<String, Object> updates = new HashMap<>();

         if (name != null) {
             updates.put("name", name);
         }
         if (gender != null) {
             updates.put("gender", gender);
         }
         if (birthday != null) {
             updates.put("birthday", birthday);
         }
         if (imageUrl != null) {
             updates.put("imageUser", imageUrl);
         }

         // Cập nhật thông tin người dùng trong cơ sở dữ liệu
         databaseReference.updateChildren(updates)
                 .addOnSuccessListener(aVoid -> {
                     // Khi cập nhật thành công, hiển thị thông báo thành công và đóng bottom sheet
                     Toast.makeText(MyAccountActivity.this, "Sửa thông tin thành công", Toast.LENGTH_SHORT).show();
                     bottomSheetDialogEdit.dismiss();

                     // Cập nhật lại thông tin người dùng trên man hinh chinh
                     informationUser();

                     // Đổi lại văn bản của nút "Lưu" về trạng thái ban đầu
                     bottomSheetEditAccountLayoutBinding.btnSave.setText("Lưu");
                 })
                 .addOnFailureListener(e -> {
                     // Nếu cập nhật thất bại, hiển thị thông báo lỗi
                     Toast.makeText(MyAccountActivity.this, "Sửa thông tin thất bại", Toast.LENGTH_SHORT).show();
                 });
     }


}

