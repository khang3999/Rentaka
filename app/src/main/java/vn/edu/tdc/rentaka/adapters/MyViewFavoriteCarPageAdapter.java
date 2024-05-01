package vn.edu.tdc.rentaka.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import vn.edu.tdc.rentaka.fragments.HaveADriverFragment;
import vn.edu.tdc.rentaka.fragments.SelfDrivingCarFragment;

public class MyViewFavoriteCarPageAdapter extends FragmentStateAdapter {


    public MyViewFavoriteCarPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
//                Log.d("Hieu","0");
                return new SelfDrivingCarFragment();
            case 1:
//                Log.d("Hieu","1");
                return new HaveADriverFragment();
            default:
//                Log.d("Hieu","2");
                return new SelfDrivingCarFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
