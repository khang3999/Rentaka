package vn.edu.tdc.rentaka.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import vn.edu.tdc.rentaka.R;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.confirm_rental_layout);

        mDatabase = FirebaseDatabase.getInstance("https://rentaka-android-app-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
//        writeNewUser("1", "Alice", "Alice@email.com");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
//                ArrayList<User> users = new ArrayList<>();
//                for (DataSnapshot userSnapshot : dataSnapshot.child("users").getChildren()) {
//                    User user = userSnapshot.getValue(User.class);
//                    users.add(user);
//                }
//                Log.d("Test", "onDataChange: "+users.toString());
                User user = dataSnapshot.child("users").child("1").getValue(User.class);
                Log.d("Test", "onDataChange: "+user.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.d("Test", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.addValueEventListener(postListener);
    }
    public void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).child("username").setValue(name);
    }
}