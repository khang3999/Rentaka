package vn.edu.tdc.rentaka.activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import vn.edu.tdc.rentaka.R;
import vn.edu.tdc.rentaka.databinding.UserAddressLayoutBinding;

public class UserAddressActivity extends AppCompatActivity {
    private UserAddressLayoutBinding binding;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserAddressLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCmSwQf7YNEZmKniG8nJe5uprltK_QlZyE");
        }

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG));

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                }

                @Override
                public void onError(@NonNull Status status) {
                    Log.d("TAG", "An error occurred: " + status);
                    Toast.makeText(UserAddressActivity.this, status+"", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
