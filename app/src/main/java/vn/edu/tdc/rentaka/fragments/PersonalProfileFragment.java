package vn.edu.tdc.rentaka.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.activities.ChangeThePasswordActivity;
import vn.edu.tdc.rentaka.activities.CreateCarActivity;
import vn.edu.tdc.rentaka.activities.DrivingLicenseActivity;
import vn.edu.tdc.rentaka.activities.FavoriteCarActivity;
import vn.edu.tdc.rentaka.activities.LoginActivity;
import vn.edu.tdc.rentaka.activities.MyAccountActivity;
import vn.edu.tdc.rentaka.activities.UserAddressActivity;
import vn.edu.tdc.rentaka.adapters.PersonalProfileAdapter;
import vn.edu.tdc.rentaka.databinding.PersonalProfileLayoutBinding;
import vn.edu.tdc.rentaka.models.PersonalProfileModel;

import java.util.ArrayList;
import java.util.List;

public class PersonalProfileFragment extends AbstractFragment {

    private PersonalProfileLayoutBinding binding;
    private PersonalProfileAdapter adapter1;
    private PersonalProfileAdapter adapter2;
    private PersonalProfileAdapter adapter3;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = PersonalProfileLayoutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //Cap nhat du lieu
        updateData();

        List<PersonalProfileModel> data1 = new ArrayList<>();
        data1.add(new PersonalProfileModel("ic_user","Tài khoản của tôi"));
        data1.add(new PersonalProfileModel("ic_car","Đăng kí xe cho thuê"));
        data1.add(new PersonalProfileModel("ic_heart","Xe yêu thích"));
        data1.add(new PersonalProfileModel("ic_diary","Địa chỉ của tôi"));
        data1.add(new PersonalProfileModel("ic_book","Giấy phép lái xe"));
        data1.add(new PersonalProfileModel("ic_wallet","Thẻ của tôi"));

        List<PersonalProfileModel> data2 = new ArrayList<>();
        data2.add(new PersonalProfileModel("ic_gift","Quà tặng"));
        data2.add(new PersonalProfileModel("ic_share","Giới thiệu bạn bè"));

        List<PersonalProfileModel> data3 = new ArrayList<>();
        data3.add(new PersonalProfileModel("ic_padlock","Đổi mật khẩu "));
        data3.add(new PersonalProfileModel("","Yêu cầu xóa tài khoản"));

        adapter1 = new PersonalProfileAdapter(getActivity(), data1, 1);
        adapter2 = new PersonalProfileAdapter(getActivity(), data2, 2 );
        adapter3 = new PersonalProfileAdapter(getActivity(), data3, 3);

        binding.ryc1.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.ryc2.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.ryc3.setLayoutManager(new LinearLayoutManager(requireActivity()));

        binding.ryc1.setAdapter(adapter1);
        binding.ryc2.setAdapter(adapter2);
        binding.ryc3.setAdapter(adapter3);

        adapter1.setOnItemClickListener(new PersonalProfileAdapter.OnItemClickListener() {
            @Override
            public void onClickListener(int position) {
                switch (position) {
                    case 0:
                        Intent intent1 = new Intent(requireActivity(), MyAccountActivity.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(requireActivity(), CreateCarActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(requireActivity(), FavoriteCarActivity.class);
                        startActivity(intent3);
                        break;
                    case 3:
                        Intent intent4 = new Intent(requireActivity(), UserAddressActivity.class);
                        startActivity(intent4);
                        break;
                    case 4:
                        Intent intent5 = new Intent(requireActivity(), DrivingLicenseActivity.class);
                        startActivity(intent5);
                        break;
                    default:
                        Toast.makeText(requireActivity(), "Thẻ của tôi", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        adapter2.setOnItemClickListener(new PersonalProfileAdapter.OnItemClickListener() {
            @Override
            public void onClickListener(int position) {
                switch (position) {
                    case 0:
                        Toast.makeText(requireActivity(), "Quà tặng", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(requireActivity(), "Giới thiệu bạn bè", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        adapter3.setOnItemClickListener(new PersonalProfileAdapter.OnItemClickListener() {
            @Override
            public void onClickListener(int position) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(requireActivity(), ChangeThePasswordActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        //Create AlertDialog builder Delete Account
                        AlertDialog builder = new AlertDialog.Builder(requireActivity()).create();
                        builder.setTitle("Xác nhận xóa tài khoản");
                        builder.setMessage("Bạn có chắc chắn muốn xóa tài khoản không?");
                        builder.setButton(AlertDialog.BUTTON_POSITIVE, "Có", (dialog, which) -> {
                            Toast.makeText(requireActivity(), "Xóa tài khoản thành công", Toast.LENGTH_SHORT).show();
//                            Intent intent1 = new Intent(requireActivity(), MainActivity.class);
//                            startActivity(intent1);
                        });
                        builder.setButton(AlertDialog.BUTTON_NEGATIVE, "Không", (dialog, which) -> {
                            builder.dismiss();
                        });
                        builder.show();
                        break;
                }
            }
        });
        //Nut dang xuat
        // Set up the logout button click listener
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog builder
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());  // Adjust context appropriately, `requireActivity()` is typically used in fragments
                builder.setTitle("Đăng Xuất");  // Title of the dialog
                builder.setMessage(getString(R.string.b_n_c_ch_c_l_mu_n_ng_xu_t_kh_ng));  // Message in the dialog

                // Set up the buttons in the dialog
                builder.setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       // Dang xuat
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(getActivity(), "Bạn đã đăng xuất thành công.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked "Hủy" button, dismiss the dialog
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });




        return view;
    }
    //Cap nhat du lieu
    private void updateData() {
        //Loading
        //Gan ten va image ten user tu firebase ve
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
                        binding.textName.setText(name);
                        //Set anh nguoi dung
//                        // Use Glide to load the profile image
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(PersonalProfileFragment.this)
                                    .load(imageUrl)
                                    .into(binding.avatar);
                        } else {
                            //Set anh
                            binding.avatar.setImageResource(R.drawable.avatar);
                        }


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d("Tag",databaseError.getMessage());
                }
            });
        }
        //An loading
    }

}
