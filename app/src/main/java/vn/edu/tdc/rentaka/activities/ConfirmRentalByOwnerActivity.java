package vn.edu.tdc.rentaka.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.ConfirmRentalLayoutBinding;
import vn.edu.tdc.rentaka.databinding.ConfirmRentalOwnerLayoutBinding;
import vn.edu.tdc.rentaka.databinding.RequiredRentalLayoutBinding;
import vn.edu.tdc.rentaka.models.Car;
import vn.edu.tdc.rentaka.models.Date;
import vn.edu.tdc.rentaka.models.Notification;
import vn.edu.tdc.rentaka.models.Order;
import vn.edu.tdc.rentaka.models.Status;
import vn.edu.tdc.rentaka.models.UserModel;

public class ConfirmRentalByOwnerActivity extends AppCompatActivity {
    private ConfirmRentalOwnerLayoutBinding binding;
    private Order order;
    private Notification notification;
    private Car car;
    private UserModel owner;
    private String imageOwnerUrl;
    private String imageCarUrl;
    private Status ownerStatus;
    private Status customerStatus;
    private Status billStatus;
    private Status carStatus;
    private Status billStatusOnFirebase;
    private Order billOnFirebase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ConfirmRentalOwnerLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Button top back navigation
        setSupportActionBar(binding.topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                // Main vao tu trai, choose date exit ve ben phai
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

        //Lay notification
        Intent intent = getIntent();
        String notificationId = intent.getStringExtra("notiId");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getUid()!=null){
            DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("notifications").child(user.getUid()).child(notificationId);
            orderRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    notification = new Notification();
                    notification = snapshot.getValue(Notification.class);
                    order = new Order();
                    order = notification.getOrder();
                    car = new Car();
                    car = order.getCar();
                    owner = new UserModel();
                    owner = order.getOwner();
                    imageCarUrl = car.getImageCarUrl();
                    // Set hinh anh xe
                    if (imageCarUrl != null && !imageCarUrl.isEmpty()) {
                        Glide.with(ConfirmRentalByOwnerActivity.this)
                                .load(imageCarUrl)
                                .into(binding.imageCar);
                    } else {
                        //Set anh mac dinh
                        binding.imageCar.setImageResource(R.drawable.car);
                    }
                    binding.carModelTextview.setText(car.getBrand()+" "+car.getModel());
                    binding.carColor.setText(car.getColor());
                    binding.radNoRequireDriver.setEnabled(false);
                    binding.radRequireDriver.setEnabled(false);
                    binding.dateReceivedTextview.setText(order.getTimePickup()+", "+order.getDateFrom().toString() );
                    binding.dateReturnedTextview.setText(order.getTimeReturn()+", "+order.getDateTo().toString());
                    binding.locationTextview.setText(car.getCity().getName());
                    Log.d("tesssss", "owner: " + owner);
                    imageOwnerUrl = order.getCustomer().getImageUser();
                    // Set hinh chu xe
                    if (imageOwnerUrl != null && !imageOwnerUrl.isEmpty()) {
                        Glide.with(ConfirmRentalByOwnerActivity.this)
                                .load(imageOwnerUrl)
                                .into(binding.profileImg);
                    } else {
                        //Set anh mac dinh
                        binding.profileImg.setImageResource(R.drawable.avatar);
                    }

                    binding.mortgagedPropertyTextview.setText("Chủ xe yêu cầu thế chấp: "+car.getMortgage()+" VND");

                    binding.customerName.setText(order.getCustomer().getName());
                    binding.commissionNumber.setText((int) (order.getTotalPay()*0.2)+"");
                    binding.moneyReceivedNumber.setText((int) (order.getTotalPay()*0.8)+"");
                    binding.methodReceived2Number.setText((int) (order.getTotalPay()*0.8)+"");

                    //Vao lay status cuar bill treen firebase, kieems tra voi status cua notification
                    DatabaseReference billRef = FirebaseDatabase.getInstance().getReference("bills").child(car.getId()).child(order.getId());
                    billRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                billOnFirebase = new Order();
                                billOnFirebase = snapshot.getValue(Order.class);
                                billStatusOnFirebase = new Status();
                                billStatusOnFirebase = billOnFirebase.getStatus();

                            if (notification.getStatus().getId() == billStatusOnFirebase.getId()) {

//                                ********* Chap nhan *********
                                //Bat su kien khi nhan vao 2 button
                                //Accept
                                binding.acceptButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //lay trang thai "Da duoc nhan"
                                        //1. Thay doi trang thai bill
                                        ///Lay trang thai 1 cho bill
                                        billStatus = new Status();
                                        DatabaseReference billStatusRef = FirebaseDatabase.getInstance().getReference("statuses").child("bills");
                                        billStatusRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                for (DataSnapshot snap : snapshot.getChildren()) {
                                                    Status s = snap.getValue(Status.class);

                                                    if (s.getId() == 1) {
                                                        billStatus = s;
                                                        break;
                                                    }
                                                    Log.d("status", "onDataChange: billStatus" + s);

                                                }
                                                Log.d("status", "onDataChange: billStatus" + billStatus);
                                                //Lay xong thi doi trang thai cho bill
                                                DatabaseReference billRef = FirebaseDatabase.getInstance().getReference("bills").child(order.getCar().getId()).child(order.getId());
                                                Map<String, Object> updates = new HashMap<>();
                                                updates.put("status", billStatus);
                                                billRef.updateChildren(updates)
                                                        .addOnSuccessListener(aVoid -> {
                                                            // Khi cập nhật thành công, hiển thị thông báo thành công và đóng bottom sheet
                                                            Toast.makeText(ConfirmRentalByOwnerActivity.this, "Cap nhat thông tin status bill thành công", Toast.LENGTH_SHORT).show();

                                                        })
                                                        .addOnFailureListener(e -> {
                                                            // Nếu cập nhật thất bại, hiển thị thông báo lỗi
                                                            Toast.makeText(ConfirmRentalByOwnerActivity.this, "Cap nhat thông tin status bill thất bại", Toast.LENGTH_SHORT).show();
                                                        });

                                                //2. Thay doi trang thai xe thanh dang co chuyen
                                                ///Lay ra trang thai cua xe
                                                DatabaseReference carStatusRef = FirebaseDatabase.getInstance().getReference("statuses").child("car");
                                                carStatus = new Status();
                                                carStatusRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot snap : snapshot.getChildren()) {
                                                            Status s = new Status();
                                                            s = snap.getValue(Status.class);
                                                            Log.d("check", "onDataChange:  " + s.getId());
                                                            if (s.getId() == 2) {
                                                                carStatus = s;
                                                                break;
                                                            }

                                                        }
                                                        Log.d("status", "onDataChange: carStatus" + carStatus);

                                                        //Doi trang thai cho xe
                                                        DatabaseReference carRef = FirebaseDatabase.getInstance().getReference("cars").child(order.getOwner().getId()).child(order.getCar().getId());
                                                        Map<String, Object> updates = new HashMap<>();
                                                        updates.put("statusId", carStatus);
                                                        carRef.updateChildren(updates)
                                                                .addOnSuccessListener(aVoid -> {
                                                                    // Khi cập nhật thành công, hiển thị thông báo thành công và đóng bottom sheet
                                                                    Toast.makeText(ConfirmRentalByOwnerActivity.this, "Cap nhat thông tin status car thành công", Toast.LENGTH_SHORT).show();


                                                                })
                                                                .addOnFailureListener(e -> {
                                                                    // Nếu cập nhật thất bại, hiển thị thông báo lỗi
                                                                    Toast.makeText(ConfirmRentalByOwnerActivity.this, "Cap nhat thông tin status car thất bại", Toast.LENGTH_SHORT).show();
                                                                });

                                                        //3. Thong bao den nguoi thue va chu xe
                                                        //Lay ra trang thai
                                                        ownerStatus = new Status();
                                                        customerStatus = new Status();
                                                        DatabaseReference ownerStatusRef = FirebaseDatabase.getInstance().getReference("statuses").child("owner");
                                                        ownerStatusRef.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                for (DataSnapshot snap : snapshot.getChildren()) {
                                                                    Status s = snap.getValue(Status.class);
                                                                    if (s.getId() == 1) {
                                                                        ownerStatus = s;
                                                                        break;
                                                                    }

                                                                }
                                                                Log.d("status", "onDataChange: ownerStatus" + ownerStatus);

                                                                DatabaseReference customerStatusRef = FirebaseDatabase.getInstance().getReference("statuses").child("customer");
                                                                customerStatusRef.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        for (DataSnapshot snap : snapshot.getChildren()) {
                                                                            Status s = snap.getValue(Status.class);
                                                                            if (s.getId() == 1) {
                                                                                customerStatus = s;
                                                                                break;
                                                                            }

                                                                        }
                                                                        Log.d("status", "onDataChange: cusStatus" + customerStatus);

                                                                        String ownerId = owner.getId();
                                                                        String customerId = order.getCustomer().getId();
                                                                        Log.d("useerrr", "onDataChange: " + ownerId.toString());
                                                                        Log.d("useerrr", "onDataChange: " + customerId);

                                                                        Notification notiOwner = new Notification();
                                                                        Notification notiCustomer = new Notification();

                                                                        DatabaseReference notiOwnerRef = FirebaseDatabase.getInstance().getReference("notifications").child(ownerId);
                                                                        DatabaseReference notiOwnerId = notiOwnerRef.push();
                                                                        notiOwner.setId(notiOwnerId.getKey());
                                                                        notiOwner.setOrder(order);
                                                                        notiOwner.setStatus(ownerStatus);
                                                                        notiOwner.setDateCreated(LocalDate.now().toString());
                                                                        notiOwnerId.setValue(notiOwner);

                                                                        DatabaseReference notiCusRef = FirebaseDatabase.getInstance().getReference("notifications").child(customerId);
                                                                        DatabaseReference notiCusId = notiCusRef.push();
                                                                        notiCustomer.setId(notiCusId.getKey());
                                                                        notiCustomer.setOrder(order);
                                                                        notiCustomer.setStatus(customerStatus);
                                                                        notiCustomer.setDateCreated(LocalDate.now().toString());
                                                                        notiCusId.setValue(notiCustomer);

                                                                        finish();
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
                                });

