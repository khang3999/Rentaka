package vn.edu.tdc.rentaka.adapters;

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
import vn.edu.tdc.rentaka.databinding.CardAdvantageItemLayoutBinding;
import vn.edu.tdc.rentaka.models.Advantage;

public class AdvantageAdapter extends RecyclerView.Adapter<AdvantageAdapter.MyViewHolder> {
    private ArrayList<Advantage> listAdvantage;
    private Context context;
    private ItemClickListener itemClickListener;
    // Uy quyen onClick
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public AdvantageAdapter(Context context, ArrayList<Advantage> list){
        this.listAdvantage = list;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(CardAdvantageItemLayoutBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Advantage advantage = listAdvantage.get(position);
        Log.d("position", position+"");
        CardAdvantageItemLayoutBinding binding = (CardAdvantageItemLayoutBinding) holder.getBinding();
        switch (position) {
            case 0:
                binding.advantageImage.setImageResource(R.drawable.advantage_1);
                break;
            case 1:
                binding.advantageImage.setImageResource(R.drawable.advantage_2);
                break;
            case 2:
                binding.advantageImage.setImageResource(R.drawable.advantage_3);
                break;
            case 3:
                binding.advantageImage.setImageResource(R.drawable.advantage_4);
                break;
            default:
                break;
        }
        binding.advantageTitle.setText(advantage.getTitle());
        binding.advantageDescription.setText(advantage.getDescription());
    }

    @Override
    public int getItemCount() {
        return listAdvantage.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ViewBinding binding;

        public MyViewHolder(@NonNull ViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            //Bat su kien chung
            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemClickListener != null){
                        itemClickListener.onItemClick(AdvantageAdapter.MyViewHolder.this);

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
    public interface ItemClickListener {
        public void onItemClick(AdvantageAdapter.MyViewHolder holder);
    }
}
