package vn.edu.tdc.rentaka.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.adapters.AdvantageAdapter;
import vn.edu.tdc.rentaka.adapters.LocationAdapter;
import vn.edu.tdc.rentaka.adapters.PromotionAdapter;
import vn.edu.tdc.rentaka.databinding.HomeFragmentBinding;
import vn.edu.tdc.rentaka.databinding.MainLayoutBinding;
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
    // Mac dinh search theo xe tu lai type = 0
    private int typeSearch = 0;
    private View preView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khoi tao binding
        binding = HomeFragmentBinding.inflate(getLayoutInflater());
        View fragment = null;
        // Inflate the layout for this fragment
        //fragment = inflater.inflate(R.layout.home_fragment, container, false);
        fragment = binding.getRoot();

        // Set adapter for Promotion
        listPromotions = new ArrayList<Promotion>();
        listPromotions.add(new Promotion(1));
        listPromotions.add(new Promotion(2));
        listPromotions.add(new Promotion(3));
        listPromotions.add(new Promotion(4));
        promotionAdapter = new PromotionAdapter(this.getContext(), listPromotions);
        LinearLayoutManager layoutManagerPromotion = new LinearLayoutManager(this.getContext());
        layoutManagerPromotion.setOrientation(RecyclerView.HORIZONTAL);
        layoutManagerPromotion.setReverseLayout(false);
        binding.listPromotion.setLayoutManager(layoutManagerPromotion);
        binding.listPromotion.setAdapter(promotionAdapter);

        // Set adapter for Location
        listLocations = new ArrayList<Location>();
        listLocations.add(new Location(1, "Ho Chi Minh"));
        listLocations.add(new Location(2, "Ha Noi"));
        listLocations.add(new Location(3, "Da Nang"));
        listLocations.add(new Location(4, "Binh Duong"));
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
                // Chuyển sang activity  chon tinh
            }
        });

        binding.tvTimeResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyen sang activity chon thoi gian
            }
        });

        binding.btnNoDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Doi mau va doi trang thai search

            }
        });

        binding.btnHasDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Doi mau va doi trang thai search

            }
        });
        return fragment;
    }
}