package vn.edu.tdc.rentaka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.databinding.RecyclerViewInstructionLayoutBinding;
import vn.edu.tdc.rentaka.databinding.RecyclerViewInstructionReverseLayoutBinding;
import vn.edu.tdc.rentaka.models.Instruction;


public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Instruction> list;


    public InstructionAdapter(Context context, ArrayList<Instruction> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        return new MyViewHolder(RecyclerViewInstructionLayoutBinding.inflate(layoutInflater, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.itemList = ((RecyclerViewInstructionLayoutBinding) holder.binding).itemList;
            RecyclerViewInstructionLayoutBinding binding = (RecyclerViewInstructionLayoutBinding) holder.binding;
            binding.textView2.setText(list.get(position).getTitle());
            binding.imageView.setImageResource(list.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ViewBinding binding;
        CardView itemList;

        public MyViewHolder(@NonNull ViewBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
