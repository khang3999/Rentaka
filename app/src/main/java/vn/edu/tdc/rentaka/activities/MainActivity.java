package vn.edu.tdc.rentaka.activities;

import android.content.ClipData;
import android.content.Intent;
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

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.MainLayoutBinding;
import vn.edu.tdc.rentaka.fragments.AbstractFragment;
import vn.edu.tdc.rentaka.fragments.HistoryFragment;
import vn.edu.tdc.rentaka.fragments.HomeFragment;
import vn.edu.tdc.rentaka.fragments.NewsFragment;
import vn.edu.tdc.rentaka.fragments.NotificationFragment;
import vn.edu.tdc.rentaka.fragments.PersonalProfileFragment;

public class MainActivity extends AppCompatActivity {
// <<<<<<< future/confirm-rental-ui
//     private DatabaseReference mDatabase;

//     @Override
//     protected void onCreate(Bundle savedInstanceState) {
//         super.onCreate(savedInstanceState);
//         EdgeToEdge.enable(this);
//         setContentView(R.layout.confirm_rental_layout);

//         mDatabase = FirebaseDatabase.getInstance("https://rentaka-android-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
// //        writeNewUser("1", "Alice", "Alice@email.com");
//         ValueEventListener postListener = new ValueEventListener() {
//             @Override
//             public void onDataChange(DataSnapshot dataSnapshot) {
//                 // Get Post object and use the values to update the UI
// //                ArrayList<User> users = new ArrayList<>();
// //                for (DataSnapshot userSnapshot : dataSnapshot.child("users").getChildren()) {
// //                    User user = userSnapshot.getValue(User.class);
// //                    users.add(user);
// //                }
// //                Log.d("Test", "onDataChange: "+users.toString());
//                 User user = dataSnapshot.child("users").child("1").getValue(User.class);
//                 Log.d("Test", "onDataChange: "+user.toString());
//             }

//             @Override
//             public void onCancelled(DatabaseError databaseError) {
//                 // Getting Post failed, log a message
//                 Log.d("Test", "loadPost:onCancelled", databaseError.toException());
//             }
//         };
//         mDatabase.addValueEventListener(postListener);
//     }
//     public void writeNewUser(String userId, String name, String email) {
//         User user = new User(name, email);

    //         mDatabase.child("users").child(userId).child("username").setValue(name);
// =======
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
                if (menuItem.getItemId() == R.id.homeItemMenu) {
                    currentFragment = 0;
                } else if (menuItem.getItemId() == R.id.newsItemMenu) {
                    currentFragment = 1;
                } else if (menuItem.getItemId() == R.id.historyItemMenu) {
                    currentFragment = 2;
                } else if (menuItem.getItemId() == R.id.notificationItemMenu) {
                    currentFragment = 3;
                } else if (menuItem.getItemId() == R.id.profileItemMenu) {
                    currentFragment = 4;
                }
                updateUI();
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        Log.d("yu", "onResume: " + intent.getStringExtra("city"));

        updateUI();
    }

        private void updateUI () {
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
    }

