package vn.edu.tdc.rentaka.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.activities.EditCarActivity;
import vn.edu.tdc.rentaka.activities.MyCarActivity;
import vn.edu.tdc.rentaka.databinding.CardCarItemBinding;
import vn.edu.tdc.rentaka.databinding.MycarItemLayoutBinding;
import vn.edu.tdc.rentaka.models.Car;

public class MyCarAdapter extends RecyclerView.Adapter<MyCarAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Car> listCar;
    private MyCarAdapter.ItemClickListener itemClickListener;
    private DatabaseReference db;

    public void setOnItemClickListener(MyCarAdapter.ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MyCarAdapter(Context context, ArrayList<Car> listCar) {
        this.context = context;
        this.listCar = listCar;
        this.db = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(MycarItemLayoutBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MycarItemLayoutBinding binding = (MycarItemLayoutBinding) holder.getBinding();
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
         binding.nameCar.setText(car.getBrand()+" "+car.getModel());
        binding.statusMycar.setText(car.getStatusId().getTitle());

        //Khi nhan vao nut Sua thong tin xe
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditCarActivity.class);
                intent.putExtra("carid", car.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                context.startActivity(intent);
            }
        });
        //Khi nhan vao nut xoa xe
        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user.getUid()!=null){
                //Create AlertDialog builder Delete Account
                AlertDialog builder = new AlertDialog.Builder(context).create();
                builder.setTitle("Xác nhận xóa xe ");
                builder.setMessage("Bạn có chắc chắn muốn xóa chiếc xe này không?");
                builder.setButton(AlertDialog.BUTTON_POSITIVE, "Có", (dialog, which) -> {
                   DatabaseReference carIDRef = FirebaseDatabase.getInstance().getReference().child("cars/"+user.getUid()+"/"+car.getId());
                   carIDRef.removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                Log.d("tessss", "onComplete: Xay ra loi khi xoa");
                            } else {
                                Log.d("tessss", "onComplete: Xoa thanh cong");

                            }
                        }
                    });
                });
                builder.setButton(AlertDialog.BUTTON_NEGATIVE, "Không", (dialog, which) -> {
                    builder.dismiss();
                });
                builder.show();
                }
                else {
                    //Create AlertDialog builder Delete Account
                    AlertDialog builder = new AlertDialog.Builder(context).create();
                    builder.setTitle("Kết nối Internet ");
                    builder.setMessage("Mạng Internet của bạn hiện không ổn định, vui lòng kiểm tra lại!");
                    builder.setButton(AlertDialog.BUTTON_POSITIVE, "Oke", (dialog, which) -> {
                    });
                    builder.setButton(AlertDialog.BUTTON_NEGATIVE, "Không", (dialog, which) -> {
                        builder.dismiss();
                    });
                    builder.show();
                }
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

            // Bắt sự kiện chung
            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(MyViewHolder.this);
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
        void onItemClick(MyViewHolder holder);
    }

}

