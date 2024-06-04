package vn.edu.tdc.rentaka.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.rentaka.APIs.FirebaseAPI;
import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.adapters.CityAdapter;
import vn.edu.tdc.rentaka.databinding.CardItemCityLayoutBinding;
import vn.edu.tdc.rentaka.databinding.ChooseLocationLayoutBinding;
import vn.edu.tdc.rentaka.fragments.AbstractFragment;
import vn.edu.tdc.rentaka.fragments.HistoryFragment;
import vn.edu.tdc.rentaka.fragments.HomeFragment;
import vn.edu.tdc.rentaka.fragments.NewsFragment;
import vn.edu.tdc.rentaka.fragments.NotificationFragment;
import vn.edu.tdc.rentaka.fragments.PersonalProfileFragment;
import vn.edu.tdc.rentaka.models.City;

public class ChooseLocationActivity extends AppCompatActivity {
    private ChooseLocationLayoutBinding binding;
    private FirebaseAPI firebaseAPI;
    private ArrayList<City> copyList = new ArrayList<>();
    private ArrayList<City> listCities = new ArrayList<>();
    private CityAdapter adapterCities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khoi tao binding
        binding = ChooseLocationLayoutBinding.inflate(getLayoutInflater());
        // Gán view cho binding
        setContentView(binding.getRoot());
        firebaseAPI = new FirebaseAPI();
        //Button top back navigation
        setSupportActionBar(binding.topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseLocationActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                // Main vao tu trai, choose date exit ve ben phai
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });
        //Goi ham lay list cities
        firebaseAPI.fetchCities(listCities);
        copyList.addAll(listCities);
        //Copy de search
        adapterCities = new CityAdapter(ChooseLocationActivity.this, copyList);
        LinearLayoutManager layoutManagerCities = new LinearLayoutManager(ChooseLocationActivity.this);
        layoutManagerCities.setOrientation(RecyclerView.VERTICAL);
        layoutManagerCities.setReverseLayout(false);
        binding.listCities.setLayoutManager(layoutManagerCities);
        binding.listCities.setAdapter(adapterCities);

        //Bat su kien khi search
        binding.editTextPickLocation.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.icDelete.setVisibility(View.VISIBLE);
                if (s.toString().isEmpty()) {
                    Log.d("test", "onTextChanged: aaaa");
                    copyList.clear();
                    copyList.addAll(listCities);

                } else {

                    Log.d("test", "onTextChanged: " + s.toString());
                    copyList.clear();
                    for (City c : listCities) {
                        if (c.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            copyList.add(c);
                        }
                    }
                }
                adapterCities.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("test", "afterTextChanged: " + s.toString());
            }
        });

        //Bat su kien click vao item adapter
        adapterCities.setOnItemClickListener(new CityAdapter.ItemClickListener() {
            @Override
            public void onItemClick(CityAdapter.MyViewHolder holder) {
                CardItemCityLayoutBinding binding1 = (CardItemCityLayoutBinding) holder.getBinding();
                Log.d("TAG", "onItemClick: " + binding1.cityName.getText().toString());
                Intent intent = new Intent(ChooseLocationActivity.this, MainActivity.class);
                intent.putExtra("city", binding1.cityName.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                // Main vao tu trai, choose date exit ve ben phai
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

        // bat su kien khi focus do edittext
        binding.editTextPickLocation.setOnFocusChangeListener(
                new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        binding.boxEditTextLocation.setBackgroundResource(R.drawable.border_line_green);
                        binding.boxListLocation.setVisibility(View.VISIBLE);
                        binding.icPickNow.setVisibility(View.INVISIBLE);
                        binding.tvLocationNow.setVisibility(View.INVISIBLE);
                        binding.editTextPickLocation.setText("");
                    }
                });


        //Bat su kien khi nhan delete
        binding.icDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUIDefault();
            }
        });

        Log.d("test", "onCreate: final");


    }



    @Override
    protected void onResume() {
        super.onResume();
//        firebaseAPI.fetchCities(listCities);
//        copyList.clear();
//        copyList.addAll(listCities);
//        adapterCities.notifyDataSetChanged();
        setUIDefault();
    }


    private void setUIDefault() {
        binding.editTextPickLocation.clearFocus();
        binding.boxEditTextLocation.setBackgroundResource(R.drawable.border_line_gray);
        binding.boxListLocation.setVisibility(View.INVISIBLE);
        binding.icPickNow.setVisibility(View.VISIBLE);
        binding.tvLocationNow.setVisibility(View.VISIBLE);
        binding.icDelete.setVisibility(View.INVISIBLE);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binding.editTextPickLocation.getWindowToken(), 0);

    }
}

