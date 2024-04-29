package vn.edu.tdc.rentaka.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.ItemsFunctionProfileBrickLayoutBinding;
import vn.edu.tdc.rentaka.databinding.ItemsFunctionProfileNoBrickLayoutBinding;
import vn.edu.tdc.rentaka.models.PersonalProfileModel;

import java.util.List;

public class PersonalProfileAdapter extends RecyclerView.Adapter<PersonalProfileAdapter.MyViewHolder> {

    private static final int VIEW_TYPE_BRICK = 0;
    private static final int VIEW_TYPE_NO_BRICK = 1;
    private OnItemClickListener itemClickListener; // Listener sự kiện click
    private final List<PersonalProfileModel> dataList;
    private final int idList;
    private final Context mContext; // Context để sử dụng cho Toast

    public interface OnItemClickListener {
        void onClickListener(int position);
    }

    public PersonalProfileAdapter(Context context, List<PersonalProfileModel> dataList, int idList) {
        this.mContext = context;
        this.dataList = dataList;
        this.idList = idList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_BRICK:
                ItemsFunctionProfileBrickLayoutBinding brickBinding = ItemsFunctionProfileBrickLayoutBinding.inflate(layoutInflater, parent, false);
                return new MyViewHolder(brickBinding);
            case VIEW_TYPE_NO_BRICK:
                ItemsFunctionProfileNoBrickLayoutBinding noBrickBinding = ItemsFunctionProfileNoBrickLayoutBinding.inflate(layoutInflater, parent, false);
                return new MyViewHolder(noBrickBinding);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PersonalProfileModel data = dataList.get(position);
        holder.bind(data);
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
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position == dataList.size() - 1 ? VIEW_TYPE_NO_BRICK : VIEW_TYPE_BRICK;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ItemsFunctionProfileBrickLayoutBinding brickBinding;
        private final ItemsFunctionProfileNoBrickLayoutBinding noBrickBinding;
        View.OnClickListener onClickListener ;

        public MyViewHolder(@NonNull ItemsFunctionProfileBrickLayoutBinding brickBinding) {
            super(brickBinding.getRoot());
            this.brickBinding = brickBinding;
            this.noBrickBinding = null;
        }

        public MyViewHolder(@NonNull ItemsFunctionProfileNoBrickLayoutBinding noBrickBinding) {
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
