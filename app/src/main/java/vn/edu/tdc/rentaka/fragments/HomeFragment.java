package vn.edu.tdc.rentaka.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.adapters.PromotionAdapter;
import vn.edu.tdc.rentaka.databinding.HomeFragmentBinding;
import vn.edu.tdc.rentaka.databinding.MainLayoutBinding;
import vn.edu.tdc.rentaka.models.Promotion;

public class HomeFragment extends AbstractFragment {

    private ArrayList<Promotion> listPromotions;
    private HomeFragmentBinding binding;
    private PromotionAdapter promotionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khoi tao binding
        binding = HomeFragmentBinding.inflate(getLayoutInflater());
        View fragment = null;
        // Inflate the layout for this fragment
        //fragment = inflater.inflate(R.layout.home_fragment, container, false);
        fragment = binding.getRoot();

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
        return fragment;
    }
}