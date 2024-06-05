package vn.edu.tdc.rentaka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.ChooseLocationLayoutBinding;
import vn.edu.tdc.rentaka.databinding.ConfirmRentalLayoutBinding;
import vn.edu.tdc.rentaka.databinding.ConfirmRentalOwnerLayoutBinding;
import vn.edu.tdc.rentaka.databinding.RentalDetailLayoutBinding;
import vn.edu.tdc.rentaka.models.Car;
import vn.edu.tdc.rentaka.models.UserModel;

public class ConfirmRentalActivity extends AppCompatActivity {
    private ConfirmRentalLayoutBinding binding;
    private ArrayList<LocalDate> listDateUnavailable;
    private Intent intent;
    private String cityName;
    private String startDate;
    private String endDate;
    private String carId;
    private String imageCarUrl;
    private String imageOwnerUrl;
    private int priceDaily;
    private Car car;
    private UserModel owner;
    private ArrayList<LocalDate> listDateBlocked;
    private int requiredDriver = 0;
    private int totalsDay = 0;
    private NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ConfirmRentalLayoutBinding.inflate(getLayoutInflater());
        // Gán view cho binding
        setContentView(binding.getRoot());

        // Khởi tạo list date blocked
        listDateBlocked = new ArrayList<>();

        // khoi tao list data unavailable
        listDateUnavailable = new ArrayList<>();
        // Bắt sự kiện tap vao icon calendar mở lịch
        binding.buttonChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyen sang activity chon thoi gian
                Intent intent = new Intent(ConfirmRentalActivity.this, ConfirmChooseDateActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();


        // Hien thi thong tin cua xe da chon, dua theo carId, lay du lieu tu firebase
        if (intent != null && intent.hasExtra("carId")) { // Neu chuyen tu man hinh detail moi co carId do set intent moi
            carId = intent.getStringExtra("carId");
            DatabaseReference carReference = FirebaseDatabase.getInstance().getReference("cars");
            carReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot userSnap : snapshot.getChildren()) {// lay tat ca user
                        for (DataSnapshot carSnap : userSnap.getChildren()) { // tim trong tung user xem co carId naof trung thi break
                            car = new Car();
                            car = carSnap.getValue(Car.class);
                            Log.d("tesssss", "carrental: " + car);
                            if (car.getId().equals(carId)) {
                                break;
                            }
                        }
                        if (car.getId().equals(carId)) {
                            break;
                        }
                    }
                    // SET THONG TIN XE
                    Log.d("tesssss", "carrental: ngoai " + car);
                    imageCarUrl = car.getImageCarUrl();
                    // Set hinh anh xe
                    if (imageCarUrl != null && !imageCarUrl.isEmpty()) {
                        Glide.with(ConfirmRentalActivity.this)
                                .load(imageCarUrl)
                                .into(binding.imageCar);
                    } else {
                        //Set anh mac dinh
                        binding.imageCar.setImageResource(R.drawable.car);
                    }

                    binding.carModelTextview.setText(car.getBrand() + " " + car.getModel());
                    binding.carColor.setText("Màu " + car.getColor());
                    binding.seatNumber.setText(car.getSeat() + " chỗ");
                    // Set ten city
                    binding.locationTextview.setText(car.getCity().getName());
                    // Set gia theo xe
                    binding.rentalPriceTextview.setText(numberFormat.format(car.getPriceDaily()) + " / Ngày");
                    // Set gia tong ket
                    binding.rentalTotalPriceTextview.setText("0");
                    binding.depositThroughAppTextview.setText("0");
                    binding.payUponRetrievingCarTextview.setText("0");
                    binding.finalAmountTextview.setText("0");

//                    binding.radGroup.getCheckedRadioButtonId();
                    binding.radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (binding.radNoRequireDriver.getId() == checkedId){
                                priceDaily = car.getPriceDaily();
                            } else if(binding.radRequireDriver.getId() == checkedId) {
                                priceDaily = car.getPriceDaily() + car.getSalaryDriver();
                            }
                            binding.rentalPriceTextview.setText(numberFormat.format(priceDaily)+" / Ngày");
                        }
                    });

                    

                    // SET THONG TIN CHU XE
                    // Lay thong tin chu xe sau khi lay duoc xe
                    DatabaseReference ownerReference = FirebaseDatabase.getInstance().getReference("users");
                    ownerReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot snapshotId : snapshot.getChildren()) {
                                if (snapshotId.getKey().equals(car.getOwnerID())) {
                                    owner = snapshotId.getValue(UserModel.class);
                                    break;
                                }
                            }
                            Log.d("tesssss", "owner: " + owner);
                            imageOwnerUrl = owner.getImageUser();
                            // Set hinh chu xe
                            if (imageOwnerUrl != null && !imageOwnerUrl.isEmpty()) {
                                Glide.with(ConfirmRentalActivity.this)
                                        .load(imageOwnerUrl)
                                        .into(binding.profileImg);
                            } else {
                                //Set anh mac dinh
                                binding.profileImg.setImageResource(R.drawable.avatar);
                            }

                            binding.ownerNameTextview.setText(owner.getName());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        // End flow intent from detail
        // Intent tu chon ngay: ConfirmChooseDate

    }
//     END onResume
}
