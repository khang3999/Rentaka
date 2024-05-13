package vn.edu.tdc.rentaka.adapters;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.databinding.ItemsFunctionAccountBinding;
import vn.edu.tdc.rentaka.models.MyAccountModel;

public class MyAccountAdapter extends RecyclerView.Adapter<MyAccountAdapter.MyViewHolder>{
    private final Activity context;
    private final ArrayList<MyAccountModel> data;
    private OnItemClickListener itemClickListener;
    public MyAccountAdapter(Activity context, ArrayList<MyAccountModel> data) {
        this.context = context;
        this.data = data;
    }
    public interface OnItemClickListener {
        void onClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyAccountAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemsFunctionAccountBinding.inflate(context.getLayoutInflater(),parent,false));
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyAccountModel myAccountModel = data.get(position);
        ((ItemsFunctionAccountBinding)holder.binding).textStart.setText(myAccountModel.getName());
        ((ItemsFunctionAccountBinding)holder.binding).textEnd.setText(myAccountModel.getValue());
//        holder.linearLayout = ((ItemsFunctionAccountBinding) holder.binding).itemList;
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) {
                    itemClickListener.onClickListener(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ViewBinding binding;
        View.OnClickListener onClickListener ;

        public MyViewHolder(@NonNull ViewBinding view) {
            super(view.getRoot());
            // đưa đối tượng view vào binding để dùng lại
            binding = view;
        }

    }
}
