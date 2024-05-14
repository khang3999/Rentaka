package vn.edu.tdc.rentaka.activities;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import vn.edu.tdc.rentaka.APIs.FirebaseAPI;
import vn.edu.tdc.rentaka.APIs.StorageAPI;
import vn.edu.tdc.rentaka.R;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;

import vn.edu.tdc.rentaka.databinding.MainLayoutBinding;
import vn.edu.tdc.rentaka.fragments.AbstractFragment;
import vn.edu.tdc.rentaka.fragments.HistoryFragment;
import vn.edu.tdc.rentaka.fragments.HomeFragment;
import vn.edu.tdc.rentaka.fragments.NewsFragment;
import vn.edu.tdc.rentaka.fragments.NotificationFragment;
import vn.edu.tdc.rentaka.fragments.PersonalProfileFragment;


public class MainActivity extends AppCompatActivity {
    FirebaseAPI firebaseAPI = new FirebaseAPI();
    StorageAPI storageAPI = new StorageAPI();


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.confirm_rental_layout);
//
//
//        Date date = new Date(LocalDate.now());
//
//        Customer owner = new Customer("Tran", "Phuc", "tranhieuphuc12@gmail.com", "123456789", new Location("LA", "location 43", Location.LocationType.customer), "123456789", Customer.CustomerType.owner);
//        owner.setId("837N5VnztJt7JRKKefo5");
//        Customer renter = new Customer("Nguyen", "Nhi", "phuongnhi12@gmail.com", "987654321", new Location("An Giang", "location 41", Location.LocationType.customer), "987654321", Customer.CustomerType.renter);
//        Car car = new Car(owner.getId(), "Toyota", "Camry", "2021", "Black", "description", 2019, 4);
//        car.setId("TIZUxOJ00qGBAuShSlJ9");
//        car.setStatusID("foD0J0b2jeHV9LJH3dqJ");
//        Reservation reservation = new Reservation("TIZUxOJ00qGBAuShSlJ9", owner.getId(), null, null,null,null,null, 100.0);

//        reservation.setRenterID("K3MZ3sL0Uu83Of1ycDPP");
//        reservation.setPickUpDate(date);
//        reservation.setReturnDate(date);
//        reservation.setPickUpLocation(new Location("LA", "location 43", Location.LocationType.pickUpLocation));
//        reservation.setReturnLocation(new Location("AL", "location 34", Location.LocationType.returnLocation));
//        reservation.setId("xCWQ4Jj43A2aen35FDfm");
//        reservation.setStatusID("Vmmfoz79pLKTWPjljMJw");
//
//
//        Button selectImage = (Button) findViewById(R.id.send_rental_request_button);
//        Button uploadImage = (Button) findViewById(R.id.promotion_code_title_textview);


//        selectImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                storageAPI.selectImage(MainActivity.this);
//            }
//        });
//        uploadImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            storageAPI.uploadImage(MainActivity.this);
//            }
//        });



//    }

    //Properties
    private AbstractFragment fragment;
    private int currentFragment = 0;
    private MainLayoutBinding binding;
    // doi tuong dung de dan fragment vao khung man hinh
    private FragmentTransaction transaction;

    // Set color when click
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        //Khoi tao binding
        binding = MainLayoutBinding.inflate(getLayoutInflater());
        // Gán view cho binding
        setContentView(binding.getRoot());

        fragment = new HomeFragment();
        updateUI();

        binding.bottomMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.homeItemMenu){
                    currentFragment = 0;
                } else if(menuItem.getItemId() == R.id.newsItemMenu){
                    currentFragment = 1;
                } else if(menuItem.getItemId() == R.id.historyItemMenu){
                    currentFragment = 2;
                } else if(menuItem.getItemId() == R.id.notificationItemMenu){
                    currentFragment = 3;
                } else if(menuItem.getItemId() == R.id.profileItemMenu){
                    currentFragment = 4;
                }
                updateUI();
                return true;
            }
        });

    }

    private void updateUI() {
        // Set title
        // Neu da ton tai thi tai su dung
        if (getSupportFragmentManager().findFragmentByTag(currentFragment + "") != null) {
            fragment = (AbstractFragment) getSupportFragmentManager().findFragmentByTag(currentFragment + "");
        }
        // Neu chua ton tai thi tao moi fragment
        else {
            // Tao doi tuong fragment tuong ung
            if (currentFragment == 0) {
                fragment = new HomeFragment();
            } else if (currentFragment == 1) {
                fragment = new NewsFragment();
            } else if (currentFragment == 2) {
                fragment = new HistoryFragment();
            } else if (currentFragment == 3) {
                fragment = new NotificationFragment();
            } else if (currentFragment == 4) {
                fragment = new PersonalProfileFragment();
            }
        }

        // CHUAN BI CHO TRANSACTION
        //Lay doi tuong fragment transaction
        transaction = getSupportFragmentManager().beginTransaction();
        // Do du lieu vao doi tuong va dan doi tuong fragment vao khung man hinh,
        // voi tham so dau tien la id cua khung chua fragment o layout )
        transaction.replace(R.id.fragmentContainer, fragment, currentFragment + "");
        // Dua fragment vao trong Stack neu chua ton tai
        if (getSupportFragmentManager().findFragmentByTag(currentFragment + "") == null) {
            transaction.addToBackStack(null);
        }
        // Yeu cau thuc hien transaction
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == storageAPI.PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            storageAPI.filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                storageAPI.filePath);
                ImageView imageView = (ImageView) findViewById(R.id.car_img);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }

    }
}

