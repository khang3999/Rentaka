package vn.edu.tdc.rentaka.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.checkerframework.checker.units.qual.A;

import vn.edu.tdc.rentaka.R;

public class PickLocationCurrentFragment extends AbstractFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.pick_location_current_fragment, container, false);
    }
}
