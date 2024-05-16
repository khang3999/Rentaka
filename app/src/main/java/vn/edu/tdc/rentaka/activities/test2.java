package vn.edu.tdc.rentaka.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.fragments.PersonalProfileFragment;

public class test2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

        // Create new fragment
        Fragment fragment = new PersonalProfileFragment();

        // Use FragmentManager for fragment transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.mainTest, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}