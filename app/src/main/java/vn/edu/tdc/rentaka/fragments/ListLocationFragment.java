package vn.edu.tdc.rentaka.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.adapters.CityAdapter;
import vn.edu.tdc.rentaka.databinding.ListLocationFragmentBinding;
import vn.edu.tdc.rentaka.models.City;

public class ListLocationFragment extends AbstractFragment{
    private ArrayList<City> listCities;

    public ArrayList<City> getListCities() {
        return listCities;
    }

    public void setListCities(ArrayList<City> listCities) {
        this.listCities = listCities;
    }

    private ArrayList<City> copyList = listCities;
    private CityAdapter adapterCities;

    public ArrayList<City> getCopyList() {
        return copyList;
    }

    public void setCopyList(ArrayList<City> copyList) {
        this.copyList = copyList;
    }

    private ListLocationFragmentBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Khoi tao binding
        binding = ListLocationFragmentBinding.inflate(getLayoutInflater());
        View fragment = null;
        fragment = binding.getRoot();
        //Khoi tao mang gia
        listCities = new ArrayList<>();
        listCities.add(new City("hcm"));
        listCities.add(new City("hn"));
        listCities.add(new City("dn"));
        listCities.add(new City("qn"));


        adapterCities = new CityAdapter(this.getContext(), copyList);
        LinearLayoutManager layoutManagerCities = new LinearLayoutManager(this.getContext());
        layoutManagerCities.setOrientation(RecyclerView.VERTICAL);
        layoutManagerCities.setReverseLayout(false);
        binding.listCities.setLayoutManager(layoutManagerCities);
        binding.listCities.setAdapter(adapterCities);

        return fragment;
    }


}
