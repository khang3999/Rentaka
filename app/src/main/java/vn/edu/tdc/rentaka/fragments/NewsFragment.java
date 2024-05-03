package vn.edu.tdc.rentaka.fragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.NewsFragmentBinding;

public class NewsFragment extends AbstractFragment {
    private NewsFragmentBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = NewsFragmentBinding.inflate(getLayoutInflater());
        View fragment = null;
        fragment  = binding.getRoot();

        // Set gach ngang chu
        //binding.test.setPaintFlags(binding.test.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);







        return fragment;
    }
}