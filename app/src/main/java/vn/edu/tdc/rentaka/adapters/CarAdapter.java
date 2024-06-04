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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.CardCarItemBinding;
import vn.edu.tdc.rentaka.models.Car;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Car> listCar;
    private ItemClickListener itemClickListener;
    private DatabaseReference db;

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CarAdapter(Context context, ArrayList<Car> listCar) {
        this.context = context;
        this.listCar = listCar;
        this.db = FirebaseDatabase.getInstance().getReference();
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

        // Chuyển chuỗi thành file hình
        String imageUrl = car.getImageCarUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(binding.imageCar);
        } else {
            // Set ảnh mặc định
            binding.imageCar.setImageResource(R.drawable.car);
        }

        binding.textViewTypeGearBox.setText(car.getTypeGearbox());
        binding.textViewFuel.setText(car.getFuel());
        binding.nameCar.setText(car.getModel());
        binding.addressCar.setText(car.getDescription());
        //Update moi lan load
        updateFavoriteIcon(binding, car);

        // Set tag cho tung item
        holder.itemView.setTag(binding);

        // Yeu thich
        binding.icHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nhan heart sp
                toggleFavorite(car, holder.getBinding().getRoot());
            }
        });

        // Set the favorite icon status based on the database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.child("favourites").child(user.getUid())
                    .child(car.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                binding.icHeart.setImageResource(R.drawable.ic_heart_24_press);
                            } else {
                                binding.icHeart.setImageResource(R.drawable.ic_heart);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle possible errors.
                        }
                    });
        }
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

            // Bắt sự kiện chung
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

    private void toggleFavorite(Car car, View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference favRef = db.child("favourites").child(user.getUid()).child(car.getId());

            favRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Remove from favourites
                        favRef.removeValue();
                        updateFavoriteIcon((CardCarItemBinding) view.getTag(), false);
                        Snackbar.make(view, "Bạn Đã Bỏ Yêu Thích", Snackbar.ANIMATION_MODE_FADE).show();
                    } else {
                        // Add to favourites
                        favRef.setValue(car);
                        updateFavoriteIcon((CardCarItemBinding) view.getTag(), true);
                        Snackbar.make(view, "Bạn Đã Thêm Vào Yêu Thích", Snackbar.ANIMATION_MODE_FADE).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void updateFavoriteIcon(CardCarItemBinding binding, boolean isFavorite) {
        if (isFavorite) {
            binding.icHeart.setImageResource(R.drawable.ic_heart_24_press);
        } else {
            binding.icHeart.setImageResource(R.drawable.ic_heart);
        }
    }

    private void updateFavoriteIcon(CardCarItemBinding binding, Car car) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.child("favourites").child(user.getUid()).child(car.getId())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                binding.icHeart.setImageResource(R.drawable.ic_heart_24_press);
                            } else {
                                binding.icHeart.setImageResource(R.drawable.ic_heart);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle possible errors.
                        }
                    });
        }
    }
}
