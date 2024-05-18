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
    private AbstractFragment fragment;
    private boolean checkExistFragment;
    private FragmentTransaction transaction;
    private ArrayList<City> listCities;
    private ArrayList<City> copyList ;
    private CityAdapter adapterCities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khoi tao binding
        binding = ChooseLocationLayoutBinding.inflate(getLayoutInflater());
        // Gán view cho binding
        setContentView(binding.getRoot());

        //Khoi tao mang gia
        listCities = new ArrayList<>();
        listCities.add(new City("hcm"));
        listCities.add(new City("hn"));
        listCities.add(new City("dn"));
        listCities.add(new City("qn"));

        copyList = listCities;
        adapterCities = new CityAdapter(this, copyList);
        LinearLayoutManager layoutManagerCities = new LinearLayoutManager(this);
        layoutManagerCities.setOrientation(RecyclerView.VERTICAL);
        layoutManagerCities.setReverseLayout(false);
        binding.listCities.setLayoutManager(layoutManagerCities);
        binding.listCities.setAdapter(adapterCities);

        //Bat su kien click vao item adapter
        adapterCities.setOnItemClickListener(new CityAdapter.ItemClickListener() {
            @Override
            public void onItemClick(CityAdapter.MyViewHolder holder) {
                CardItemCityLayoutBinding binding1 = (CardItemCityLayoutBinding)holder.getBinding();
                Log.d("TAG", "onItemClick: "+binding1.cityName.getText().toString());
                Intent intent = new Intent(ChooseLocationActivity.this, MainActivity.class);
                intent.putExtra("city", binding1.cityName.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
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
                binding.boxEditTextLocation.setBackgroundResource(R.drawable.border_line_gray);
                binding.boxListLocation.setVisibility(View.INVISIBLE);
                binding.icPickNow.setVisibility(View.VISIBLE);
                binding.tvLocationNow.setVisibility(View.VISIBLE);
                binding.icDelete.setVisibility(View.INVISIBLE);
                binding.editTextPickLocation.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.editTextPickLocation.getWindowToken(), 0);
            }
        });

        // Bat su kien khi nhap ky tu
        binding.editTextPickLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                binding.icDelete.setVisibility(View.VISIBLE);
                if (s.toString().isEmpty()) {
                    Log.d("test", "onTextChanged: empty");
                    copyList = listCities;
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
                adapterCities.setListCites(copyList);
                adapterCities.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("test", "afterTextChanged: "+s.toString());
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

    }
}