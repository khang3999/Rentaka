package vn.edu.tdc.rentaka.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.activities.ChooseDateActivity;
import vn.edu.tdc.rentaka.activities.ChooseLocationActivity;
import vn.edu.tdc.rentaka.adapters.AdvantageAdapter;
import vn.edu.tdc.rentaka.adapters.LocationAdapter;
import vn.edu.tdc.rentaka.adapters.PromotionAdapter;
import vn.edu.tdc.rentaka.databinding.BottomSheetDiaglogLayoutBinding;
import vn.edu.tdc.rentaka.databinding.HomeFragmentBinding;
import vn.edu.tdc.rentaka.models.Advantage;
import vn.edu.tdc.rentaka.models.Location;
import vn.edu.tdc.rentaka.models.Promotion;

public class HomeFragment extends AbstractFragment {

    private ArrayList<Promotion> listPromotions;
    private ArrayList<Location> listLocations;
    private ArrayList<Advantage> listAdvantage;
    private HomeFragmentBinding binding;
    private PromotionAdapter promotionAdapter;
    private LocationAdapter locationAdapter;
    private AdvantageAdapter advantageAdapter;
    private Activity activity;

    // Mac dinh search theo xe tu lai type = 0
    private int typeSearch = 0;

    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetDiaglogLayoutBinding bottomSheetDiaglogLayoutBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khoi tao binding
        binding = HomeFragmentBinding.inflate(getLayoutInflater());
        bottomSheetDiaglogLayoutBinding = BottomSheetDiaglogLayoutBinding.inflate(getLayoutInflater(),null,false);

        View fragment = null;
        // Inflate the layout for this fragment
        //fragment = inflater.inflate(R.layout.home_fragment, container, false);
        fragment = binding.getRoot();
        // Get Activity of this fragment
        activity = getActivity();
        // Set adapter for Promotion
        listPromotions = new ArrayList<Promotion>();
        listPromotions.add(new Promotion(1));
        listPromotions.add(new Promotion(2));
        listPromotions.add(new Promotion(3));
        listPromotions.add(new Promotion(4));
        promotionAdapter = new PromotionAdapter(this.getContext(), listPromotions);
        promotionAdapter.setOnItemClickListener(new PromotionAdapter.ItemClickListener() {
            @Override
            public void onItemClick(PromotionAdapter.MyViewHolder holder) {

                //Center diaglog
                bottomSheetDialog = new BottomSheetDialog(
                        activity, R.style.BottomSheetDialogTheme
                );
                //Kiem tra ton tai bottomSheetDiaglog hay chua
                if(bottomSheetDialog != null){
                    //// Kiểm tra xem centerSheetDiaglogLayout đã có parent hay không
                    ViewGroup parentView = (ViewGroup) bottomSheetDiaglogLayoutBinding.getRoot().getParent();
                    if (bottomSheetDiaglogLayoutBinding.getRoot().getParent()!=null){
                        //Neu co thi xoa layout duoc gan vao bottomsheetDiaglog di
                        parentView.removeView(bottomSheetDiaglogLayoutBinding.getRoot());
                    }
                    //Gan layout moi vao bottomshetdiaglog
                    Promotion promotion = listPromotions.get(holder.getAdapterPosition());
                    //Khi nao co hinh do vao day
//                    centerSheetDiaglogLayoutBinding.imageDiaglog.setImageResource(promotion.getId());
                    bottomSheetDialog.setContentView(bottomSheetDiaglogLayoutBinding.getRoot());
                    bottomSheetDialog.show();

                }
            }
        });
        //Bat su kien khi click vao x de tat bottom sheet
        bottomSheetDiaglogLayoutBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        LinearLayoutManager layoutManagerPromotion = new LinearLayoutManager(this.getContext());
        layoutManagerPromotion.setOrientation(RecyclerView.HORIZONTAL);
        layoutManagerPromotion.setReverseLayout(false);
        binding.listPromotion.setLayoutManager(layoutManagerPromotion);
        binding.listPromotion.setAdapter(promotionAdapter);

        // Set adapter for Location
        listLocations = new ArrayList<Location>();
//        listLocations.add(new Location(1, "Ho Chi Minh"));
//        listLocations.add(new Location(2, "Ha Noi"));
//        listLocations.add(new Location(3, "Da Nang"));
//        listLocations.add(new Location(4, "Binh Duong"));
        locationAdapter = new LocationAdapter(this.getContext(), listLocations);
        LinearLayoutManager layoutManagerLocation = new LinearLayoutManager(this.getContext());
        layoutManagerLocation.setOrientation(RecyclerView.HORIZONTAL);
        layoutManagerLocation.setReverseLayout(false);
        binding.listLocations.setLayoutManager(layoutManagerLocation);
        binding.listLocations.setAdapter(locationAdapter);

        // Set adapter for Advantage
        listAdvantage = new ArrayList<Advantage>();
        listAdvantage.add(new Advantage(1,"Dòng xe đa dạng","Hơn 100 dòng xe cho bạn tuỳ ý lựa chọn: Mini, Sedan, CUV, SUV, MPV, Bán tải."));
        listAdvantage.add(new Advantage(2,"Giao xe tạn nơi","Bạn có thể lựa chọn giao xe tận nhà/sân bay... Phí tiết kiệm chỉ từ 15k/km."));
        listAdvantage.add(new Advantage(3,"An tâm đặt xe","Không tính phí huỷ chuyến trong vòng 1h sau khi đặt cọc. Hoàn cọc và bồi thường 100% nếu chủ xe huỷ chuyến trong vòng 7 ngày trước chuyến đi."));
        listAdvantage.add(new Advantage(4,"Thủ tục đơn giản","Chỉ cần có CCCD gắn chip (Hoặc Passport) & Giấy phép lái xe là bạn đã đủ điều kiện thuê xe trên Mioto."));
        advantageAdapter = new AdvantageAdapter(this.getContext(), listAdvantage);
        LinearLayoutManager layoutManagerAdvantage = new LinearLayoutManager(this.getContext());
        layoutManagerAdvantage.setOrientation(RecyclerView.HORIZONTAL);
        layoutManagerAdvantage.setReverseLayout(false);
        binding.listAdvantages.setLayoutManager(layoutManagerAdvantage);
        binding.listAdvantages.setAdapter(advantageAdapter);

        // Bat su kien cho search theo dia diem: tinh va thanh pho
        binding.tvLocationResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang activity  chon city
                Intent intent = new Intent(getActivity(), ChooseLocationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        binding.tvTimeResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyen sang activity chon thoi gian
                Intent intent = new Intent(getActivity(), ChooseDateActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        // Doi mau va bat su kien 2 button xe tu lai va xe co tai xe
        binding.btnNoDriver.setActivated(true);

        binding.btnNoDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Doi mau va doi trang thai search
                if (typeSearch != 0){
                    binding.btnNoDriver.setActivated(true);
                    binding.btnHasDriver.setActivated(false);
                    typeSearch = 0;
                }
            }
        });

        binding.btnHasDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Doi mau va doi trang thai search
                if (typeSearch != 1){
                    binding.btnHasDriver.setActivated(true);
                    binding.btnNoDriver.setActivated(false);
                    typeSearch = 1;
                }
            }
        });


        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (activity.getIntent() != null && activity.getIntent().hasExtra("city")){
            Intent intent = getActivity().getIntent();
            binding.tvLocationResult.setText(intent.getStringExtra("city"));
        }
    }
}