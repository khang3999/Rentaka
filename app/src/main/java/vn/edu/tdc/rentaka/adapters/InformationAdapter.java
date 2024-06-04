package vn.edu.tdc.rentaka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.databinding.RecyclerViewInformationLayoutBinding;
import vn.edu.tdc.rentaka.databinding.RecyclerViewInstructionLayoutBinding;
import vn.edu.tdc.rentaka.models.Information;
import vn.edu.tdc.rentaka.models.Instruction;

public class InformationAdapter extends RecyclerView.Adapter<InformationAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Information> list;


    public InformationAdapter(Context context, ArrayList<Information> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InformationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MyViewHolder(RecyclerViewInformationLayoutBinding.inflate(layoutInflater, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InformationAdapter.MyViewHolder holder, int position) {
        holder.itemList = ((RecyclerViewInformationLayoutBinding) holder.binding).itemList;
        RecyclerViewInformationLayoutBinding binding = (RecyclerViewInformationLayoutBinding) holder.binding;
        binding.titleInfomation.setText(list.get(position).getTitle());
        binding.iconInfomation.setImageResource(list.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ViewBinding binding;
        LinearLayout itemList;

        public MyViewHolder(@NonNull ViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}