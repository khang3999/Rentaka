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
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.rentaka.APIs.FirebaseAPI;
import vn.edu.tdc.rentaka.APIs.RealTimeAPI;
import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.CreateCarLayoutBinding;
import vn.edu.tdc.rentaka.models.Car;
import vn.edu.tdc.rentaka.models.City;

public class CreateCarActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private Uri imageUriCar;
    private Uri imageUriInspection;
    private Uri imageUriInsurance;

    private Uri imageUriRegister;

    private String userId;
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
    private boolean isValidDescription = false;
    private ArrayAdapter<String> adapterCity;
    private ArrayList<City> listCities;
    private ArrayList<String> listCitiesName;
    private int tapOnImage;
    private RealTimeAPI realTimeAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = CreateCarLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo real time database
        realTimeAPI = new RealTimeAPI();
        listCities = new ArrayList<>();
        realTimeAPI.fetchCities(new RealTimeAPI.FetchListener<City>() {
            @Override
            public void onFetched(List<City> list) {
                listCitiesName = new ArrayList<>();
                listCities.addAll(list);
                for (City c : listCities) {
                    listCitiesName.add(c.getName());
                    Log.d("citi", "onCreate: " + c.getName());
                }

                adapterCity = new ArrayAdapter<String>(CreateCarActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listCitiesName);
                binding.spinnerLocation.setAdapter(adapterCity);

//
            }

            @Override
            public void onError(Exception e) {

            }
        });



