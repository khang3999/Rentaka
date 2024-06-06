package vn.edu.tdc.rentaka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.RentalDetailLayoutBinding;
import vn.edu.tdc.rentaka.databinding.RequiredRentalLayoutBinding;
import vn.edu.tdc.rentaka.models.Car;
import vn.edu.tdc.rentaka.models.Notification;
import vn.edu.tdc.rentaka.models.Order;
import vn.edu.tdc.rentaka.models.Status;
import vn.edu.tdc.rentaka.models.UserModel;

public class RequiredRentalActivity extends AppCompatActivity {
    private RequiredRentalLayoutBinding binding;
    private Order order;
    private Notification notification;
    private Car car;
    private UserModel owner;
    private String imageOwnerUrl;
    private String imageCarUrl;
    private Status billStatusOnFirebase;
    private Order billOnFirebase;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = RequiredRentalLayoutBinding.inflate(getLayoutInflater());
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
                        Glide.with(RequiredRentalActivity.this)
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
                    imageOwnerUrl = owner.getImageUser();
                    // Set hinh chu xe
                    if (imageOwnerUrl != null && !imageOwnerUrl.isEmpty()) {
                        Glide.with(RequiredRentalActivity.this)
                                .load(imageOwnerUrl)
                                .into(binding.profileImg);
                    } else {
                        //Set anh mac dinh
                        binding.profileImg.setImageResource(R.drawable.avatar);
                    }

                    binding.mortgagedPropertyTextview.setText("Chủ xe yêu cầu thế chấp: "+car.getMortgage()+" VND");

                    binding.ownerNameTextview.setText(owner.getName());
                    binding.rentalTotalDateTextview.setText(order.getTotalDate()+"");
                    binding.finalAmountTextview.setText(order.getTotalPay()+"");
                    binding.rentalPriceTextview.setText((int)(order.getTotalPay()/order.getTotalDate())+"");
                    binding.depositThroughAppTextview.setText((int) (order.getTotalPay()*0.2)+"");
                    binding.payUponRetrievingCarTextview.setText((int) (order.getTotalPay()*0.8)+"");

                    //Kiem tra lai bill tren firebase co bi huy hay khong neu huy thi set lai title cho navigation
                    DatabaseReference billRef = FirebaseDatabase.getInstance().getReference("bills").child(car.getId()).child(order.getId());
                    billRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            billOnFirebase = new Order();
                            billOnFirebase = snapshot.getValue(Order.class);
                            billStatusOnFirebase = new Status();
                            billStatusOnFirebase = billOnFirebase.getStatus();
                            Log.d("status", "status:" +billStatusOnFirebase.getId());

                                if (billStatusOnFirebase.getId()==4){
                                    binding.topAppBar.setTitle("Chuyến xe đã huỷ");
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
