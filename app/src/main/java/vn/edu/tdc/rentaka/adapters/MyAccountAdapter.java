package vn.edu.tdc.rentaka.adapters;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.databinding.ItemsFunctionAccountBinding;
import vn.edu.tdc.rentaka.databinding.MyAccountLayoutBinding;
import vn.edu.tdc.rentaka.databinding.PersonalProfileLayoutBinding;
import vn.edu.tdc.rentaka.models.MyAccountModel;

public class MyAccountAdapter extends RecyclerView.Adapter<MyAccountAdapter.MyViewHolder>{
    private final Activity context;
    private final ArrayList<MyAccountModel> data;
    private ItemClickListener itemClickListener;
    public MyAccountAdapter(Activity context, ArrayList<MyAccountModel> data) {
        this.context = context;
        this.data = data;
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    
    @NonNull
    @Override
    public MyAccountAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemsFunctionAccountBinding.inflate(context.getLayoutInflater(),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyAccountModel myAccountModel = data.get(position);
        ((ItemsFunctionAccountBinding)holder.binding).textStart.setText(myAccountModel.getName());
        ((ItemsFunctionAccountBinding)holder.binding).textEnd.setText(myAccountModel.getValue());
//        holder.linearLayout = ((ItemsFunctionAccountBinding) holder.binding).itemList;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public interface ItemClickListener {
        void onItemClick(MyViewHolder holder);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ViewBinding binding;
        private int postion;
        private LinearLayout linearLayout;

        public MyViewHolder(@NonNull ViewBinding view) {
            super(view.getRoot());
            binding = view;
           view.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        public ViewBinding getBinding() {
            return binding;
        }

        public int getPostion() {
            return postion;
        }

        public LinearLayout getLinearLayout() {
            return linearLayout;
        }
    }
}
