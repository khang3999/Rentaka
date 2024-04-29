package vn.edu.tdc.rentaka.activities;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.DrivingLicenseLayoutBinding;

public class DrivingLicenseActivity extends AppCompatActivity {
   private DrivingLicenseLayoutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driving_license_layout);
        binding = DrivingLicenseLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //SET HTML CHO TEXTVIEW (CUSTOM )
        String textNote = "<b>Lưu ý:</b>Để tránh phát sinh vấn đề trong quá trình thuê xe, <u>người đặt xe</u> trên Mioto (đã xác thực giấy phép lái xe) <b>ĐỒNG THỜI</b> phải là <u>người nhận xe</u>.";
        String textGPLX = "Hình chụp cần thấy <b> Ảnh đại diện</b> và <b> Số GPLX </b>";
        String textNote2 = "<b><u>Vì sao tôi phải Xác thực GPLX </u></b>";
        binding.textNote.setText(Html.fromHtml(textNote, Html.FROM_HTML_MODE_COMPACT));
        binding.textGPLX.setText(Html.fromHtml(textGPLX, Html.FROM_HTML_MODE_COMPACT));
        binding.textNote2.setText(Html.fromHtml(textNote2, Html.FROM_HTML_MODE_COMPACT));
    }
}