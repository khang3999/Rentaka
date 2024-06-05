package vn.edu.tdc.rentaka.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.CardCarItemBinding;
import vn.edu.tdc.rentaka.databinding.CardItemCityLayoutBinding;
import vn.edu.tdc.rentaka.databinding.PromotionItemLayoutBinding;
import vn.edu.tdc.rentaka.models.Car;
import vn.edu.tdc.rentaka.models.City;
import vn.edu.tdc.rentaka.models.Promotion;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<City> listCites;
    private ItemClickListener itemClickListener;

    public ArrayList<City> getListCites() {
        return listCites;
    }

    public void setListCites(ArrayList<City> listCites) {
        this.listCites = listCites;
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public CityAdapter (Context context, ArrayList<City> list){
        this.context = context;
        this.listCites = list;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new CityAdapter.MyViewHolder(CardItemCityLayoutBinding.inflate(layoutInflater,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CardItemCityLayoutBinding binding = (CardItemCityLayoutBinding) holder.getBinding();
        City city = listCites.get(position);
        binding.cityName.setText(city.getName());

    }

    @Override
    public int getItemCount() {
        return listCites.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ViewBinding binding;
        public MyViewHolder(@NonNull ViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            //Bat su kien chung
            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null){
                        itemClickListener.onItemClick(MyViewHolder.this);
                    }
                    else {
                        Log.d("adapter","You must create an ItemClickListener before!");
                    }
                }
            });

        }

        public ViewBinding getBinding() {
            return binding;
        }

        public void setBinding(ViewBinding binding) {
            this.binding = binding;
        }
    }

    //    Interface
    public interface ItemClickListener{
        public void onItemClick(MyViewHolder holder);
    }
}
