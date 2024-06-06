package vn.edu.tdc.rentaka.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.databinding.RecyclerViewInstructionLayoutBinding;
import vn.edu.tdc.rentaka.models.Instruction;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Instruction> list;
    private ItemClickListener itemClickListener;

    public InstructionAdapter(Context context, ArrayList<Instruction> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerViewInstructionLayoutBinding binding = RecyclerViewInstructionLayoutBinding.inflate(layoutInflater, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Instruction instruction = list.get(position);
        RecyclerViewInstructionLayoutBinding binding = (RecyclerViewInstructionLayoutBinding) holder.binding;
        binding.textView2.setText(instruction.getTitle());
        binding.imageView.setImageResource(instruction.getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ViewBinding binding;
        CardView itemList;

        public MyViewHolder(@NonNull ViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.itemList = ((RecyclerViewInstructionLayoutBinding) binding).itemList;

            binding.getRoot().setOnClickListener(view -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(this);
                } else {
                    Log.d("InstructionAdapter", "You must create an ItemClickListener before!");
                }
            });
        }
    }

    public interface ItemClickListener {
        void onItemClick(MyViewHolder holder);
    }
}
