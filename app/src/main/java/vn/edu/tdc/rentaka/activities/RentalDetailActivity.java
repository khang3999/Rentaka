package vn.edu.tdc.rentaka.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.BottomSheetDiaglogLayoutBinding;
import vn.edu.tdc.rentaka.databinding.RentalDetailLayoutBinding;
import vn.edu.tdc.rentaka.fragments.HomeFragment;
import vn.edu.tdc.rentaka.models.Car;
import vn.edu.tdc.rentaka.models.UserModel;

public class RentalDetailActivity extends AppCompatActivity {

    private BottomSheetDialog bottomSheetDialog;
    private RentalDetailLayoutBinding rentalBinding;

    private BottomSheetDiaglogLayoutBinding bottomSheetBinding;
    private Car car;
    private String imageUrl;

    private UserModel owner;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rentalBinding = RentalDetailLayoutBinding.inflate(getLayoutInflater());
        bottomSheetBinding = BottomSheetDiaglogLayoutBinding.inflate(getLayoutInflater(), null, false);

        setContentView(rentalBinding.getRoot());

        //Button top back navigation
        setSupportActionBar(rentalBinding.topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rentalBinding.topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RentalDetailActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                // Main vao tu trai, choose date exit ve ben phai
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });
        intent = new Intent();


        //Gach chan text
        rentalBinding.btnMore.setPaintFlags(rentalBinding.btnMore.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //Bottom sheet
        bottomSheetDialog = new BottomSheetDialog(
                RentalDetailActivity.this, R.style.BottomSheetDialogTheme
        );
        bottomSheetDialog.setContentView(bottomSheetBinding.getRoot());
        //Bat su kien khi click vao Xem them
        rentalBinding.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });
        //Bat su kien khi click vao x de tat bottom sheet
        bottomSheetBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        //Catch on click event for the button "Choose rental"
        rentalBinding.btnRent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RentalDetailActivity.this, ConfirmRentalActivity.class);
                intent.putExtra("car", car.getId());
                startActivity(intent);
                // Main vao tu trai, choose date exit ve ben phai
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        intent = getIntent();
        String carId = intent.getStringExtra("car");
        Log.d("tesssss", "carid: " + carId);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cars");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnap : snapshot.getChildren()) {
                    for (DataSnapshot carSnap : userSnap.getChildren()) {
                        car = new Car();
                        car = carSnap.getValue(Car.class);

                        Log.d("tesssss", "carrental: " + car);
                        if (car.getId().equals(carId)) {
                            break;
                        }
                    }
                    if (car.getId().equals(carId)) {
                        break;
                    }
                }

                Log.d("tesssss", "carrental: ngoai " + car);
                imageUrl = car.getImageCarUrl();

                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(RentalDetailActivity.this)
                            .load(imageUrl)
                            .into(rentalBinding.imageCar);
                } else {
                    //Set anh mac dinh
                    rentalBinding.imageCar.setImageResource(R.drawable.car);
                }
                rentalBinding.nameCar.setText(car.getBrand() + " " + car.getModel());
                rentalBinding.type.setText(car.getTypeGearbox() + "");
                rentalBinding.seat.setText(car.getSeat() + "");
                rentalBinding.fuel.setText(car.getFuel() + "");

                DatabaseReference ownerReference = FirebaseDatabase.getInstance().getReference("users");
                ownerReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot snapshotId : snapshot.getChildren()) {
                            if (snapshotId.getKey().equals(car.getOwnerID())) {
                                owner = snapshotId.getValue(UserModel.class);
                                break;
                            }

                        }

                        Log.d("tesssss", "owner: " + owner);
                        String imageOwnerUrl = owner.getImageUser();
                        if (imageOwnerUrl != null && !imageOwnerUrl.isEmpty()) {
                            Glide.with(RentalDetailActivity.this)
                                    .load(imageOwnerUrl)
                                    .into(rentalBinding.ownerImg);
                        } else {
                            //Set anh mac dinh
                            rentalBinding.ownerImg.setImageResource(R.drawable.avatar);
                        }

                        rentalBinding.ownerName.setText(owner.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                rentalBinding.mortageDescription.setText(car.getMortgage() + "");
                rentalBinding.priceSale.setText(car.getPriceDaily() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}