//        // Lay id user dang dang nhap, dung thu vien FirebaseUser
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            String id = user.getUid();
//            this.userId = id;
//        }


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
                String regex = "^([a-zA-Z]{1,})$";
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
                String regex = "^([a-zA-Z0-9]{1,})$";
                if (s.toString().matches(regex)) {
                    isValidModel = true;
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
                String regex = "^[a-zA-Z0-9]{1,}$";
                if (s.toString().matches(regex)) {
                    isValidLicense = true;
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
                String regex = "^[a-zA-Z]{1,}$";
                if (s.toString().matches(regex)) {
                    isValidColor = true;
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
        // Required input description
        binding.editTextDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // It nhat 1 ký tu
                String regex = "^.{1,}$";
                if (s.toString().matches(regex)) {
                    isValidDescription = true;
                    binding.editTextDescription.setError(null);
                } else {
                    isValidDescription = false;
                    binding.editTextDescription.setError("Please enter at least 1 characters");
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
        binding.editTextPriceDaily.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // >= 100.000 VND
                String regex = "^((1[0-9]{2,}000|[2-9][0-9]{2,}000)|0)$";
                if (s.toString().matches(regex)) {
                    isValidPriceSelf = true;
                    binding.editTextPriceDaily.setError(null);
                } else {
                    isValidPriceSelf = false;
                    binding.editTextPriceDaily.setError("Minimum denomination 100.000 VND");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Check regex price driver
        binding.editTextSalaryDriver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // >= 100.000
                String regex = "^((1[0-9]{2,}000|[2-9][0-9]{2,}000)|0)$";
                if (s.toString().matches(regex)) {
                    isValidPriceDriver = true;
                    binding.editTextSalaryDriver.setError(null);
                } else {
                    isValidPriceDriver = false;
                    binding.editTextSalaryDriver.setError("Minimum denomination 100.000 VND");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //CHON HINH
        // Set up the image picker launcher
        // Khởi tạo ActivityResultLauncher để mở bộ chọn ảnh và nhận kết quả trả về
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // Kiểm tra nếu kết quả trả về là thành công (RESULT_OK) và có dữ liệu (data không null)
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        if (tapOnImage == 0) {
                            imageUriCar = result.getData().getData(); // Lấy Uri của ảnh được chọn từ kết quả trả về
                            binding.imageCar.setImageURI(imageUriCar);
                        } else if (tapOnImage == 1) {
                            imageUriInspection = result.getData().getData(); // Lấy Uri của ảnh được chọn từ kết quả trả về
                            binding.imageInspection.setImageURI(imageUriInspection);
                        } else if (tapOnImage == 2) {
                            imageUriInsurance = result.getData().getData(); // Lấy Uri của ảnh được chọn từ kết quả trả về
                            binding.imageInsurance.setImageURI(imageUriInsurance);
                        } else {
                            imageUriRegister = result.getData().getData(); // Lấy Uri của ảnh được chọn từ kết quả trả về
                            binding.imageRegistration.setImageURI(imageUriRegister);
                        }

                    }
                }
        );
//        Set event click to pick image for image car
        binding.imageCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapOnImage = 0;
                checkAndRequestPermissions();
            }
        });

        // Set event click to pick image for image inspection
        binding.imageInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapOnImage = 1;
                checkAndRequestPermissions();
            }
        });
        // Set event click to pick image for image insurance
        binding.imageInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapOnImage = 2;
                checkAndRequestPermissions();
            }
        });
        // Set event click to pick image for image registration
        binding.imageRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tapOnImage = 3;
                checkAndRequestPermissions();
            }
        });
        // Add event for button register
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean valid = isValidBrand && isValidModel && isValidSince
                        && isValidLicense && isValidColor && isValidFuel && isValidType
                        && isValidSeat && isValidMortgage && isValidPriceDriver && isValidPriceSelf;
                if (valid) {
                    //Create car và đưa lên cơ sơ dữ liêu
                    Car car = new Car();
                    car.setOwnerID(userId);
                    car.setBrand(binding.autoCompleteBrand.getText().toString());
                    car.setModel(binding.editTextModel.getText().toString());
                    car.setSince(Integer.parseInt(binding.editTextSince.getText().toString()));
                    car.setLicensePlate(binding.editTextLicensePlate.getText().toString());
                    car.setColor(binding.editTextColor.getText().toString());
                    car.setDescription(binding.editTextDescription.getText().toString());
                    // Create City
                    City city = listCities.get(binding.spinnerLocation.getSelectedItemPosition());
                    car.setCity(city);
                    // Set fuel
                    int idFuel = binding.groupChooseFuel.getCheckedRadioButtonId();
                    RadioButton radFuelChoose = findViewById(idFuel);
                    car.setFuel(radFuelChoose.getText().toString());
                    // Set gearBox
                    int idType = binding.groupChooseType.getCheckedRadioButtonId();
                    RadioButton radTypeChoose = findViewById(idType);
                    car.setTypeGearbox(radTypeChoose.getText().toString());
                    // Set Seat
                    int idSeat = binding.groupChooseSeat.getCheckedRadioButtonId();
                    RadioButton radSeatChoose = findViewById(idSeat);
                    car.setSeat(Integer.parseInt(radSeatChoose.getText().toString()));
                    // Set mortgage
                    double m = 0.0;
                    try {
                        m = Double.parseDouble(binding.editTextMortgageMoney.getText().toString());
                    } catch (NumberFormatException e) {
                        m = 0.0;
                    }
                    double priceDaily = 0.0;
                    try {
                        priceDaily = Double.parseDouble(binding.editTextPriceDaily.getText().toString());
                    } catch (NumberFormatException e) {
                        priceDaily = 0.0;
                    }
                    double salary = 0.0;
                    try {
                        salary = Double.parseDouble(binding.editTextSalaryDriver.getText().toString());
                    } catch (NumberFormatException e) {
                        salary = 0.0;
                    }
                    car.setMortgage(m);
                    // Set price daily
                    car.setPriceDaily(priceDaily);
                    // Set salary driver
                    car.setPriceDaily(salary);

                    realTimeAPI.createNewCar(userId = "scGmRrT8oHMXwRUErikbmLKmF213", car, imageUriCar, imageUriInspection, imageUriInsurance, imageUriRegister, CreateCarActivity.this);

                } else {
                    Snackbar snackbar = Snackbar.make(v, "Please input all fields! ", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

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
}