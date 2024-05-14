package vn.edu.tdc.rentaka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.databinding.CardCarItemBinding;
import vn.edu.tdc.rentaka.models.Car;

public class CarAdapter extends  RecyclerView.Adapter<CarAdapter.MyViewHolder>{

    private Context context;
    private ArrayList<Car> listCar;
    private ItemClickListener itemClickListener;

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(CardCarItemBinding.inflate(layoutInflater,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CardCarItemBinding binding = (CardCarItemBinding) holder.getBinding();
        Car car = listCar.get(position);
    }

    @Override
    public int getItemCount() {
        return listCar.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private ViewBinding binding;
        public MyViewHolder(@NonNull ViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
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
        public void onItemClick(CarAdapter.MyViewHolder holder);
    }
}
