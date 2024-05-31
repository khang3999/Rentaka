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
import vn.edu.tdc.rentaka.databinding.HomeFragmentBinding;
import vn.edu.tdc.rentaka.databinding.PromotionItemLayoutBinding;
import vn.edu.tdc.rentaka.models.Promotion;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.MyViewHolder> {
    private ArrayList<Promotion> listPromotion;
    private Context context;
    private ItemClickListener itemClickListener;

    // Uy quyen onClick
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    // Constructor
    public PromotionAdapter (Context context, ArrayList<Promotion> list){
        this.context = context;
        this.listPromotion = list;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(PromotionItemLayoutBinding.inflate(layoutInflater,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Promotion promotion = listPromotion.get(position);

        PromotionItemLayoutBinding binding = (PromotionItemLayoutBinding) holder.getBinding();
        // Khi lay du lieu tu firebase => setImageBitmap
        // Set tam image

            binding.promotionImage.setImageResource(R.drawable.khuyenmai10);

    }

    @Override
    public int getItemCount() {
        return listPromotion.size();
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
                        itemClickListener.onItemClick(PromotionAdapter.MyViewHolder.this);

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