//                                ********* HUY *****
                                binding.denyButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //lay trang thai "Da duoc nhan"
                                        //1. Thay doi trang thai bill
                                        ///Lay trang thai 1 cho bill
                                        billStatus = new Status();
                                        DatabaseReference billStatusRef = FirebaseDatabase.getInstance().getReference("statuses").child("bills");
                                        billStatusRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                for (DataSnapshot snap : snapshot.getChildren()) {
                                                    Status s = snap.getValue(Status.class);

                                                    if (s.getId() == 4) {
                                                        billStatus = s;
                                                        break;
                                                    }
                                                    Log.d("status", "onDataChange: billStatus" + s);

                                                }
                                                Log.d("status", "onDataChange: billStatus" + billStatus);
                                                //Lay xong thi doi trang thai cho bill
                                                DatabaseReference billRef = FirebaseDatabase.getInstance().getReference("bills").child(order.getCar().getId()).child(order.getId());
                                                Map<String, Object> updates = new HashMap<>();
                                                updates.put("status", billStatus);
                                                billRef.updateChildren(updates)
                                                        .addOnSuccessListener(aVoid -> {
                                                            // Khi cập nhật thành công, hiển thị thông báo thành công và đóng bottom sheet
                                                            Toast.makeText(ConfirmRentalByOwnerActivity.this, "Cap nhat thông tin status bill thành công", Toast.LENGTH_SHORT).show();

                                                        })
                                                        .addOnFailureListener(e -> {
                                                            // Nếu cập nhật thất bại, hiển thị thông báo lỗi
                                                            Toast.makeText(ConfirmRentalByOwnerActivity.this, "Cap nhat thông tin status bill thất bại", Toast.LENGTH_SHORT).show();
                                                        });

                                                //2. Thong bao den nguoi thue va chu xe
                                                //Lay ra trang thai
                                                ownerStatus = new Status();
                                                customerStatus = new Status();
                                                DatabaseReference ownerStatusRef = FirebaseDatabase.getInstance().getReference("statuses").child("owner");
                                                ownerStatusRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot snap : snapshot.getChildren()) {
                                                            Status s = snap.getValue(Status.class);
                                                            if (s.getId() == 4) {
                                                                ownerStatus = s;
                                                                break;
                                                            }

                                                        }
                                                        Log.d("status", "onDataChange: ownerStatus" + ownerStatus);

                                                        DatabaseReference customerStatusRef = FirebaseDatabase.getInstance().getReference("statuses").child("customer");
                                                        customerStatusRef.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                for (DataSnapshot snap : snapshot.getChildren()) {
                                                                    Status s = snap.getValue(Status.class);
                                                                    if (s.getId() == 4) {
                                                                        customerStatus = s;
                                                                        break;
                                                                    }

                                                                }
                                                                Log.d("status", "onDataChange: cusStatus" + customerStatus);

                                                                String ownerId = owner.getId();
                                                                String customerId = order.getCustomer().getId();
                                                                Log.d("useerrr", "onDataChange: " + ownerId.toString());
                                                                Log.d("useerrr", "onDataChange: " + customerId);

                                                                Notification notiOwner = new Notification();
                                                                Notification notiCustomer = new Notification();

                                                                DatabaseReference notiOwnerRef = FirebaseDatabase.getInstance().getReference("notifications").child(ownerId);
                                                                DatabaseReference notiOwnerId = notiOwnerRef.push();
                                                                notiOwner.setId(notiOwnerId.getKey());
                                                                notiOwner.setOrder(order);
                                                                notiOwner.setStatus(ownerStatus);
                                                                notiOwner.setDateCreated(LocalDate.now().toString());
                                                                notiOwnerId.setValue(notiOwner);

                                                                DatabaseReference notiCusRef = FirebaseDatabase.getInstance().getReference("notifications").child(customerId);
                                                                DatabaseReference notiCusId = notiCusRef.push();
                                                                notiCustomer.setId(notiCusId.getKey());
                                                                notiCustomer.setOrder(order);
                                                                notiCustomer.setStatus(customerStatus);
                                                                notiCustomer.setDateCreated(LocalDate.now().toString());
                                                                notiCusId.setValue(notiCustomer);

                                                                finish();
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

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                });
                            }
                            else {
                                binding.groupAction.setVisibility(View.INVISIBLE);
                                binding.actionDone.setVisibility(View.VISIBLE);
                                if (billStatusOnFirebase.getId()==4){
                                    binding.topAppBar.setTitle("Chuyến xe đã huỷ");
                                }
                            }
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
}
