package vn.edu.tdc.rentaka.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.CardNotiItemBinding;
import vn.edu.tdc.rentaka.databinding.CardStatusItemLayoutBinding;
import vn.edu.tdc.rentaka.models.Date;
import vn.edu.tdc.rentaka.models.Notification;
import vn.edu.tdc.rentaka.models.Order;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Order> listOrders;
    private HistoryAdapter.ItemClickListener itemClickListener;
    private DatabaseReference db;

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

}
