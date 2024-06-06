package vn.edu.tdc.rentaka.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.CardNotiItemBinding;
import vn.edu.tdc.rentaka.models.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Notification> listNotifications;
    private NotificationAdapter.ItemClickListener itemClickListener;
    private DatabaseReference db;

    public void setOnItemClickListener(NotificationAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public NotificationAdapter(Context context, ArrayList<Notification> listNotifications) {
        this.context = context;
        this.listNotifications = listNotifications;
        this.db = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new NotificationAdapter.MyViewHolder(CardNotiItemBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        CardNotiItemBinding binding = (CardNotiItemBinding) holder.getBinding();
        Notification notification = listNotifications.get(position);

        binding.billId.setText(notification.getOrder().getId());
        binding.titleNoti.setText(notification.getStatus().getTitle());
        binding.descriptionNoti.setText(notification.getStatus().getDescription());
        binding.dateNoti.setText(notification.getDateCreated().toString());
        // Chuyển chuỗi thành file hình
        String imageUrl = notification.getOrder().getCar().getImageCarUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(binding.imageCar);
        } else {
            // Set ảnh mặc định
            binding.imageCar.setImageResource(R.drawable.car);
        }

    }

    @Override
    public int getItemCount() {
        return listNotifications.size();
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
                        itemClickListener.onItemClick(NotificationAdapter.MyViewHolder.this);
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
        void onItemClick(NotificationAdapter.MyViewHolder holder);
    }

}
