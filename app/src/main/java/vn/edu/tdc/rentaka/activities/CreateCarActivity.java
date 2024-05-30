package vn.edu.tdc.rentaka.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.APIs.FirebaseAPI;
import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.CreateCarLayoutBinding;
import vn.edu.tdc.rentaka.models.Car;

public class CreateCarActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private static final int STORAGE_PERMISSION_CODE = 101;
    Uri imageUri;
    private CreateCarLayoutBinding binding;
    private boolean isValidBrand = false;
    private boolean isValidModel = false;
    private boolean isValidSince = false;
    private boolean isValidLicense = false;
    private boolean isValidColor = false;
    private boolean isValidFuel = false;
    private boolean isValidType = false;
    private boolean isValidSeat = false;
    private boolean isValidMortgage = false;
    private boolean isValidPriceSelf = false;
    private boolean isValidPriceDriver = false;

    private FirebaseAPI firebaseAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CreateCarLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo firebaseAPI
        firebaseAPI = new FirebaseAPI();

        //Button top back navigation
        setSupportActionBar(binding.topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateCarActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                // Main vao tu trai, choose date exit ve ben phai
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });
        // Required input brand
        binding.autoCompleteBrand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // It nhat 1 ký tu
                String regex = "^[a-z]{1,}$";
                if (s.toString().matches(regex)) {
                    isValidBrand = true;
                    binding.autoCompleteBrand.setError(null);
                } else {
                    isValidBrand = false;
                    binding.autoCompleteBrand.setError("Please enter at least 1 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Required input model
        binding.editTextModel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // It nhat 1 ký tu
                String regex = "^[a-z]{1,}$";
                if (s.toString().matches(regex)) {
                    isValidModel= true;
                    binding.editTextModel.setError(null);
                } else {
                    isValidModel = false;
                    binding.editTextModel.setError("Please enter at least 1 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Check regex since
        binding.editTextSince.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 1900 ~ 2024
                String regex = "^(19\\d{2}|20[0-1]\\d|202[0-4])$";
                if (s.toString().matches(regex)) {
                    isValidSince = true;
                    binding.editTextSince.setError(null);
                } else {
                    isValidSince = false;
                    binding.editTextSince.setError("Year from 1900 to 2024");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Required input license plate
        binding.editTextLicensePlate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // It nhat 1 ký tu
                String regex = "^[a-z]{1,}$";
                if (s.toString().matches(regex)) {
                    isValidLicense= true;
                    binding.editTextLicensePlate.setError(null);
                } else {
                    isValidLicense = false;
                    binding.editTextLicensePlate.setError("Please enter at least 1 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Required input color
        binding.editTextColor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // It nhat 1 ký tu
                String regex = "^[a-z]{1,}$";
                if (s.toString().matches(regex)) {
                    isValidColor= true;
                    binding.editTextColor.setError(null);
                } else {
                    isValidColor = false;
                    binding.editTextColor.setError("Please enter at least 1 characters");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Required fuel
        binding.groupChooseFuel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isValidFuel = true;
            }
        });

        // Required type
        binding.groupChooseType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isValidType = true;
            }
        });

        // Required seat
        binding.groupChooseSeat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isValidSeat = true;
            }
        });

        // Check regex money mortgage
        binding.editTextMortgageMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // o or >= 1000
                String regex = "^(0|\\d{1,}000)$";
                if (s.toString().matches(regex)) {
                    isValidMortgage = true;
                    binding.editTextMortgageMoney.setError(null);
                } else {
                    isValidMortgage = false;
                    binding.editTextMortgageMoney.setError("0 or a valid currency denomination");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Check regex price self
        binding.editTextPriceSelf.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // >= 100.000 VND
                String regex = "^(19\\d{2}|20[0-1]\\d|202[0-4])$";
                if (s.toString().matches(regex)) {
                    isValidPriceSelf = true;
                    binding.editTextPriceSelf.setError(null);
                } else {
                    isValidPriceSelf = false;
                    binding.editTextPriceSelf.setError("Minimum denomination 100.000 VND");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Check regex price driver
        binding.editTextPriceDriver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // >= 100.000
                String regex = "^(19\\d{2}|20[0-1]\\d|202[0-4])$";
                if (s.toString().matches(regex)) {
                    isValidPriceDriver = true;
                    binding.editTextPriceDriver.setError(null);
                } else {
                    isValidPriceDriver = false;
                    binding.editTextPriceDriver.setError("Minimum denomination 100.000 VND");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Add event for button register
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = isValidBrand && isValidModel && isValidSince
                        && isValidLicense && isValidColor && isValidFuel && isValidType
                        && isValidSeat && isValidMortgage && isValidPriceDriver && isValidPriceSelf;
                if (valid){
                    Log.d("btn regis", "onClick: trueeeee");
                }
                else {
                    Snackbar snackbar = Snackbar.make(v, "Vui long ", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Set default
        binding.autoCompleteBrand.setText("");
        binding.editTextModel.setText("");
        binding.editTextSince.setText("");
        binding.editTextLicensePlate.setText("");
        binding.editTextColor.setText("");
        binding.groupChooseFuel.clearCheck();
        binding.groupChooseType.clearCheck();
        binding.groupChooseSeat.clearCheck();
        binding.editTextMortgageMoney.setText("");
        binding.editTextPriceDriver.setText("");
        binding.editTextPriceSelf.setText("");

    }
}