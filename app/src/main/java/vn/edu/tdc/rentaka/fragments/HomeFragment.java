package vn.edu.tdc.rentaka.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.rentaka.APIs.FirebaseAPI;
import vn.edu.tdc.rentaka.APIs.RealTimeAPI;
import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.activities.ChooseDateActivity;
import vn.edu.tdc.rentaka.activities.ChooseLocationActivity;
import vn.edu.tdc.rentaka.activities.ListCarSearchActivity;
import vn.edu.tdc.rentaka.activities.RentalDetailActivity;
import vn.edu.tdc.rentaka.adapters.AdvantageAdapter;
import vn.edu.tdc.rentaka.adapters.CarAdapter;
import vn.edu.tdc.rentaka.adapters.LocationAdapter;
import vn.edu.tdc.rentaka.adapters.PromotionAdapter;
import vn.edu.tdc.rentaka.databinding.BottomSheetDiaglogLayoutBinding;
import vn.edu.tdc.rentaka.databinding.CardCarItemBinding;
import vn.edu.tdc.rentaka.databinding.HomeFragmentBinding;
import vn.edu.tdc.rentaka.databinding.LocationItemLayoutBinding;
import vn.edu.tdc.rentaka.models.Advantage;
import vn.edu.tdc.rentaka.models.Car;
import vn.edu.tdc.rentaka.models.Location;
import vn.edu.tdc.rentaka.models.Promotion;

public class HomeFragment extends AbstractFragment {
//    private RealTimeAPI realTimeAPI;
    private ArrayList<Promotion> listPromotions;
    private ArrayList<Location> listLocations;
    private ArrayList<Advantage> listAdvantage;
    private ArrayList<Car> listCars;
    private HomeFragmentBinding binding;
    private CardCarItemBinding cardCarItemBinding;
    private PromotionAdapter promotionAdapter;
    private LocationAdapter locationAdapter;
    private AdvantageAdapter advantageAdapter;
    private CarAdapter carAdapter;

    private Activity activity;

    // Mac dinh search theo xe tu lai type = 0
    private int typeSearch = 0;
    // Check da chon dia diem va thoi gian
    private String location="";
    private String date="";
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetDiaglogLayoutBinding bottomSheetDiaglogLayoutBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khoi tao binding
        binding = HomeFragmentBinding.inflate(getLayoutInflater());
        bottomSheetDiaglogLayoutBinding = BottomSheetDiaglogLayoutBinding.inflate(getLayoutInflater(),null,false);
        cardCarItemBinding = CardCarItemBinding.inflate(getLayoutInflater(),null,false);

        View fragment = null;
        fragment = binding.getRoot();
        // Get Activity of this fragment
        activity = getActivity();

        // Khoi tao firebase
//        realTimeAPI = new RealTimeAPI();

        // Set adapter for Promotion
        listPromotions = new ArrayList<Promotion>();
        listPromotions.add(new Promotion(1,"Promotion 1","khuyenmai10.jpg","Chuong trinh khuyen mai so 1"));
        listPromotions.add(new Promotion(2,"Promotion 2","khuyenmai10.jpg","Chuong trinh khuyen mai so 2"));
        listPromotions.add(new Promotion(3,"Promotion 3","khuyenmai10.jpg","Chuong trinh khuyen mai so 3"));
        listPromotions.add(new Promotion(4,"Promotion 4","khuyenmai10.jpg","Chuong trinh khuyen mai so 4"));

