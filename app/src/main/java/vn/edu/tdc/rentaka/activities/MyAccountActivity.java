package vn.edu.tdc.rentaka.activities;

import vn.edu.tdc.rentaka.R;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import vn.edu.tdc.rentaka.adapters.MyAccountAdapter;
import vn.edu.tdc.rentaka.databinding.BottomSheetEditAccountLayoutBinding;
import vn.edu.tdc.rentaka.databinding.BottomSheetPhoneNumberLayoutBinding;
import vn.edu.tdc.rentaka.databinding.MyAccountLayoutBinding;
import vn.edu.tdc.rentaka.models.MyAccountModel;

public class MyAccountActivity extends AppCompatActivity {
    MyAccountAdapter adapter;
    private MyAccountLayoutBinding binding;
    private BottomSheetEditAccountLayoutBinding bottomSheetEditAccountLayoutBinding;
    private BottomSheetPhoneNumberLayoutBinding bottomSheetPhoneNumberLayoutBinding;
    BottomSheetDialog bottomSheetDialogEdit;
    BottomSheetDialog bottomSheetDialogPhone;
    BottomSheetDialog bottomSheetDialogIDMESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MyAccountLayoutBinding.inflate(getLayoutInflater());
        //Lay man hinh nen de len man hinh chinh
        bottomSheetEditAccountLayoutBinding = BottomSheetEditAccountLayoutBinding.inflate(getLayoutInflater(), null, false);
        bottomSheetPhoneNumberLayoutBinding = BottomSheetPhoneNumberLayoutBinding.inflate(getLayoutInflater(), null, false);
        setContentView(binding.getRoot());
        ArrayList<MyAccountModel> dataFuc = new ArrayList<>();
        dataFuc.add(new MyAccountModel("Giấy phép lái xe", "Thêm giấy phép lái xe"));
        dataFuc.add(new MyAccountModel("Điện thoại", "Thêm số điện thoại"));
        dataFuc.add(new MyAccountModel("Email", "Thêm gmail"));
        dataFuc.add(new MyAccountModel("Facebook", "Thêm facebook"));
        dataFuc.add(new MyAccountModel("Google", "Liên kết ngay"));
        adapter = new MyAccountAdapter(this, dataFuc);

        // Tạo đối tượng layout manager của Recycler View
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rycAcount.setLayoutManager(layoutManager);
        binding.rycAcount.setAdapter(adapter);

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
        bottomSheetDialogPhone = new BottomSheetDialog(
                MyAccountActivity.this, R.style.BottomSheetDialogTheme
        );

// Đặt nội dung view của BottomSheetDialog là root view của bottomSheetEditAccountLayoutBinding
        bottomSheetDialogEdit.setContentView(bottomSheetEditAccountLayoutBinding.getRoot());

// Đặt nội dung view của BottomSheetDialog là root view của bottomSheetPhoneNumberLayoutBinding
        bottomSheetDialogPhone.setContentView(bottomSheetPhoneNumberLayoutBinding.getRoot());

// Đặt một OnClickListener cho nút lưu trong bottomSheetEditAccountLayoutBinding

        bottomSheetEditAccountLayoutBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyAccountActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                bottomSheetDialogEdit.dismiss();
            }
        });



        //Button edit
        binding.rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogEdit.show();
            }
        });
        //Button so dien thoai
        //Button edit


        //Button delete
        bottomSheetEditAccountLayoutBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogEdit.dismiss();
            }
        });
        bottomSheetPhoneNumberLayoutBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialogPhone.dismiss();
            }
        });

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
                        bottomSheetDialogPhone.show();
                        break;
                    case 2:
                        Toast.makeText(MyAccountActivity.this, "Email", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(MyAccountActivity.this, "Facebook", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(MyAccountActivity.this, "Google", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(MyAccountActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        // Xu ly nhap gender
        selectGender();
        // Xu ly nhap ngay sinh
        selectDate();
    }
      //Chon gioi tinh
        public void selectGender() {
            // Set OnClickListener for the gender TextView
            final int[] checkedItem = {-1};
            // xử lý nút để mở hộp thoại cảnh báo với lựa chọn mục duy nhất khi được nhấp vào
            bottomSheetEditAccountLayoutBinding.gender.setOnClickListener(v -> {
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

                    // bây giờ cũng cập nhật TextView để xem trước mục đã chọn
                    bottomSheetEditAccountLayoutBinding.gender.setText(listItems[which]);
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
        bottomSheetEditAccountLayoutBinding.birthday.setOnClickListener(new View.OnClickListener() {
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
                        bottomSheetEditAccountLayoutBinding.birthday.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                }, year, month, day);
                // Show ra dialog
                datePickerDialog.show();
            }
        });
        //Kiem tra xem ngay sinh co hop le khong
        bottomSheetEditAccountLayoutBinding.birthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Kiem tra xem ngay sinh co hop le khong
                if (s != null && s.length() < 10 ){
                    bottomSheetEditAccountLayoutBinding.textInputLayoutBirthday.setError("Ngày sinh không hợp lệ");
                }
                else {
                    bottomSheetEditAccountLayoutBinding.birthday.setError(null);
                }
            }
        });

    }
    }

