package vn.edu.tdc.rentaka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.adapters.CarAdapter;
import vn.edu.tdc.rentaka.databinding.CardCarItemBinding;
import vn.edu.tdc.rentaka.databinding.ListCarSearchLayoutBinding;
import vn.edu.tdc.rentaka.models.Car;
import vn.edu.tdc.rentaka.models.City;

public class ListCarSearchActivity extends AppCompatActivity {
    private ListCarSearchLayoutBinding binding;
    private ArrayList<Car> listCars;
    private CarAdapter carAdapter;
    private String location = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ListCarSearchLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set event for back button
        setSupportActionBar(binding.topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListCarSearchActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                // Thiết lập animation
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });
        //Set adapter
        listCars = new ArrayList<>();
        carAdapter = new CarAdapter(ListCarSearchActivity.this, listCars);
        LinearLayoutManager layoutManagerCities = new LinearLayoutManager(ListCarSearchActivity.this);
        layoutManagerCities.setOrientation(RecyclerView.VERTICAL);
        layoutManagerCities.setReverseLayout(false);
        binding.listCar.setLayoutManager(layoutManagerCities);
        binding.listCar.setAdapter(carAdapter);

    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Log.d("tesssss", "onResume: lc +  "+intent.getStringExtra("location"));
        if (intent.hasExtra("location")){
            location = intent.getStringExtra("location");
            binding.location.setText(intent.getStringExtra("location"));
        }
        if (intent.hasExtra("date")){
            binding.date.setText(intent.getStringExtra("date"));
        } else {
            // Lấy thời gian hiện tại
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Tạo định dạng cho giờ và ngày tháng
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Tạo LocalDateTime cho (giờ hiện tại + 1 giờ) và (giờ hiện tại + 2 giờ)
            LocalDateTime dateTimePlusOneHour = currentDateTime.plus(1, ChronoUnit.HOURS);
            LocalDateTime dateTimePlusTwoHours = currentDateTime.plus(2, ChronoUnit.HOURS);

            // Format giờ và ngày tháng cho cả hai thời điểm
            String formattedTimePlusOneHour = dateTimePlusOneHour.format(timeFormatter);
            String formattedDatePlusOneHour = dateTimePlusOneHour.format(dateFormatter);

            String formattedTimePlusTwoHours = dateTimePlusTwoHours.format(timeFormatter);
            String formattedDatePlusTwoHours = dateTimePlusTwoHours.format(dateFormatter);

            binding.date.setText(formattedTimePlusOneHour+"h00, "+formattedDatePlusOneHour+" - "+formattedTimePlusTwoHours+"h00, "+formattedDatePlusTwoHours);
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getUid()!= null){
            //Lay ra list xe co dia diem minh chon
            DatabaseReference carsRef = FirebaseDatabase.getInstance().getReference("cars");
            carsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listCars.clear();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        if (userSnapshot.getKey().equals(user.getUid())) {
                            continue;
                        }
                        for (DataSnapshot carSnapshot : userSnapshot.getChildren()) {
                            Car car = carSnapshot.getValue(Car.class);
                            //Xe nao chua san sang thi khong hien thi
                            if (car.getStatusId().getId()==1){
                                continue;
                            }
                            City city = new City();
                            city = car.getCity();
                            String nameCity = city.getName();
                            if (location.equals(nameCity)){
                                listCars.add(car);
                            }

                        }
                    }
                    Log.d("tesss", "listCar at "+location+": "+listCars);
                    carAdapter.notifyDataSetChanged();
                    //Bat su kien khi click vao item caradapter
                    carAdapter.setOnItemClickListener(new CarAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(CarAdapter.MyViewHolder holder) {
                            CardCarItemBinding binding1 = (CardCarItemBinding) holder.getBinding();
                            Intent intent = new Intent(ListCarSearchActivity.this, RentalDetailActivity.class);
                            intent.putExtra("car", listCars.get(holder.getAdapterPosition()).getId());
                            Log.d("tesss", "position: "+holder.getAdapterPosition());
                            Log.d("tesssss", "carid: "+listCars.get(holder.getAdapterPosition()).getId());
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
//                // Main vao tu trai, choose date exit ve ben phai
//                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }
}