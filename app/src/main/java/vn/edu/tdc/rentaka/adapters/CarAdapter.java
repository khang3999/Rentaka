package vn.edu.tdc.rentaka.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.CardCarItemBinding;
import vn.edu.tdc.rentaka.models.Car;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Car> listCar;
    private ItemClickListener itemClickListener;
    private FirebaseFirestore db;

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CarAdapter(Context context, ArrayList<Car> listCar) {
        this.context = context;
        this.listCar = listCar;
        db = FirebaseFirestore.getInstance();
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

        binding.nameCar.setText(car.getModel() + " " + car.getDescription());
        updateFavoriteIcon(binding, car);

        // Handle favorite icon click
        binding.icHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavorite = toggleFavorite(car);
                updateFavoriteIcon(binding, car);
            }
        });
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

    private boolean toggleFavorite(Car car) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (car.getFavourite().equals("like")) {
                car.setFavorite("unlike");
                db.collection("cars").document(car.getId()).update("favourite", "unlike");
                Toast.makeText(context, "Bạn Đã Bỏ Yêu Thích", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                car.setFavorite("like");
                db.collection("cars").document(car.getId()).update("favourite", "like");
                Toast.makeText(context, "Bạn Đã Thêm Vào Yêu Thích", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private void updateFavoriteIcon(CardCarItemBinding binding, Car car) {
        if (car.getFavourite().equals("like")) {
            binding.icHeart.setImageResource(R.drawable.ic_heart_24_press);
        } else {
            binding.icHeart.setImageResource(R.drawable.ic_heart);
        }
    }
}
