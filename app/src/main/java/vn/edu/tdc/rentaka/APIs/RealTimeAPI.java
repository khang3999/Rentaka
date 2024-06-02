package vn.edu.tdc.rentaka.APIs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.edu.tdc.rentaka.models.Status;

public class RealTimeAPI {
    private DatabaseReference mDatabase;

    public RealTimeAPI() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    // Generic interface for fetching data
    public interface FetchListener<T> {
        void onFetched(List<T> data);
        void onError(Exception e);
    }

    // Method to fetch all existing statuses ** working properly
    public void fetchAllStatuses(FetchListener<Status> listener) {
        DatabaseReference statusesRef = mDatabase.child("statuses");
        statusesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Status> allStatuses = new ArrayList<>();

                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot statusSnapshot : groupSnapshot.getChildren()) {
                        if (!statusSnapshot.getKey().equals("id") && !statusSnapshot.getKey().equals("data")) {
                            String id = statusSnapshot.child("id").getValue(String.class);
                            String name = statusSnapshot.child("data").getValue(String.class);
                            if (id != null && name != null) {
                                Status status = new Status(id, Status.StatusName.valueOf(name));
                                allStatuses.add(status);
                            }
                        }
                    }
                }

                listener.onFetched(allStatuses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }

    // Method to add a new status to the database** working properly
    public void addStatus(Status.StatusGroup statusGroup, Status.StatusName statusName) {
        // Reference to the statuses node
        DatabaseReference statusesRef = mDatabase.child("statuses");
        String statusGroupStr = statusGroup.toString();

        // Check if the status group exists
        statusesRef.orderByChild("data").equalTo(statusGroupStr).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Status group exists, get its reference
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        DatabaseReference statusGroupRef = childSnapshot.getRef();
                        // Add status to existing status group
                        addStatusToGroup(statusGroupRef, statusName);
                    }
                } else {
                    // Status group does not exist, create new group and add status
                    DatabaseReference statusGroupRef = statusesRef.push();
                    String statusGroupId = statusGroupRef.getKey();

                    // Create a Map to hold the status group data, including the group name
                    Map<String, Object> statusGroupData = new HashMap<>();
                    statusGroupData.put("data", statusGroupStr);
                    statusGroupData.put("id", statusGroupId);

                    // Set the value of the new status group, including the key
                    statusGroupRef.setValue(statusGroupData, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                System.out.println("Data could not be saved " + databaseError.getMessage());
                            } else {
                                System.out.println("Status group saved successfully.");

                                // Add status to the newly created group
                                addStatusToGroup(statusGroupRef, statusName);
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error checking for status group: " + databaseError.getMessage());
            }
        });
    }

    // Method to add a status to a status group** working properly
    private void addStatusToGroup(DatabaseReference statusGroupRef, Status.StatusName statusName) {
        // Reference to the status name within the status group
        DatabaseReference statusNameRef = statusGroupRef.push();
        String statusNameId = statusNameRef.getKey();

        // Create a Map to hold the status name data, including the generated key
        Map<String, Object> statusNameData = new HashMap<>();
        statusNameData.put("id", statusNameId);
        statusNameData.put("data", statusName.toString());

        // Set the value of the new status name, including the key
        statusNameRef.setValue(statusNameData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                } else {
                    System.out.println("Status name saved successfully.");
                }
            }
        });
    }


}
