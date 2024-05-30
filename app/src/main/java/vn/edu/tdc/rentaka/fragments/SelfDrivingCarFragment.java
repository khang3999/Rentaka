package vn.edu.tdc.rentaka.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.rentaka.APIs.FirebaseAPI;
import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.adapters.CarAdapter;
import vn.edu.tdc.rentaka.databinding.BottomSheetDiaglogLayoutBinding;
import vn.edu.tdc.rentaka.databinding.HomeFragmentBinding;
import vn.edu.tdc.rentaka.databinding.SelfDrivingCarLayoutBinding;
import vn.edu.tdc.rentaka.models.Car;

public class SelfDrivingCarFragment extends Fragment {
    private FirebaseAPI firebaseAPI;
    private SelfDrivingCarLayoutBinding binding;
    private CarAdapter carSeflDriverAdapter;
    private Activity activity;
    private ArrayList<Car> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khoi tao binding
        binding = SelfDrivingCarLayoutBinding.inflate(getLayoutInflater());
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
        firebaseAPI.fetchCarsBySelfFavourite(new FirebaseAPI.onCallBack<Car>() {
            @Override
            public void onCallBack(List<Car> list) {
                Log.d("fb", "onCallBack: "+ list.size());
                carSeflDriverAdapter = new CarAdapter(activity,(ArrayList<Car>) list);
                LinearLayoutManager layoutManagerAdvantage = new LinearLayoutManager(activity);
                binding.listCarNoDriver.setLayoutManager(layoutManagerAdvantage);
                binding.listCarNoDriver.setAdapter(carSeflDriverAdapter);

            }
        });
    }
}