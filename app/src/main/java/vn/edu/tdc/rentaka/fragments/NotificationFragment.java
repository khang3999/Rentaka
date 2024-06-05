package vn.edu.tdc.rentaka.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.adapters.MyCarAdapter;
import vn.edu.tdc.rentaka.adapters.NotificationAdapter;
import vn.edu.tdc.rentaka.databinding.NotificationFragmentBinding;
import vn.edu.tdc.rentaka.models.Notification;

public class NotificationFragment extends AbstractFragment {

    private NotificationFragmentBinding binding;
    private Activity activity;
    private ArrayList<Notification> listNotification;
    private NotificationAdapter notificationAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = NotificationFragmentBinding.inflate(getLayoutInflater());
        View fragment = null;
        fragment = binding.getRoot();
        activity = getActivity();
        listNotification = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(activity, listNotification);
        binding.listNoti.setLayoutManager(new LinearLayoutManager(activity));
        binding.listNoti.setAdapter(notificationAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notifications");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getUid()!=null){

         DatabaseReference notificationRef = databaseReference.child(user.getUid());
            notificationRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listNotification.clear();
                    for (DataSnapshot datasnapshot: snapshot.getChildren()) {
                        Notification notification = new Notification();
                        notification = datasnapshot.getValue(Notification.class);
                        listNotification.add(notification);
                    }
                    notificationAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return fragment;
    }
}