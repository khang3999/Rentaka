package vn.edu.tdc.rentaka.activities;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.BottomSheetDiaglogLayoutBinding;
import vn.edu.tdc.rentaka.databinding.BottomSheetShowCancelPolicyLayoutBinding;
import vn.edu.tdc.rentaka.databinding.RentalDetailLayoutBinding;

public class RentalDetailActivity extends AppCompatActivity {

    private BottomSheetDialog bottomSheetDialog;
    private RentalDetailLayoutBinding rentalBinding;
    private BottomSheetDiaglogLayoutBinding bottom_sheet_binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rentalBinding = RentalDetailLayoutBinding.inflate(getLayoutInflater());
        bottom_sheet_binding = BottomSheetDiaglogLayoutBinding.inflate(getLayoutInflater(),null,false);
        setContentView(rentalBinding.getRoot());

        //Gach chan text
        rentalBinding.btnMore.setPaintFlags(rentalBinding.btnMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
         //Bottom sheet
        bottomSheetDialog = new BottomSheetDialog(
                RentalDetailActivity.this, R.style.BottomSheetDialogTheme
        );
        bottomSheetDialog.setContentView(bottom_sheet_binding.getRoot());
        //Bat su kien khi click vao Xem them
        rentalBinding.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });
        //Bat su kien khi click vao x de tat bottom sheet
        bottom_sheet_binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
    }
}