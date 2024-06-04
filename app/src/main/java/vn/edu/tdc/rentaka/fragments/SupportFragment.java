package vn.edu.tdc.rentaka.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import vn.edu.tdc.rentaka.databinding.SupportLayoutBinding;

public class SupportFragment extends AbstractFragment{
    private SupportLayoutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SupportLayoutBinding.inflate(getLayoutInflater());
        View fragment = null;
        fragment = binding.getRoot();

        return fragment;
    }


}
