package vn.edu.tdc.rentaka.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import vn.edu.tdc.rentaka.activities.MainActivity;
import vn.edu.tdc.rentaka.activities.MyAccountActivity;
import vn.edu.tdc.rentaka.adapters.PersonalProfileAdapter;
import vn.edu.tdc.rentaka.databinding.PersonalProfileLayoutBinding;
import vn.edu.tdc.rentaka.models.PersonalProfileModel;

import java.util.ArrayList;
import java.util.List;

public class PersonalProfileFragment extends Fragment {

    private PersonalProfileLayoutBinding binding;
    private PersonalProfileAdapter adapter1;
    private PersonalProfileAdapter adapter2;
    private PersonalProfileAdapter adapter3;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = PersonalProfileLayoutBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

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
                        Intent intent = new Intent(requireActivity(), MyAccountActivity.class);
                        intent.putExtra("name",data1.get(position).getContent());
                        startActivity(intent);
                        break;
                    case 1:
                        Toast.makeText(requireActivity(), "Đăng kí xe cho thuê", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(requireActivity(), "Xe yêu thích", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(requireActivity(), "Địa chỉ của tôi", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(requireActivity(), "Giấy phép lái xe", Toast.LENGTH_SHORT).show();
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
                        Intent intent = new Intent(requireActivity(), MyAccountActivity.class);
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
        return view;
    }

    private void showToast(int pos) {
        Toast.makeText(getActivity(), "Item number : " + pos, Toast.LENGTH_SHORT).show();
    }
}