        // Khoi tao adapter
        promotionAdapter = new PromotionAdapter(this.getContext(), listPromotions);
        promotionAdapter.setOnItemClickListener(new PromotionAdapter.ItemClickListener() {
            @Override
            public void onItemClick(PromotionAdapter.MyViewHolder holder) {

                Log.d("test", "onItemClick: aaaaa");
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
//                    //Gan layout moi vao bottomshetdiaglog
//                    Promotion promotion = listPromotions.get(holder.getAdapterPosition());
//                    //Khi nao co hinh do vao day con thieu anh
//                    bottomSheetDiaglogLayoutBinding.title.setText(promotion.getTitle());
//                    bottomSheetDiaglogLayoutBinding.description.setText(promotion.getDescription());
//
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
        //Get promotion in firebase

        // Load data Cars from firebase
        String userId = "1111";
        listCars = new ArrayList<>();
        carAdapter = new CarAdapter(activity, listCars);
        LinearLayoutManager layoutManagerListCar = new LinearLayoutManager(activity);
        layoutManagerListCar.setOrientation(RecyclerView.HORIZONTAL);
        layoutManagerListCar.setReverseLayout(false);
        binding.listCar.setLayoutManager(layoutManagerListCar);
        binding.listCar.setAdapter(carAdapter);

        DatabaseReference carsRef = FirebaseDatabase.getInstance().getReference("cars");
        carsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listCars.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
//                    if (userSnapshot.getKey().equals(userId)) {
//                        continue;
//                    }
                    for (DataSnapshot carSnapshot : userSnapshot.getChildren()) {
                        Car car = carSnapshot.getValue(Car.class);
                        listCars.add(car);
                    }
                }
                carAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // Auto stop at center of item
        attachSnapHelper(binding.listPromotion);

        // Set adapter for Location
        listLocations = new ArrayList<Location>();
        listLocations.add(new Location("Tp. Hồ Chí Minh", "",Location.LocationType.pickUpLocation));
        listLocations.add(new Location("Hà Nội", "", Location.LocationType.pickUpLocation));
        listLocations.add(new Location("Đà Nẵng", "", Location.LocationType.pickUpLocation));
        listLocations.add(new Location("Bình Dương", "", Location.LocationType.pickUpLocation));
        locationAdapter = new LocationAdapter(this.getContext(), listLocations);


        locationAdapter.setOnItemClickListener(new LocationAdapter.ItemClickListener() {
            @Override
            public void onItemClick(LocationAdapter.MyViewHolder holder) {
                LocationItemLayoutBinding locationItemLayoutBinding = (LocationItemLayoutBinding) holder.getBinding();
                Intent intent = new Intent(activity, ListCarSearchActivity.class);
                intent.putExtra("location", locationItemLayoutBinding.cityName.getText());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManagerLocation = new LinearLayoutManager(this.getContext());
        layoutManagerLocation.setOrientation(RecyclerView.HORIZONTAL);
        layoutManagerLocation.setReverseLayout(false);
        binding.listLocations.setLayoutManager(layoutManagerLocation);
        binding.listLocations.setAdapter(locationAdapter);
        attachSnapHelper(binding.listLocations);

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
        attachSnapHelper(binding.listAdvantages);

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

        binding.tvDateResult.setOnClickListener(new View.OnClickListener() {
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

        // Set disable for button searching
        binding.btnSearch.setActivated(false);
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location.equals("")){
                    Toast.makeText(activity, "Vui lòng chọn địa điểm", Toast.LENGTH_SHORT).show();
                } else if (date.equals("")){
                    Toast.makeText(activity, "Vui lòng chọn thời gian", Toast.LENGTH_SHORT).show();
                }
                if (v.isActivated()){
                    Intent intent = new Intent(activity, ListCarSearchActivity.class);
                    intent.putExtra("location", location);
                    intent.putExtra("date", date);
                    intent.putExtra("typeSearch", typeSearch);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            }
        });

//        realTimeAPI.fetchAllCarsNotInSelf(userId, new RealTimeAPI.FetchListener<Car>() {
//            @Override
//            public void onFetched(List<Car> data) {
//                listCars.clear();
//                listCars.addAll(data);
//                carAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });


        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Get data Intent from choose location activity
        if (activity.getIntent() != null && activity.getIntent().hasExtra("city")) {
            Intent intent = getActivity().getIntent();
            location = intent.getStringExtra("city");
        }
        if (activity.getIntent() != null && activity.getIntent().hasExtra("date")) {
            Intent intent = getActivity().getIntent();
            date = intent.getStringExtra("date");
        }

        if (!location.equals("")) {
            binding.tvLocationResult.setText(location);
        }
        if (!date.equals("")) {
            binding.tvDateResult.setText(date);
        }
        if (!location.equals("") && !date.equals("")) {
            binding.btnSearch.setActivated(true);
        }




        // Update UI information user at home
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String imageUrl = dataSnapshot.child("imageUser").getValue(String.class);
                        //Set ten nguoi dung
                        binding.username.setText(name);
                        //Set anh nguoi dung
//                        // Use Glide to load the profile image
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(HomeFragment.this)
                                    .load(imageUrl)
                                    .into(binding.userImage);
                        } else {
                            //Set anh mac dinh
                            binding.userImage.setImageResource(R.drawable.avatar);
                        }


                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Tag",databaseError.getMessage());
                }
            });
        }

    }

        // Ham auto slide to center of recycle view item
        public void attachSnapHelper(RecyclerView recyclerView) {
            // Kiểm tra nếu đã gán rồi thì không làm nữa
            if (recyclerView.getOnFlingListener() == null) {
                // Nếu chưa, thì gắn SnapHelper
                LinearSnapHelper snapHelper = new LinearSnapHelper();
                snapHelper.attachToRecyclerView(recyclerView);
            }

        }

}