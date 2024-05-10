package vn.edu.tdc.rentaka.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import vn.edu.tdc.rentaka.APIs.FirebaseAPI;
import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.models.Car;
import vn.edu.tdc.rentaka.models.Location;
import vn.edu.tdc.rentaka.models.Reservation;
import vn.edu.tdc.rentaka.models.*;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.confirm_rental_layout);
        FirebaseAPI firebaseAPI = new FirebaseAPI();

        Date date = new Date(LocalDate.now());

        Customer owner = new Customer("Tran", "Phuc", "tranhieuphuc12@gmail.com", "123456789", new Location("LA", "location 43", Location.LocationType.CUSTOMER), "123456789", Customer.CustomerType.OWNER);
        owner.setId("O531Elqv5WDnnvSgeSqH");
        Customer renter = new Customer("Nguyen", "Nhi", "phuongnhi12@gmail.com", "987654321", new Location("An Giang", "location 41", Location.LocationType.CUSTOMER), "987654321", Customer.CustomerType.RENTER);
        Car car = new Car(owner.getId(), "Toyota", "Camry", "2021", "Black", "description", 2019, 4);

        Reservation reservation = new Reservation("BeLYW9Tx1szs5DYoNvwF", owner.getId(), null, null,null,null,null, 100.0);

        reservation.setRenterID("7F846Q126a8lR9JnA95C");
        reservation.setPickUpDate(date);
        reservation.setReturnDate(date);
        reservation.setPickUpLocation(new Location("LA", "location 43", Location.LocationType.PICKUP));
        reservation.setReturnLocation(new Location("AL", "location 34", Location.LocationType.RETURN));
        reservation.setId("g6nCqFWCxquDFpd3U9dx");
        reservation.setStatusID("ae8HLUo0St6NpiBtEkwa");

//        firebaseAPI.addCustomer(renter);
//        firebaseAPI.addReservation(reservation);

        Log.d("TEST", "onCreate: " + reservation.toString());
//    firebaseAPI.updateReservationWhenRenterRentsCar(reservation);
        firebaseAPI.updateReservationWhenOwnerAcceptsRenterRequest(reservation);
//        firebaseAPI.fetchCustomersByProperty("email","tranhieuphuc12@gmail.com", new FirebaseAPI.onCallBack<Customer>() {
//            @Override
//            public void onCallBack(List<Customer> List) {
//                for (Customer customer : List) {
//                    Log.d("Customer", customer.toString());
//                }
//            }
//        });
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

