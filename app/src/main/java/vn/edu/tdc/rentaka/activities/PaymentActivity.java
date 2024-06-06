package vn.edu.tdc.rentaka.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
import vn.edu.tdc.rentaka.databinding.PaymentLayoutBinding;
import vn.edu.tdc.rentaka.databinding.RentalDetailLayoutBinding;
import vn.edu.tdc.rentaka.models.Notification;
import vn.edu.tdc.rentaka.models.Order;
import vn.edu.tdc.rentaka.models.Status;

public class PaymentActivity extends AppCompatActivity {

    private PaymentLayoutBinding binding;
    private Order order;
    private Notification notification;
    private Status ownerStatus;
    private Status customerStatus;
    private Status billStatus;
    private Status carStatus;
    private Status billStatusOnFirebase;
    private Order billOnFirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = PaymentLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.home.setOnClickListener(new View.OnClickListener() {
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
            DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("notifications").child(user.getUid()).child(notificationId);
            notificationRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    notification = new Notification();
                    notification = snapshot.getValue(Notification.class);
                    order = new Order();
                    order = notification.getOrder();

                    //Do du lieu
                    int priceDaily = order.getTotalPay()/order.getTotalDate();
                    binding.billID.setText(order.getId());
                    binding.tvRentalPricePerDay.setText(priceDaily+"");
                    binding.tvPickUpDate.setText(order.getDateFrom().toString());
                    binding.tvReturnDate.setText(order.getDateTo().toString());
                    binding.tvPickUpTime.setText(order.getTimePickup().substring(0, order.getTimePickup().length() - 2));
                    binding.tvReturnTime.setText(order.getTimeReturn().substring(0, order.getTimeReturn().length() - 2));
                    binding.carLocation.setText(order.getCar().getCity().getName());
                    binding.totalPayment.setText(order.getTotalPay()+" VND");
                    binding.deposit.setText((int)(order.getTotalPay()*0.2)+" VND");
                    binding.totalDeposit.setText((int)(order.getTotalPay()*0.2)+" VND");
                    binding.totalDepositFinal.setText((int)(order.getTotalPay()*0.2)+" VND");
                    binding.idCar.setText(order.getCar().getId());
                    //Vao lay status cuar bill treen firebase, kieems tra voi status cua notification
                    DatabaseReference billRef = FirebaseDatabase.getInstance().getReference("bills").child(order.getCar().getId()).child(order.getId());
                    billRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            billOnFirebase = new Order();
                            billOnFirebase = snapshot.getValue(Order.class);
                            billStatusOnFirebase = new Status();
                            billStatusOnFirebase = billOnFirebase.getStatus();

                            if (notification.getStatus().getId() == billStatusOnFirebase.getId()) {

//                                ********* Chap nhan *********
                                //Xu ly 2 button
                                binding.btnConfirmRental.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                binding.btnCancel.setOnClickListener(new View.OnClickListener() {
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
                                                            Toast.makeText(PaymentActivity.this, "Cap nhat thông tin status bill thành công", Toast.LENGTH_SHORT).show();

                                                        })
                                                        .addOnFailureListener(e -> {
                                                            // Nếu cập nhật thất bại, hiển thị thông báo lỗi
                                                            Toast.makeText(PaymentActivity.this, "Cap nhat thông tin status bill thất bại", Toast.LENGTH_SHORT).show();
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

                                                                String ownerId = order.getOwner().getId();
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
                                binding.btnCancel.setEnabled(false);
                                binding.btnConfirmRental.setEnabled(true);
                                if (billStatusOnFirebase.getId()==4){
                                    binding.billID.setText("Chuyến xe đã huỷ");
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