package vn.edu.tdc.rentaka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.LocationItemLayoutBinding;
import vn.edu.tdc.rentaka.models.Location;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder>{
    private ArrayList<Location> listLocations;
    private Context context;
    private ItemClickListener itemClickListener;

    // Uy quyen onClick
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    // Constructor
    public LocationAdapter (Context context, ArrayList<Location> list){
        this.context = context;
        this.listLocations = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(LocationItemLayoutBinding.inflate(layoutInflater,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Location location = listLocations.get(position);

        LocationItemLayoutBinding binding = (LocationItemLayoutBinding) holder.getBinding();
        binding.cityName.setText(location.getCity());
        binding.cityImage.setImageResource(R.drawable.ho_chi_minh_v2);
    }

    @Override
    public int getItemCount() {
        return listLocations.size();
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
        public void onItemClick(PromotionAdapter.MyViewHolder holder);
    }
}
