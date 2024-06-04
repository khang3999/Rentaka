package vn.edu.tdc.rentaka.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
    private  String imageUrl;

    private UserModel owner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rentalBinding = RentalDetailLayoutBinding.inflate(getLayoutInflater());
        bottomSheetBinding = BottomSheetDiaglogLayoutBinding.inflate(getLayoutInflater(),null,false);

        setContentView(rentalBinding.getRoot());

        Intent intent = new Intent();
        String carId = intent.getStringExtra("car");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cars");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot data : snapshot.getChildren()) {
                    for (DataSnapshot user : data.getChildren()) {
                        car = new Car();
                        car = user.getValue(Car.class);
                        if (car.getId().equals(carId)){
                            return;
                        }

                    }


                }
                Log.d("tesssss", "car: "+car);
                 imageUrl = car.getImageCarUrl();

                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(RentalDetailActivity.this)
                            .load(imageUrl)
                            .into(rentalBinding.imageCar);
                } else {
                    //Set anh mac dinh
                    rentalBinding.imageCar.setImageResource(R.drawable.car);
                }
                rentalBinding.nameCar.setText(car.getBrand()+" "+car.getModel());
                rentalBinding.type.setText(car.getTypeGearbox()+"");
                rentalBinding.seat.setText(car.getSeat()+"");
                rentalBinding.fuel.setText(car.getFuel()+"");

                DatabaseReference ownerReference = FirebaseDatabase.getInstance().getReference("users");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot snapshotId : snapshot.getChildren()) {
                            if (snapshotId.getKey().equals(car.getOwnerID())){
                                owner = snapshotId.getValue(UserModel.class);
                            }

                        }
                        Log.d("tesssss", "owner: "+owner);
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



                rentalBinding.mortageDescription.setText(car.getMortgage()+"");
                rentalBinding.priceSale.setText(car.getPriceDaily()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//


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
    }
}