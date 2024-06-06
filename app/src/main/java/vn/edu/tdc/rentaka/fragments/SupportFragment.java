package vn.edu.tdc.rentaka.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.APIs.RealTimeAPI;
import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.adapters.InformationAdapter;
import vn.edu.tdc.rentaka.adapters.InstructionAdapter;
import vn.edu.tdc.rentaka.databinding.BottomSheetDiaglogLayoutBinding;
import vn.edu.tdc.rentaka.databinding.SupportLayoutBinding;
import vn.edu.tdc.rentaka.models.Information;
import vn.edu.tdc.rentaka.models.Instruction;

public class SupportFragment extends AbstractFragment {
    private SupportLayoutBinding binding;
    private static final int REQUEST_CALL_PERMISSION = 1;
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetDiaglogLayoutBinding bottomSheetDiaglogLayoutBinding;
    private RealTimeAPI realTimeAPI = new RealTimeAPI();
    private Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SupportLayoutBinding.inflate(getLayoutInflater());
        View fragment = binding.getRoot();

        // Get Activity of this fragment
        activity = getActivity();

        // Initialize BottomSheetDialog layout binding
        bottomSheetDiaglogLayoutBinding = BottomSheetDiaglogLayoutBinding.inflate(inflater);

        setupInstructionRecyclerView();
        setupInformationRecyclerView();
        setupButtons();

        return fragment;
    }

    private void setupInstructionRecyclerView() {
        ArrayList<Instruction> instructions = addSampleInstructionData();
        LinearLayoutManager layoutManagerInstruction = new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false);
        attachSnapHelper(binding.instructionRecyclerView);

        // Set adapter for instruction RecyclerView
        InstructionAdapter adapter = new InstructionAdapter(this.getContext(), instructions);

        // Set click listener for instruction RecyclerView
        adapter.setOnItemClickListener(holder -> {
            Log.d("SupportFragment", "onItemClick: Item clicked");
            showBottomSheetDialog(holder.getAdapterPosition());
        });

        binding.instructionRecyclerView.setLayoutManager(layoutManagerInstruction);
        binding.instructionRecyclerView.setAdapter(adapter);
    }

    private void showBottomSheetDialog(int position) {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(activity, R.style.BottomSheetDialogTheme);
        }

        // Remove parent if already attached
        ViewGroup parentView = (ViewGroup) bottomSheetDiaglogLayoutBinding.getRoot().getParent();
        if (parentView != null) {
            parentView.removeView(bottomSheetDiaglogLayoutBinding.getRoot());
        }

        Instruction instruction = addSampleInstructionData().get(position); // Retrieve instruction data
        bottomSheetDiaglogLayoutBinding.title.setText(instruction.getTitle());
        bottomSheetDiaglogLayoutBinding.image.setImageResource(instruction.getContent());
        bottomSheetDialog.setContentView(bottomSheetDiaglogLayoutBinding.getRoot());
        bottomSheetDialog.show();
    }

    private void setupInformationRecyclerView() {
        ArrayList<Information> informations = addSampleInformationData();
        GridLayoutManager layoutManagerInformation = new GridLayoutManager(this.getContext(), 2);

        // Set adapter for information RecyclerView
        InformationAdapter informationAdapter = new InformationAdapter(this.getContext(), informations);
        binding.informationRecyclerView.setLayoutManager(layoutManagerInformation);
        binding.informationRecyclerView.setAdapter(informationAdapter);
    }

    private void setupButtons() {
        binding.callButton.setOnClickListener(view -> makePhoneCall());
        binding.messageButton.setOnClickListener(view -> openChatBox());
    }

    private ArrayList<Instruction> addSampleInstructionData() {
        ArrayList<Instruction> instructions = new ArrayList<>();
        instructions.add(new Instruction("Hướng dẫn đặt xe", R.drawable.instructions_1));
        instructions.add(new Instruction("Chuẩn bị gì trước chuyến đi", R.drawable.instructions_2));
        instructions.add(new Instruction("Hướng dẫn cho chủ xe", R.drawable.instructions_3));
        instructions.add(new Instruction("Đặt xe mọi lúc", R.drawable.instructions_4));
        return instructions;
    }

    private ArrayList<Information> addSampleInformationData() {
        ArrayList<Information> informations = new ArrayList<>();
        informations.add(new Information("Thông tin công ty", R.drawable.company_100));
        informations.add(new Information("Chính sách và quy định", R.drawable.pass_100));
        informations.add(new Information("Đánh giá trên Google Play", R.drawable.playstore_100));
        informations.add(new Information("Fanpage của chúng tôi", R.drawable.facebook_100));
        informations.add(new Information("Hỏi và trả lời", R.drawable.question_100));
        informations.add(new Information("Quy chế hoạt động", R.drawable.clipboard_100));
        informations.add(new Information("Bảo mật thông tin", R.drawable.lock_100));
        informations.add(new Information("Giải quyết tranh chấp", R.drawable.check_100));
        return informations;
    }

    private void makePhoneCall() {
        String phoneNumber = "tel:1234567890"; // Replace with your desired phone number
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
        } else {
            startCall(phoneNumber);
        }
    }

    private void startCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(phoneNumber));
        startActivity(callIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(getContext(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openChatBox() {
        String phoneNumber = "1234567890"; // Replace with your desired phone number
        Uri smsUri = Uri.parse("smsto:" + phoneNumber);
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
        // Optional: Pre-fill the message content
        smsIntent.putExtra("sms_body", "Hello, I need support regarding...");
        startActivity(smsIntent);
    }

    public void attachSnapHelper(RecyclerView recyclerView) {
        // Attach SnapHelper if not already attached
        if (recyclerView.getOnFlingListener() == null) {
            LinearSnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
        }
    }
}
