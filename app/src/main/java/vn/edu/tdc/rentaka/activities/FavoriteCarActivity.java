package vn.edu.tdc.rentaka.activities;

import android.os.Bundle;
import android.view.View;

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

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.adapters.CarAdapter;
import vn.edu.tdc.rentaka.databinding.FavoriteCarLayoutBinding;
import vn.edu.tdc.rentaka.models.Car;

public class FavoriteCarActivity extends AppCompatActivity {
    private CarAdapter carAdapter;
    private FavoriteCarLayoutBinding binding;
    private ArrayList<Car> favoriteCars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FavoriteCarLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.topAppBar.setNavigationOnClickListener(v -> finish());

        favoriteCars = new ArrayList<>();
        carAdapter = new CarAdapter(this, favoriteCars);
        binding.listFavoriteCar.setLayoutManager(new LinearLayoutManager(this));
        binding.listFavoriteCar.setAdapter(carAdapter);

        loadFavoriteCars();
    }

    private void loadFavoriteCars() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference favRef = FirebaseDatabase.getInstance().getReference("favourites").child(user.getUid());
            favRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    favoriteCars.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Car car = dataSnapshot.getValue(Car.class);
                        if (car != null) {
                            favoriteCars.add(car);
                        }
                    }
                    carAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle possible errors.
                }
            });
        }
    }
}
