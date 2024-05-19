package vn.edu.tdc.rentaka.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.adapters.MyViewFavoriteCarPageAdapter;
import vn.edu.tdc.rentaka.databinding.FavoriteCarLayoutBinding;

public class FavoriteCarActivity extends AppCompatActivity {
    MyViewFavoriteCarPageAdapter myViewFavoriteCarPageAdapter;
    FavoriteCarLayoutBinding biding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_car_layout);
        biding = FavoriteCarLayoutBinding.inflate(getLayoutInflater());
        setContentView(biding.getRoot());

        //Button top back navigation -- Charcasbeos sua button back ve man hinh truoc -- start
        setSupportActionBar(biding.topAppBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        biding.topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Button top back navigation -- Charcasbeos sua button back ve man hinh truoc -- end

        //Set
        myViewFavoriteCarPageAdapter = new MyViewFavoriteCarPageAdapter(this);
        biding.viewPage.setAdapter(myViewFavoriteCarPageAdapter);

        biding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                biding.viewPage.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        biding.viewPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                biding.tabLayout.getTabAt(position).select();
            }
        });
    }
}