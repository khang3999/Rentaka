package vn.edu.tdc.rentaka.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.NotificationFragmentBinding;

public class NotificationFragment extends AbstractFragment {

    private NotificationFragmentBinding binding;
    private Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = NotificationFragmentBinding.inflate(getLayoutInflater());
        View fragment = null;
        fragment = binding.getRoot();


        return fragment;
    }
}