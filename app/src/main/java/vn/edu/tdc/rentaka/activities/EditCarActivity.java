package vn.edu.tdc.rentaka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.EditCarLayoutBinding;
import vn.edu.tdc.rentaka.models.Car;
import vn.edu.tdc.rentaka.models.Status;
import vn.edu.tdc.rentaka.models.UserModel;

public class EditCarActivity extends AppCompatActivity {

    private EditCarLayoutBinding binding;
    private Car car;
    private Intent intent;
    private ArrayList<Status> listStatus;
    private ArrayList<String> listStatusTitle = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = EditCarLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listStatus = new ArrayList<>();

        //Button top back navigation
        setSupportActionBar(binding.topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditCarActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                // Main vao tu trai, choose date exit ve ben phai
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = Integer.parseInt(binding.edtPriceDaily.getText().toString());
                int salary = Integer.parseInt(binding.edtSalaryDriver.getText().toString());
                Status status = listStatus.get(binding.status.getSelectedItemPosition());
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userid = user.getUid();
                Log.d("tessss", "userid edit: " + userid);
                Log.d("tessss", "carid edit: " + car.getId());
                Log.d("tessss", "status edit: " + status);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cars").child(userid).child(car.getId());
                Map<String, Object> updates = new HashMap<>();
                updates.put("priceDaily",price);
                updates.put("salaryDriver",salary);
                updates.put("statusId",status);
                // Cập nhật thông tin xe trong cơ sở dữ liệu
                databaseReference.updateChildren(updates)
                        .addOnSuccessListener(aVoid -> {
                            // Khi cập nhật thành công, hiển thị thông báo thành công và đóng bottom sheet
                            Toast.makeText(EditCarActivity.this, "Sửa thông tin xe thành công", Toast.LENGTH_SHORT).show();
                            finish();

                        })
                        .addOnFailureListener(e -> {
                            // Nếu cập nhật thất bại, hiển thị thông báo lỗi
                            Toast.makeText(EditCarActivity.this, "Sửa thông tin xe thất bại", Toast.LENGTH_SHORT).show();
                        });
            }
        });


    }

    protected void onResume() {
        super.onResume();
        intent = getIntent();
        String carId = intent.getStringExtra("carid");
        Log.d("tesssss", "carid edit: "+carId);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cars");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    for (DataSnapshot carSnap : userSnap.getChildren()) {
                        car = new Car();
                        car = carSnap.getValue(Car.class);

                        Log.d("tesssss", "carrental: "+car);
                        if ((carId).equals(car.getId())){
                            break;
                        }
                    }
                    if ((carId).equals(car.getId())){
                        break;
                    }
                }

                Log.d("tesssss", "carrental: ngoai "+car);
                binding.edtPriceDaily.setText(car.getPriceDaily()+"");
                binding.edtSalaryDriver.setText(car.getSalaryDriver()+"");

                DatabaseReference statusReference = FirebaseDatabase.getInstance().getReference("statuses").child("car");
                statusReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listStatusTitle.clear();
                        for (DataSnapshot snapshotId : snapshot.getChildren()) {

                            Status status = snapshotId.getValue(Status.class);
                            listStatus.add(status);
                            listStatusTitle.add(status.getTitle());
                        }

                        Log.d("tesssss", "listStatus: "+listStatus);
                        adapter = new ArrayAdapter<String>(EditCarActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, listStatusTitle);
                        binding.status.setAdapter(adapter);

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
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }
}