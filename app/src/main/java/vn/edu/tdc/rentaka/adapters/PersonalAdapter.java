package vn.edu.tdc.rentaka.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.ItemsFunctionProfileBrickBinding;
import vn.edu.tdc.rentaka.databinding.ItemsFunctionProfileNoBrickBinding;
import vn.edu.tdc.rentaka.models.PersonalProfileModel;

import java.util.List;

public class PersonalAdapter extends RecyclerView.Adapter<PersonalAdapter.MyViewHolder> {

    private static final int VIEW_TYPE_BRICK = 0;
    private static final int VIEW_TYPE_NO_BRICK = 1;
    private View.OnClickListener listener; // Listener sự kiện click
    private List<PersonalProfileModel> dataList;
    private Context mContext; // Context để sử dụng cho Toast
    private Activity mActivity; // Activity để sử dụng cho Toast

    public PersonalAdapter(Context context, List<PersonalProfileModel> dataList, Activity activity) {
        this.mContext = context;
        this.dataList = dataList;
        this.mActivity = activity;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_BRICK:
                ItemsFunctionProfileBrickBinding brickBinding = ItemsFunctionProfileBrickBinding.inflate(layoutInflater, parent, false);
                MyViewHolder brickViewHolder = new MyViewHolder(brickBinding);
                brickViewHolder.itemView.setOnClickListener(listener); // Gán listener cho itemView
                return brickViewHolder;
            case VIEW_TYPE_NO_BRICK:
                ItemsFunctionProfileNoBrickBinding noBrickBinding = ItemsFunctionProfileNoBrickBinding.inflate(layoutInflater, parent, false);
                MyViewHolder noBrickViewHolder = new MyViewHolder(noBrickBinding);
                noBrickViewHolder.itemView.setOnClickListener(listener); // Gán listener cho itemView
                return noBrickViewHolder;
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PersonalProfileModel data = dataList.get(position);
        holder.bind(data);
        // Gán sự kiện click cho itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
                showToast(position);
            }
        });
    }

    private void showToast(int pos) {
        Toast.makeText(mContext, "Item được nhấn"+pos, Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == dataList.size() - 1 ? VIEW_TYPE_NO_BRICK : VIEW_TYPE_BRICK;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemsFunctionProfileBrickBinding brickBinding;
        private final ItemsFunctionProfileNoBrickBinding noBrickBinding;
        public MyViewHolder(@NonNull ItemsFunctionProfileBrickBinding brickBinding) {
            super(brickBinding.getRoot());
            this.brickBinding = brickBinding;
            this.noBrickBinding = null;
        }

        public MyViewHolder(@NonNull ItemsFunctionProfileNoBrickBinding noBrickBinding) {
            super(noBrickBinding.getRoot());
            this.noBrickBinding = noBrickBinding;
            this.brickBinding = null;
        }

        public void bind(PersonalProfileModel data) {
            if (brickBinding != null) {
                brickBinding.textItem.setText(data.getContent());
                setItemImage(data.getImage(), brickBinding.imageItem);
            } else if (noBrickBinding != null) {
                noBrickBinding.textItem.setText(data.getContent());
                setItemImage(data.getImage(), noBrickBinding.imageItem);
            }
        }

        private void setItemImage(String image, ImageView imageView) {
            switch (image) {
                case "ic_user":
                    imageView.setImageResource(R.drawable.ic_user);
                    break;
                case "ic_car":
                    imageView.setImageResource(R.drawable.ic_car);
                    break;
                case "ic_heart":
                    imageView.setImageResource(R.drawable.ic_heart);
                    break;
                case "ic_diary":
                    imageView.setImageResource(R.drawable.ic_diary);
                    break;
                case "ic_book":
                    imageView.setImageResource(R.drawable.ic_book);
                    break;
                case "ic_wallet":
                    imageView.setImageResource(R.drawable.ic_wallet);
                    break;
                case "ic_gift":
                    imageView.setImageResource(R.drawable.ic_gift);
                    break;
                case "ic_share":
                    imageView.setImageResource(R.drawable.ic_share);
                    break;
                case "ic_padlock":
                    imageView.setImageResource(R.drawable.ic_padlock);
                    break;
                default:
                    imageView.setImageResource(R.drawable.ic_bin);
                    break;
            }
        }
    }
}