package vn.edu.tdc.rentaka.activities;

import vn.edu.tdc.rentaka.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

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
        bottomSheetPhoneNumberLayoutBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyAccountActivity.this, "Lưu thành công", Toast.LENGTH_SHORT).show();
                bottomSheetDialogPhone.dismiss();
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
        //Mutichoice giới tính
        // Set OnClickListener for the gender TextView
        bottomSheetEditAccountLayoutBinding.gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] genders = {"Male", "Female"};
                // Create AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountActivity.this);
                builder.setTitle("Select Gender");
                builder.setSingleChoiceItems(genders, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int postion) {
                        // Set the selected gender to the TextView
                        bottomSheetEditAccountLayoutBinding.gender.setText(genders[postion]);
                        dialog.dismiss();
                    }
                });
                builder.show();
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
    }
}
