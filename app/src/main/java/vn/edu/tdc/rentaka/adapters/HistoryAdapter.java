package vn.edu.tdc.rentaka.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.activities.ConfirmRentalByOwnerActivity;
import vn.edu.tdc.rentaka.activities.MainActivity;
import vn.edu.tdc.rentaka.databinding.CardNotiItemBinding;
import vn.edu.tdc.rentaka.databinding.CardStatusItemLayoutBinding;
import vn.edu.tdc.rentaka.models.Date;
import vn.edu.tdc.rentaka.models.Notification;
import vn.edu.tdc.rentaka.models.Order;
import vn.edu.tdc.rentaka.models.Status;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Order> listOrders;
    private HistoryAdapter.ItemClickListener itemClickListener;
    private DatabaseReference db;
    private Status billStatusOnFirebase;
    private Order billOnFirebase;

    private Status ownerStatus;
    private Status customerStatus;
    private Status billStatus;
    private Status carStatus;

    public void setOnItemClickListener(HistoryAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public HistoryAdapter(Context context, ArrayList<Order> listOrders) {
        this.context = context;
        this.listOrders = listOrders;
        this.db = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new HistoryAdapter.MyViewHolder(CardStatusItemLayoutBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {
        CardStatusItemLayoutBinding binding = (CardStatusItemLayoutBinding) holder.getBinding();
         Order order = listOrders.get(position);
        binding.nameCar.setText(order.getCar().getBrand()+" "+order.getCar().getModel());
        Date dateFrom = new Date();
        dateFrom = order.getDateFrom();
        Date dateTo = new Date();
        dateTo = order.getDateTo();
        Log.d("date", "onBindViewHolder: "+dateFrom);
        binding.timeRent.setText(order.getTimePickup()+" "+dateFrom.toString()+" - "+order.getTimeReturn()+" "+dateTo.toString());
        binding.status.setText(order.getStatus().getTitle());
        // Chuyển chuỗi thành file hình
        String imageUrl = order.getCar().getImageCarUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(binding.imageCar);
        } else {
            // Set ảnh mặc định
            binding.imageCar.setImageResource(R.drawable.car);
        }

        Log.d("status", "order status: " + order.getStatus().getId());
        Log.d("status", "date to : " + order.getDateTo().toLocalDate());
        Log.d("status", "date local : " + LocalDate.now());
        if (order.getStatus().getId()==2 && order.getDateTo().toLocalDate().isEqual(LocalDate.now())){
            binding.btnDone.setVisibility(View.VISIBLE);
        }

        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateBillAndCreateNotification(order, binding);



            }
        });

    }

    @Override
    public int getItemCount() {
        return listOrders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ViewBinding binding;

        public MyViewHolder(@NonNull ViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            // Bắt sự kiện chung
            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(HistoryAdapter.MyViewHolder.this);
                    } else {
                        Log.d("adapter", "You must create an ItemClickListener before!");
                    }
                }
            });
        }

        public ViewBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewBinding binding) {
            this.binding = binding;
        }
    }

    // Interface
    public interface ItemClickListener {
        void onItemClick(HistoryAdapter.MyViewHolder holder);
    }

    public void updateBillAndCreateNotification(Order order, ViewBinding viewBinding){
        //Cap nhat trang thai cho bill
        DatabaseReference billStatusRef = FirebaseDatabase.getInstance().getReference("statuses").child("bills");
        billStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snap : snapshot.getChildren()) {
                    Status s = snap.getValue(Status.class);

                    if (s.getId() == 3) {
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
                            Toast.makeText(context, "Cap nhat thông tin status bill thành công", Toast.LENGTH_SHORT).show();

                        })
                        .addOnFailureListener(e -> {
                            // Nếu cập nhật thất bại, hiển thị thông báo lỗi
                            Toast.makeText(context, "Cap nhat thông tin status bill thất bại", Toast.LENGTH_SHORT).show();
                        });

                //2. Thong bao den nguoi thue va chu xe
                //Lay ra trang thai
                ownerStatus = new Status();
                customerStatus = new Status();
                DatabaseReference ownerStatusRef = FirebaseDatabase.getInstance().getReference("statuses").child("owner");
                ownerStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Status s = snap.getValue(Status.class);
                            if (s.getId() == 3) {
                                ownerStatus = s;
                                break;
                            }

                        }
                        Log.d("status", "onDataChange: ownerStatus" + ownerStatus);

                        DatabaseReference customerStatusRef = FirebaseDatabase.getInstance().getReference("statuses").child("customer");
                        customerStatusRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snap : snapshot.getChildren()) {
                                    Status s = snap.getValue(Status.class);
                                    if (s.getId() == 3) {
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
                                ((CardStatusItemLayoutBinding)viewBinding).btnDone.setVisibility(View.INVISIBLE);
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
}
