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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.CardCarItemBinding;
import vn.edu.tdc.rentaka.fragments.HomeFragment;
import vn.edu.tdc.rentaka.models.Car;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Car> listCar;
    private ItemClickListener itemClickListener;

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CarAdapter(Context context, ArrayList<Car> listCar) {
        this.context = context;
        this.listCar = listCar;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(CardCarItemBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CardCarItemBinding binding = (CardCarItemBinding) holder.getBinding();
        Car car = listCar.get(position);

        // Chuyen chuoi thanh file hinh
        String imageUrl = car.getImageCarUrl(); // lay duong dan den file
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(binding.imageCar);
        } else {
            //Set anh mac dinh
            binding.imageCar.setImageResource(R.drawable.car);
        }

        binding.textViewTypeGearBox.setText(car.getTypeGearbox());
        binding.textViewFuel.setText(car.getFuel());
        binding.nameCar.setText(car.getModel());
        binding.addressCar.setText(car.getCity().getName());
        
        //updateFavoriteIcon(binding, car);


//        // Handle favorite icon click
//        binding.icHeart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean isFavorite = toggleFavorite(car, holder.itemView);
//                updateFavoriteIcon(binding, car);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return listCar.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ViewBinding binding;

        public MyViewHolder(@NonNull ViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            // Bat su kien chung
            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(CarAdapter.MyViewHolder.this);
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
        void onItemClick(CarAdapter.MyViewHolder holder);
    }

//    private boolean toggleFavorite(Car car, View view) {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            if (car.getFavourite().equals("like")) {
//                car.setFavorite("unlike");
//                db.collection("cars").document(car.getId()).update("favourite", "unlike");
//                Snackbar.make(view, "Bạn Đã Bỏ Yêu Thích", Snackbar.ANIMATION_MODE_FADE).show();
//                return false;
//            } else {
//                car.setFavorite("like");
//                db.collection("cars").document(car.getId()).update("favourite", "like");
//                Snackbar.make(view, "Bạn Đã Thêm Vào Yêu Thích", Snackbar.ANIMATION_MODE_FADE).show();
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private void updateFavoriteIcon(CardCarItemBinding binding, Car car) {
//        if (car.getFavourite().equals("like")) {
//            binding.icHeart.setImageResource(R.drawable.ic_heart_24_press);
//        } else {
//            binding.icHeart.setImageResource(R.drawable.ic_heart);
//        }
//    }
}
