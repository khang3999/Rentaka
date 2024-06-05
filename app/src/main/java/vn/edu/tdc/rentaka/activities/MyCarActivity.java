package vn.edu.tdc.rentaka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.adapters.CarAdapter;
import vn.edu.tdc.rentaka.adapters.MyCarAdapter;
import vn.edu.tdc.rentaka.databinding.CardCarItemBinding;
import vn.edu.tdc.rentaka.databinding.FavoriteCarLayoutBinding;
import vn.edu.tdc.rentaka.databinding.MycarItemLayoutBinding;
import vn.edu.tdc.rentaka.databinding.MycarLayoutBinding;
import vn.edu.tdc.rentaka.models.Car;

public class MyCarActivity extends AppCompatActivity {
    private MyCarAdapter myCarAdapter;
    private MycarLayoutBinding binding;
    private ArrayList<Car> listMyCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = MycarLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.topAppBar.setNavigationOnClickListener(v -> finish());

        listMyCar = new ArrayList<>();
        myCarAdapter = new MyCarAdapter(this, listMyCar);
        binding.listCar.setLayoutManager(new LinearLayoutManager(this));
        binding.listCar.setAdapter(myCarAdapter);


        loadMyListCars();
    }

    private void loadMyListCars() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getUid() != null) {
            DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("cars").child(user.getUid());
            favRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listMyCar.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Car car = dataSnapshot.getValue(Car.class);
                        if (car != null) {
                            listMyCar.add(car);
                        }
                    }
                    myCarAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle possible errors.
                }
            });
        }
    }
}