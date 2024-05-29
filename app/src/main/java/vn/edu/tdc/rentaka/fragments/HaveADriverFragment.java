package vn.edu.tdc.rentaka.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.rentaka.APIs.FirebaseAPI;
import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.adapters.CarAdapter;
import vn.edu.tdc.rentaka.databinding.HaveADriverLayoutBinding;
import vn.edu.tdc.rentaka.databinding.SelfDrivingCarLayoutBinding;
import vn.edu.tdc.rentaka.models.Car;


public class HaveADriverFragment extends Fragment
{
    private FirebaseAPI firebaseAPI;
    private HaveADriverLayoutBinding binding;
    private CarAdapter haveDriverAdapter;
    private Activity activity;
    private ArrayList<Car> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khoi tao binding
        binding = HaveADriverLayoutBinding.inflate(getLayoutInflater());
        // Lấy gốc của layout để hiển thị trong Fragment
        View fragment = null;
        fragment = binding.getRoot();
        // Khoi tao firebase
        firebaseAPI = new FirebaseAPI();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Get car from database
        firebaseAPI.fetchCarsByDrivingFavourite(new FirebaseAPI.onCallBack<Car>() {
            @Override
            public void onCallBack(List<Car> list) {
                Log.d("fb", "onCallBack: "+ list.size());
                haveDriverAdapter = new CarAdapter(activity,(ArrayList<Car>) list);
                LinearLayoutManager layoutManagerAdvantage = new LinearLayoutManager(activity);
                binding.listCarWithDriver.setLayoutManager(layoutManagerAdvantage);
                binding.listCarWithDriver.setAdapter(haveDriverAdapter);

            }
        });
    }
}