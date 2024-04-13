package vn.edu.tdc.rentaka.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import vn.edu.tdc.rentaka.adapters.PersonalAdapter;
import vn.edu.tdc.rentaka.databinding.PersonalProfileLayoutBinding;
import vn.edu.tdc.rentaka.models.PersonalProfileModel;

import java.util.ArrayList;
import java.util.List;

public class PersonalFragment extends Fragment {

    private PersonalProfileLayoutBinding binding;
    private PersonalAdapter adapter1;
    private PersonalAdapter adapter2;
    private PersonalAdapter adapter3;

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
/*
        adapter1 = new PersonalAdapter(data1);
        adapter2 = new PersonalAdapter(data2);
        adapter3 = new PersonalAdapter(data3);
*/
        adapter1 = new PersonalAdapter(getContext(), data1, getActivity());
        adapter2 = new PersonalAdapter(getContext(), data2, getActivity());
        adapter3 = new PersonalAdapter(getContext(), data3, getActivity());
        binding.ryc1.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.ryc2.setLayoutManager(new LinearLayoutManager(requireActivity()));
        binding.ryc3.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.ryc1.setAdapter(adapter1);
        binding.ryc2.setAdapter(adapter2);
        binding.ryc3.setAdapter(adapter3);

        return view;
    }
}
