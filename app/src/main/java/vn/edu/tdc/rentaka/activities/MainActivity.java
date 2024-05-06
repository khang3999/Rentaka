package vn.edu.tdc.rentaka.activities;

import android.content.ClipData;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.rentaka.APIs.FirebaseAPI;
import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.MainLayoutBinding;
import vn.edu.tdc.rentaka.fragments.AbstractFragment;
import vn.edu.tdc.rentaka.fragments.HistoryFragment;
import vn.edu.tdc.rentaka.fragments.HomeFragment;
import vn.edu.tdc.rentaka.fragments.NewsFragment;
import vn.edu.tdc.rentaka.fragments.NotificationFragment;
import vn.edu.tdc.rentaka.fragments.PersonalProfileFragment;
import vn.edu.tdc.rentaka.models.Car;
import vn.edu.tdc.rentaka.models.Status;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.confirm_rental_layout);
        FirebaseAPI firebaseAPI = new FirebaseAPI();
//        firebaseAPI.addCar(new Car("Corolla", "Toyota", "Red", "29A-12345", "Available", "This is a car", 2019, 4));
         firebaseAPI.fetchCars(new FirebaseAPI.onCallback<Car>() {
             @Override
             public void onCallback(List<Car> List) {
                 for (Car car : List) {
                     Log.d("Car", car.toString());
                 }
             }
         });




    }

// =======
    //Properties
//    private AbstractFragment fragment;
//    private int currentFragment = 0;
//    private MainLayoutBinding binding;
//    // doi tuong dung de dan fragment vao khung man hinh
//    private FragmentTransaction transaction;
//
//    // Set color when click
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_layout);
//
//        //Khoi tao binding
//        binding = MainLayoutBinding.inflate(getLayoutInflater());
//        // Gán view cho binding
//        setContentView(binding.getRoot());
//
//        fragment = new HomeFragment();
//        updateUI();
//
//        binding.bottomMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                if (menuItem.getItemId() == R.id.homeItemMenu){
//                    currentFragment = 0;
//                } else if(menuItem.getItemId() == R.id.newsItemMenu){
//                    currentFragment = 1;
//                } else if(menuItem.getItemId() == R.id.historyItemMenu){
//                    currentFragment = 2;
//                } else if(menuItem.getItemId() == R.id.notificationItemMenu){
//                    currentFragment = 3;
//                } else if(menuItem.getItemId() == R.id.profileItemMenu){
//                    currentFragment = 4;
//                }
//                updateUI();
//                return true;
//            }
//        });
//
//    }
//
//    private void updateUI() {
//        // Set title
//        // Neu da ton tai thi tai su dung
//        if (getSupportFragmentManager().findFragmentByTag(currentFragment + "") != null) {
//            fragment = (AbstractFragment) getSupportFragmentManager().findFragmentByTag(currentFragment + "");
//        }
//        // Neu chua ton tai thi tao moi fragment
//        else {
//            // Tao doi tuong fragment tuong ung
//            if (currentFragment == 0) {
//                fragment = new HomeFragment();
//            } else if (currentFragment == 1) {
//                fragment = new NewsFragment();
//            } else if (currentFragment == 2) {
//                fragment = new HistoryFragment();
//            } else if (currentFragment == 3) {
//                fragment = new NotificationFragment();
//            } else if (currentFragment == 4) {
//                fragment = new PersonalProfileFragment();
//            }
//        }
//
//        // CHUAN BI CHO TRANSACTION
//        //Lay doi tuong fragment transaction
//        transaction = getSupportFragmentManager().beginTransaction();
//        // Do du lieu vao doi tuong va dan doi tuong fragment vao khung man hinh,
//        // voi tham so dau tien la id cua khung chua fragment o layout )
//        transaction.replace(R.id.fragmentContainer, fragment, currentFragment + "");
//        // Dua fragment vao trong Stack neu chua ton tai
//        if (getSupportFragmentManager().findFragmentByTag(currentFragment + "") == null) {
//            transaction.addToBackStack(null);
//        }
//        // Yeu cau thuc hien transaction
//        transaction.commit();
//
//    }
}

