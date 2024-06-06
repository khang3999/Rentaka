package vn.edu.tdc.rentaka.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.ChooseLocationLayoutBinding;
import vn.edu.tdc.rentaka.databinding.ConfirmRentalLayoutBinding;
import vn.edu.tdc.rentaka.databinding.ConfirmRentalOwnerLayoutBinding;
import vn.edu.tdc.rentaka.databinding.RentalDetailLayoutBinding;
import vn.edu.tdc.rentaka.models.Car;
import vn.edu.tdc.rentaka.models.Date;
import vn.edu.tdc.rentaka.models.Notification;
import vn.edu.tdc.rentaka.models.Order;
import vn.edu.tdc.rentaka.models.Status;
import vn.edu.tdc.rentaka.models.UserModel;

public class ConfirmRentalActivity extends AppCompatActivity {
    private ConfirmRentalLayoutBinding binding;
    private String startDate;
    private String endDate;
    private String timeStart;
    private String timeEnd;
    private String carId;
    private String imageCarUrl;
    private String imageOwnerUrl;
    private int priceDaily;
    private Car car;
    private UserModel owner = new UserModel();
    private UserModel author = new UserModel();
    private ArrayList<String> listDateBlockedStr;
//    private ArrayList<String> listDateChooseStr;
    private ArrayList<LocalDate> listDateChoose;
    private ArrayList<LocalDate> listDateBlocked;

    private  Status ownerStatus;
    private  Status customerStatus;

    private int totalDate = 0;
    private String ownerId;
    private NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ConfirmRentalLayoutBinding.inflate(getLayoutInflater());
        // Gán view cho binding
        setContentView(binding.getRoot());

        // Khởi tạo list date blocked
        listDateBlockedStr = new ArrayList<>();
//        listDateChooseStr = new ArrayList<>();
        listDateBlocked = new ArrayList<>();
        listDateChoose = new ArrayList<>();

