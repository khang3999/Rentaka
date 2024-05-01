package vn.edu.tdc.rentaka.activities;

import android.content.ClipData;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.MainLayoutBinding;
import vn.edu.tdc.rentaka.fragments.AbstractFragment;
import vn.edu.tdc.rentaka.fragments.HomeFragment;
import vn.edu.tdc.rentaka.fragments.NewsFragment;

public class MainActivity extends AppCompatActivity {

    //Properties
    private AbstractFragment fragment;
    private  int currentFragment = 0;
    private MainLayoutBinding binding;
    // doi tuong dung de dan fragment vao khung man hinh
    private FragmentTransaction transaction;
    // Set color when click
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.main_layout);

        //Khoi tao binding
        binding = MainLayoutBinding.inflate(getLayoutInflater());
        // Gán view cho binding
        setContentView(binding.getRoot());


        // Khoi tạo lan dau
        fragment = new HomeFragment();
        updateUI();


    }

    private void updateUI(){
        // Set title
        // Neu da ton tai thi tai su dung
        if (getSupportFragmentManager().findFragmentByTag(currentFragment+"") != null){
            fragment = (AbstractFragment) getSupportFragmentManager().findFragmentByTag(currentFragment+"");
        }
        // Neu chua ton tai thi tao moi fragment
        else {
            // Tao doi tuong fragment tuong ung
            if (currentFragment == 0) {
                fragment = new HomeFragment();
            } else if (currentFragment == 1) {
                fragment = new NewsFragment();
            }
            // Them cac fragment
        }

        // CHUAN BI CHO TRANSACTION
        //Lay doi tuong fragment transaction
        transaction = getSupportFragmentManager().beginTransaction();
        // Do du lieu vao doi tuong va dan doi tuong fragment vao khung man hinh,
        // voi tham so dau tien la id cua khung chua fragment o layout )
        transaction.replace(R.id.fragmentContainer,fragment,currentFragment+"");
        // Dua fragment vao trong Stack neu chua ton tai
        if (getSupportFragmentManager().findFragmentByTag(currentFragment+"") == null){
            transaction.addToBackStack(null);
        }
        // Yeu cau thuc hien transaction
        transaction.commit();
    }
}

