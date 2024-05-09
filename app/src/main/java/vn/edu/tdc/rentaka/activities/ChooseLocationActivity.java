package vn.edu.tdc.rentaka.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.adapters.CityAdapter;
import vn.edu.tdc.rentaka.databinding.ChooseLocationLayoutBinding;
import vn.edu.tdc.rentaka.fragments.AbstractFragment;
import vn.edu.tdc.rentaka.fragments.HistoryFragment;
import vn.edu.tdc.rentaka.fragments.HomeFragment;
import vn.edu.tdc.rentaka.fragments.ListLocationFragment;
import vn.edu.tdc.rentaka.fragments.NewsFragment;
import vn.edu.tdc.rentaka.fragments.NotificationFragment;
import vn.edu.tdc.rentaka.fragments.PersonalProfileFragment;
import vn.edu.tdc.rentaka.fragments.PickLocationCurrentFragment;
import vn.edu.tdc.rentaka.models.City;

public class ChooseLocationActivity extends AppCompatActivity {
    private ChooseLocationLayoutBinding binding;
    private AbstractFragment fragment;
    private boolean checkExistFragment;
    private FragmentTransaction transaction;
    private ListLocationFragment listLocationFragment;
    private int currentFragment = 0;
    private ArrayList<City> copyList ;
    private ArrayList<City> listCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khoi tao binding
        binding = ChooseLocationLayoutBinding.inflate(getLayoutInflater());
        // Gán view cho binding
        setContentView(binding.getRoot());

        //Khong focus vao edit text
        fragment = new PickLocationCurrentFragment();
        updateUI();

        //Focus vao edit text
        binding.editTextPickLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //Chang background
                binding.boxEditTextLocation.setBackgroundResource(R.drawable.border_line_green);

                // Do fragment list location
                currentFragment = 1;
                updateUI();

            }
        });







        binding.editTextPickLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().isEmpty()) {
                    Log.d("test", "onTextChanged: empty");
                    copyList = listLocationFragment.getListCities();
                    for (City c : copyList
                    ) {
                        Log.d("test", "empty: " + c.getName());
                    }
                } else {

                    Log.d("test", "onTextChanged: " + s.toString());
                    copyList = new ArrayList<>();
                    for (City c : listCities) {
                        if (c.getName().contains(s.toString())) {
                            copyList.add(c);
                            Log.d("test", "filter: " + c.getName());
                        }
                    }
                }
                listLocationFragment.setCopyList(copyList);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        binding.btnDone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ChooseLocationActivity.this, MainActivity.class);
//                intent.putExtra("city", binding.edtChooseLocation.getText().toString());
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                Log.d("goi", "onClick: "+intent.getStringExtra("city"));
//                startActivity(intent);
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                fragment = new PickLocationCurrentFragment();
            } else if (currentFragment == 1) {
                fragment = new ListLocationFragment();
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