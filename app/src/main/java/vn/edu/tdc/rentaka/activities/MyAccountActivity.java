package vn.edu.tdc.rentaka.activities;

import vn.edu.tdc.rentaka.R;

import android.content.DialogInterface;
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
import vn.edu.tdc.rentaka.databinding.MyAccountLayoutBinding;
import vn.edu.tdc.rentaka.models.MyAccountModel;

public class MyAccountActivity extends AppCompatActivity {
    MyAccountAdapter adapter;
    private MyAccountLayoutBinding binding;
    private BottomSheetEditAccountLayoutBinding bottom_sheet_biding;
    BottomSheetDialog bottomSheetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MyAccountLayoutBinding.inflate(getLayoutInflater());
        bottom_sheet_biding = BottomSheetEditAccountLayoutBinding.inflate(getLayoutInflater(), null, false);
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
        bottomSheetDialog = new BottomSheetDialog(
                MyAccountActivity.this, R.style.BottomSheetDialogTheme
        );

        bottomSheetDialog.setContentView(bottom_sheet_biding.getRoot());
        bottom_sheet_biding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyAccountActivity.this, "Save success", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });
        //Button edit
        binding.rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });

        //Button delete
        bottom_sheet_biding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        //Mutichoice giới tính
        // Set OnClickListener for the gender TextView
        bottom_sheet_biding.gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] genders = {"Male", "Female"};
                // Create AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(MyAccountActivity.this);
                builder.setTitle("Select Gender");
                builder.setSingleChoiceItems(genders, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Set the selected gender to the TextView
                        bottom_sheet_biding.gender.setText(genders[which]);
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
}
