package vn.edu.tdc.rentaka.services;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.installations.internal.FidListener;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.edu.tdc.rentaka.APIs.RealTimeAPI;
import vn.edu.tdc.rentaka.activities.PaymentActivity;
import vn.edu.tdc.rentaka.databinding.PaymentLayoutBinding;
import vn.edu.tdc.rentaka.models.Notification;
import vn.edu.tdc.rentaka.models.Order;
import vn.edu.tdc.rentaka.models.Status;

public class SMSHandlingService extends Service {

    private static final String TAG = "SMSHandlingService";
    private RealTimeAPI realTimeAPI = new RealTimeAPI();
    int temp = 0;
    private Status ownerStatus;
    private Status customerStatus;
    private Status billStatus;
    private Status carStatus;
    private Status billStatusOnFirebase;
    private Order billOnFirebase;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String sms = intent.getStringExtra("sms");
        if (sms != null) {
            Log.i(TAG, "Received SMS: " + sms);
            handleSMS(sms);
        }
        return START_NOT_STICKY;
    }

    private void handleSMS(String sms) {
        // Define the regex pattern to match the SMS format
        String regex = "car rental: ([a-zA-Z0-9_-]+) ([a-zA-Z0-9_-]+) (\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sms);

        if (matcher.find()) {
            String carId = matcher.group(1);
            String billId = matcher.group(2);
            String moneyAmount = matcher.group(3);
            Log.i(TAG, "ID: " + temp++);

            Log.i(TAG, "Bill ID: " + billId);
            Log.i(TAG, "Car ID: " + carId);
            Log.i(TAG, "Money Amount: " + moneyAmount);

            // Handle the parsed data (e.g., save to database, update UI, etc.)
            try {
                realTimeAPI.updateBillStatusWhenCustomerPaid(billId, carId, "900000");


                //Thong bao
                //3. Thong bao den nguoi thue va chu xe
                //Lay ra trang thai
                //Vao lay status cuar bill treen firebase, kieems tra voi status cua notification
                DatabaseReference billRef = FirebaseDatabase.getInstance().getReference("bills").child(carId).child(billId);
                billRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        billOnFirebase = new Order();
                        billOnFirebase = snapshot.getValue(Order.class);

                        ownerStatus = new Status();
                        customerStatus = new Status();
                        DatabaseReference ownerStatusRef = FirebaseDatabase.getInstance().getReference("statuses").child("owner");
                        ownerStatusRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snap : snapshot.getChildren()) {
                                    Status s = snap.getValue(Status.class);
                                    if (s.getId() == 2) {
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
                                            if (s.getId() == 2) {
                                                customerStatus = s;
                                                break;
                                            }

                                        }
                                        Log.d("status", "onDataChange: cusStatus" + customerStatus);

                                        String ownerId = billOnFirebase.getOwner().getId();
                                        String customerId = billOnFirebase.getCustomer().getId();
                                        Log.d("useerrr", "onDataChange: " + ownerId.toString());
                                        Log.d("useerrr", "onDataChange: " + customerId);

                                        Notification notiOwner = new Notification();
                                        Notification notiCustomer = new Notification();

                                        DatabaseReference notiOwnerRef = FirebaseDatabase.getInstance().getReference("notifications").child(ownerId);
                                        DatabaseReference notiOwnerId = notiOwnerRef.push();
                                        notiOwner.setId(notiOwnerId.getKey());
                                        notiOwner.setOrder(billOnFirebase);
                                        notiOwner.setStatus(ownerStatus);
                                        notiOwner.setDateCreated(LocalDate.now().toString());
                                        notiOwnerId.setValue(notiOwner);

                                        DatabaseReference notiCusRef = FirebaseDatabase.getInstance().getReference("notifications").child(customerId);
                                        DatabaseReference notiCusId = notiCusRef.push();
                                        notiCustomer.setId(notiCusId.getKey());
                                        notiCustomer.setOrder(billOnFirebase);
                                        notiCustomer.setStatus(customerStatus);
                                        notiCustomer.setDateCreated(LocalDate.now().toString());
                                        notiCusId.setValue(notiCustomer);
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



            } catch (Exception e) {
                Log.e(TAG, "Error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "SMS format not recognized");
        }
    }
}
