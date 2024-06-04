package vn.edu.tdc.rentaka.activities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.UserAddressLayoutBinding;

public class UserAddressActivity extends AppCompatActivity {
    // Khai báo các biến toàn cục
    private UserAddressLayoutBinding binding;
    // Đối tượng Geocoder để xử lý geocoding
    private Geocoder geocoder;
    // Adapter để quản lý danh sách địa chỉ
    private ArrayAdapter<String> adapter;
    // Danh sách lưu trữ các địa chỉ
    private List<String> addressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo binding từ layout
        binding = UserAddressLayoutBinding.inflate(getLayoutInflater());
        // Đặt layout cho activity
        setContentView(binding.getRoot());

        //Do data dia chi neu co
        loadDataAdress();

        //Khởi tạo đối tượng Geocoder
        geocoder = new Geocoder(this, Locale.getDefault());

        // Khởi tạo danh sách địa chỉ và adapter
        // Tạo ArrayList mới để lưu trữ các địa chỉ
        addressList = new ArrayList<>();
        // Tạo ArrayAdapter cho danh sách địa chỉ
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addressList);
        // Gán adapter cho ListView
        binding.addressSuggestions.setAdapter(adapter);

        // Thiết lập thanh công cụ với nút quay lại
        setSupportActionBar(binding.topAppBar);
        // Kích hoạt nút home để điều hướng
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Nút end man hinh
        binding.topAppBar.setNavigationOnClickListener(v -> finish());

        //Nut luu dia chi len firebase
        binding.saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy địa chỉ người dùng nhập
                String address = binding.selectedAddress.getText().toString();
                // Hiển thị thông báo nếu địa chỉ rỗng
                if (("Your selected address will appear here").equals(address)){
                    Toast.makeText(UserAddressActivity.this, "Vui lòng chọn địa chỉ", Toast.LENGTH_SHORT).show();
                } else {
                    saveAddressToDatabase(address);
                }
            }
        });

        // Thêm TextWatcher cho EditText tìm kiếm địa chỉ
        binding.searchAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                // Lấy nội dung người dùng nhập
                String key = s.toString();
                // Gọi phương thức lấy địa chỉ nếu chuỗi nhập không rỗng
                if (!key.isEmpty()) {
                    getAddress(key);
                } else {
                    // Xóa danh sách địa chỉ nếu chuỗi nhập rỗng
                    addressList.clear();
                    // Thông báo adapter để cập nhật danh sách
                    adapter.notifyDataSetChanged();
                }
            }
        });

        // Thiết lập sự kiện khi click vào một địa chỉ trong danh sách gợi ý
        binding.addressSuggestions.setOnItemClickListener((parent, view, position, id) -> {
            // Lấy địa chỉ được chọn
            String selectedAddress = addressList.get(position);

            binding.selectedAddress.setText(selectedAddress);
        });
    }
    //Load data dia chi
    private void loadDataAdress() {
        // Hiển thị địa chỉ được chọn lên TextView
        String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userAddressRef = FirebaseDatabase.getInstance().getReference().child("users").child(idUser).child("address");
        userAddressRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Kiểm tra xem dữ liệu tồn tại và không rỗng
                if (snapshot.exists() && snapshot.getValue() != null) {
                    String firebaseAddress = snapshot.getValue(String.class);
                    // Hiển thị địa chỉ từ Firebase lên TextView
                    binding.selectedAddress.setText(firebaseAddress);
                } else {
                    // Nếu không có dữ liệu từ Firebase, hiển thị địa chỉ mặc định
                    binding.selectedAddress.setText("Your selected address will appear here");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching data", error.toException());
            }
        });

    }

    // Phương thức lưu địa chỉ vào Firebase Realtime Database
    private void saveAddressToDatabase(String address) {
        // Lấy ID của người dùng hiện tại
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Tạo một tham chiếu đến vị trí trong cơ sở dữ liệu Firebase Realtime Database
        DatabaseReference userAddressRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("address");
        // Đặt giá trị của địa chỉ vào tham chiếu
        userAddressRef.setValue(address);
        Toast.makeText(this, "Them thanh cong dia chi", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Phương thức lấy địa chỉ dựa trên từ khóa tìm kiếm
    public void getAddress(String key) {
        // Tạo danh sách tạm để lưu trữ các địa chỉ tìm thấy
        ArrayList<String> addressFind = new ArrayList<>();
        try {
            // Lấy danh sách địa chỉ từ Geocoder
            List<Address> addresses = geocoder.getFromLocationName(key, 15);
            for (Address address : addresses) {
                Log.d("Address", address.toString());
                // Sử dụng StringBuilder để xây dựng địa chỉ đầy đ
                StringBuilder fullAddress = new StringBuilder();
//                Log.d("full",fullAddress.toString());
                // Thêm SubThoroughfare (số nhà) nếu có
                if (address.getSubThoroughfare() != null) {
                    fullAddress.append(address.getSubThoroughfare()).append(" ");
                }

                // Thêm Thoroughfare (tên con đường) nếu có
                if (address.getThoroughfare() != null) {
                    fullAddress.append(address.getThoroughfare()).append(", ");
                }
//                // Thêm SubLocality (Xã/Phường) nếu có
//                if (address.getSubLocality() != null) {
//                    fullAddress.append(address.getSubLocality()).append(", ");
//                    }

                // Thêm SubAdminArea (quận huyện con) nếu có
                if (address.getSubAdminArea() != null) {
                    fullAddress.append(address.getSubAdminArea()).append(", ");
                }

//                // Thêm Locality (thành phố) nếu có
                if (address.getLocality() != null) {
                    fullAddress.append(address.getLocality()).append(", ");
                }

                // Thêm AdminArea (tỉnh/thành phố) nếu có
                if (address.getAdminArea() != null) {
                    fullAddress.append(address.getAdminArea()).append(" ");
                }
                // Thêm địa chỉ đầy đủ vào danh sách tạm
                addressFind.add(fullAddress.toString());
            }

            // Cập nhật danh sách địa chỉ trên giao diện người dùng
            // Xóa danh sách cũ
            addressList.clear();
            // Thêm danh sách mới
            addressList.addAll(addressFind);
            // Thông báo adapter để cập nhật danh sách
            adapter.notifyDataSetChanged();

        } catch (IOException e) {
            // Hiển thị thông báo lỗi
            Toast.makeText(UserAddressActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("fail",e.getMessage());
        }
    }
}
