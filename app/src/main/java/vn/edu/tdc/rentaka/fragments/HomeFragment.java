package vn.edu.tdc.rentaka.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.adapters.LocationAdapter;
import vn.edu.tdc.rentaka.adapters.PromotionAdapter;
import vn.edu.tdc.rentaka.databinding.HomeFragmentBinding;
import vn.edu.tdc.rentaka.databinding.MainLayoutBinding;
import vn.edu.tdc.rentaka.models.Location;
import vn.edu.tdc.rentaka.models.Promotion;

public class HomeFragment extends AbstractFragment {

    private ArrayList<Promotion> listPromotions;
    private ArrayList<Location> listLocations;
    private HomeFragmentBinding binding;
    private PromotionAdapter promotionAdapter;
    private LocationAdapter locationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khoi tao binding
        binding = HomeFragmentBinding.inflate(getLayoutInflater());
        View fragment = null;
        // Inflate the layout for this fragment
        //fragment = inflater.inflate(R.layout.home_fragment, container, false);
        fragment = binding.getRoot();

        // Set adapter for Promotion
        listPromotions = new ArrayList<Promotion>();
        listPromotions.add(new Promotion(1));
        listPromotions.add(new Promotion(2));
        listPromotions.add(new Promotion(3));
        listPromotions.add(new Promotion(4));
        promotionAdapter = new PromotionAdapter(this.getContext(), listPromotions);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        layoutManager.setReverseLayout(false);
        binding.listPromotion.setLayoutManager(layoutManager);
        binding.listPromotion.setAdapter(promotionAdapter);


        // Set adapter for Location
        listLocations = new ArrayList<Location>();
        listLocations.add(new Location(1, "Ho Chi Minh"));
        listLocations.add(new Location(2, "Ha Noi"));
        listLocations.add(new Location(3, "Da Nang"));
        listLocations.add(new Location(4, "Binh Duong"));
        locationAdapter = new LocationAdapter(this.getContext(), listLocations);
        LinearLayoutManager layoutManagerLocation = new LinearLayoutManager(this.getContext());
        layoutManagerLocation.setOrientation(RecyclerView.HORIZONTAL);
        layoutManagerLocation.setReverseLayout(false);
        binding.listLocations.setLayoutManager(layoutManagerLocation);
        binding.listLocations.setAdapter(locationAdapter);
        return fragment;
    }
}