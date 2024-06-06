package vn.edu.tdc.rentaka.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
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
import vn.edu.tdc.rentaka.adapters.HistoryAdapter;
import vn.edu.tdc.rentaka.adapters.NotificationAdapter;
import vn.edu.tdc.rentaka.databinding.HistoryFragmentBinding;
import vn.edu.tdc.rentaka.databinding.NotificationFragmentBinding;
import vn.edu.tdc.rentaka.models.Notification;
import vn.edu.tdc.rentaka.models.Order;
import vn.edu.tdc.rentaka.models.UserModel;


public class HistoryFragment extends AbstractFragment {
    private HistoryFragmentBinding binding;
    private Activity activity;
    private ArrayList<Order> listOrders;
    private HistoryAdapter historyAdapter;
    private UserModel userModel;
    private  UserModel customer;
    private  Order order;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = HistoryFragmentBinding.inflate(getLayoutInflater());
        View fragment = null;
        fragment = binding.getRoot();
        activity = getActivity();
        listOrders = new ArrayList<>();
        historyAdapter = new HistoryAdapter(activity, listOrders);
        binding.listHistory.setLayoutManager(new LinearLayoutManager(activity));
        binding.listHistory.setAdapter(historyAdapter);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getUid()!=null){
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                   userModel = new UserModel();
                   userModel  = snapshot.getValue(UserModel.class);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bills");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot carIdSnap: snapshot.getChildren()) {
                                for (DataSnapshot billIdSnap: carIdSnap.getChildren()) {
                                    order = new Order();
                                    order = billIdSnap.getValue(Order.class);
                                    Log.d("userrrrr", "onDataChange: order "+ order);

                                    customer = new UserModel();
                                    customer = order.getCustomer();

                                    if (customer.getId().equals(userModel.getId())){
                                        listOrders.add(order);
                                    }
                                }
                            }
                            historyAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    return fragment;
    }
}