        //Button top back navigation
        setSupportActionBar(binding.topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmRentalActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        // Get user author
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.getUid() != null) {
            String authorId = user.getUid();
            DatabaseReference userIdRef = FirebaseDatabase.getInstance().getReference("users").child(authorId);
            userIdRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    author = snapshot.getValue(UserModel.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

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
                            ownerId = car.getOwnerID();
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
                    binding.rentalTotalDateTextview.setText("0");
                    binding.depositThroughAppTextview.setText("0");
                    binding.payUponRetrievingCarTextview.setText("0");
                    binding.finalAmountTextview.setText("0");


                    // Set mac dinh gia
                     priceDaily = car.getPriceDaily();
                    Log.d("price", "onCheckedChanged: "+priceDaily);

//                    binding.radGroup.getCheckedRadioButtonId();
                    binding.radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            if (binding.radNoRequireDriver.getId() == checkedId){
                                priceDaily = car.getPriceDaily();
                            } else if(binding.radRequireDriver.getId() == checkedId) {
                                priceDaily = car.getPriceDaily() + car.getSalaryDriver();
                            }

                            int finalAmount = totalDate * priceDaily;

                            binding.finalAmountTextview.setText(numberFormat.format(finalAmount)+"");
                            int commission = (int) (finalAmount * 0.2);

                            binding.depositThroughAppTextview.setText(numberFormat.format(commission)+"");
                            binding.payUponRetrievingCarTextview.setText(numberFormat.format(finalAmount-commission)+"");
                            binding.rentalPriceTextview.setText(numberFormat.format(priceDaily)+" / Ngày");
                        }
                    });


                    Log.d("tesss", "owner id: "+ownerId);
                    // SET THONG TIN CHU XE
                    // Lay thong tin chu xe sau khi lay duoc xe
                    DatabaseReference ownerReference = FirebaseDatabase.getInstance().getReference("users");
                    ownerReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot snapshotId : snapshot.getChildren()) {
                                if (snapshotId.getKey().equals(car.getOwnerID())) {
                                    owner = snapshotId.getValue(UserModel.class);
                                    Log.d("tesss", "owner id: "+owner.getId());

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

                    if (ownerId != null) {
                        // Get list date blocked of car
                        DatabaseReference billCarRef = FirebaseDatabase.getInstance().getReference("bills").child(carId);
                        billCarRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot billSnap : snapshot.getChildren()) {
                                    Order bill = billSnap.getValue(Order.class);
                                    LocalDate startDate = bill.getDateFrom().toLocalDate();
                                    LocalDate endDate = bill.getDateTo().toLocalDate();
                                    LocalDate currentDate = startDate;
                                    while (!currentDate.isAfter(endDate)) {
                                        listDateBlocked.add(currentDate);
                                        listDateBlockedStr.add(currentDate.toString());
                                        currentDate = currentDate.plusDays(1);
                                    }
                                }
                                Log.d("lsBlock", "onDataChange: " + listDateBlocked);

                                // Bắt sự kiện tap vao icon calendar mở lịch
                                binding.buttonChooseDate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        // Chuyen sang activity chon thoi gian
                                        Intent intent = new Intent(ConfirmRentalActivity.this, ConfirmChooseDateActivity.class);
                                        intent.putExtra("listBlocked", listDateBlockedStr);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(intent);
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        // End get list block car
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        // End flow intent from detail
        // Intent tu chon ngay: ConfirmChooseDate
        if (intent != null && intent.hasExtra("totalDate")){
            startDate = intent.getStringExtra("startDate");
            endDate = intent.getStringExtra("endDate");
            timeStart = intent.getStringExtra("timeStart");
            timeEnd = intent.getStringExtra("timeEnd");
            totalDate = intent.getIntExtra("totalDate",0);

            // Tao ArrayList<LocalDate> danh sach vua chon
            LocalDate sDate = LocalDate.parse(startDate);
            LocalDate eDate = LocalDate.parse(endDate);
            LocalDate currentDate = sDate;
            listDateChoose.clear();
            while (!currentDate.isAfter(eDate)) {
                listDateChoose.add(currentDate);
                currentDate = currentDate.plusDays(1);
            }

            // Danh sách ArrayList<LocalDate> danh sach block da co listDateBlock<LocalDate>

            binding.dateReceivedTextview.setText(timeStart + startDate+"");
            binding.dateReturnedTextview.setText(timeEnd +endDate+"");
            binding.rentalTotalDateTextview.setText(totalDate+"");

            binding.mortgagedPropertyTextview.setText("Chủ xe yêu cầu thế chấp: "+numberFormat.format(car.getMortgage())+" VND");
            int finalAmount = totalDate * priceDaily;
            Log.d("cal", "onResume: "+priceDaily);
            Log.d("cal", "final: "+finalAmount);

            binding.finalAmountTextview.setText(numberFormat.format(finalAmount)+"");
            int commission = (int) (finalAmount * 0.2);
            Log.d("cal", "commis: "+commission);
            binding.depositThroughAppTextview.setText(numberFormat.format(commission)+"");
            binding.payUponRetrievingCarTextview.setText(numberFormat.format(finalAmount-commission)+"");
        }

        // Set create bill
        binding.sendRentalRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean checked = false;
                Log.d("aaaaa", "onClick: size " + listDateChoose.size());
                if (listDateChoose.size() > 0){

                    if (listDateBlocked.size() == 0){
                        checked = true;
                    }
                    // Check co chon ngay hop le khong
                    for (LocalDate dateBlock : listDateBlocked) {
                        if (listDateChoose.contains(dateBlock)) {
                            checked = false;
                            new AlertDialog.Builder(ConfirmRentalActivity.this)
                                    .setTitle("Date invalid")
                                    .setMessage("Chosen date is invalid.")
                                    .setPositiveButton(android.R.string.ok, null)
                                    .show();
                            break; // Dừng vòng lặp ngay khi tìm thấy phần tử
                        }
                        checked = true;
                    }
                } else {
                    new AlertDialog.Builder(ConfirmRentalActivity.this)
                            .setTitle("Button Disabled")
                            .setMessage("Please choose date.")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
                // Khi nao true thi lam
                if (checked){
                    //Tao hoa don
                    Order bill = new Order();
                    DatabaseReference billCarRef = FirebaseDatabase.getInstance().getReference("bills").child(carId);
                    DatabaseReference billIdRef = billCarRef.push();
                    String billId = billIdRef.getKey();
                    bill.setId(billId);
                    bill.setCar(car);
                    bill.setCustomer(author);
                    bill.setOwner(owner);
                    LocalDate cr = LocalDate.now();
                    LocalDate sDate = LocalDate.parse(startDate);
                    LocalDate eDate = LocalDate.parse(endDate);
                    bill.setDateFrom(new Date(sDate));
                    bill.setDateTo(new Date(eDate));
                    bill.setDateCreated(new Date(cr));
                    bill.setTimePickup(timeStart);
                    bill.setTimeReturn(timeEnd);
                    bill.setTotalDate(totalDate);
                    bill.setTotalPay(Integer.parseInt(binding.finalAmountTextview.getText().toString()));
                    Log.d("aaaaaa", "onClick: bill " + bill.getTotalPay());
                    Log.d("aaaaaa", "onClick: " + binding.finalAmountTextview.getText().toString());
                    bill.setStatus(new Status(0,"Chờ xác nhận", "Đợi chút xíu"));
                    billIdRef.setValue(bill);


                    //Tao thong bao
                    //Lay ra
                     ownerStatus = new Status();
                     customerStatus = new Status();
                    DatabaseReference ownerStatusRef = FirebaseDatabase.getInstance().getReference("statuses").child("owner");
                    ownerStatusRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snap: snapshot.getChildren()) {
                                Status s = snap.getValue(Status.class);
                                if (s.getId()==0) {
                                    ownerStatus = s;
                                }
                                break;
                            }
                            DatabaseReference customerStatusRef = FirebaseDatabase.getInstance().getReference("statuses").child("customer");
                            customerStatusRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snap: snapshot.getChildren()) {
                                        Status s = snap.getValue(Status.class);
                                        if (s.getId()==0) {
                                            customerStatus = s;
                                            break;
                                        }

                                    }
                                    String ownerId = owner.getId();
                                    String customerId = author.getId();
                                    Log.d("useerrr", "onDataChange: "+ ownerId.toString());
                                    Log.d("useerrr", "onDataChange: "+ customerId);

                                    Notification notiOwner = new Notification();
                                    Notification notiCustomer = new Notification();

                                    DatabaseReference notiOwnerRef = FirebaseDatabase.getInstance().getReference("notifications").child(ownerId);
                                    DatabaseReference notiOwnerId = notiOwnerRef.push();
                                    notiOwner.setId(notiOwnerId.getKey());
                                    notiOwner.setOrder(bill);
                                    notiOwner.setStatus(ownerStatus);
                                    notiOwner.setDateCreated(LocalDate.now().toString());
                                    notiOwnerId.setValue(notiOwner);

                                    DatabaseReference notiCusRef = FirebaseDatabase.getInstance().getReference("notifications").child(customerId);
                                    DatabaseReference notiCusId = notiCusRef.push();
                                    notiCustomer.setId(notiCusId.getKey());
                                    notiCustomer.setOrder(bill);
                                    notiCustomer.setStatus(customerStatus);
                                    notiCustomer.setDateCreated(LocalDate.now().toString());
                                    notiCusId.setValue(notiCustomer);
                                    Intent intent1 = new Intent(ConfirmRentalActivity.this,MainActivity.class);
                                    intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(intent1);
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

            }
        });


    }
//     END onResume
}
