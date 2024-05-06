package vn.edu.tdc.rentaka.APIs;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tdc.rentaka.models.Car;
import vn.edu.tdc.rentaka.models.Status;

public class FirebaseAPI {

    //Properties
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseReference mdbRef;

    //Methods

    // Callback interface for fetching status
    public interface onCallback<T> {
        void onCallback(List<T> List);
    }

    // Function to add a new car to the Firestore database
    public void addCar(Car car) {
        db.collection("cars")
                .add(car)
                .addOnSuccessListener(documentReference -> {

                    //set document id to car id
                    db.collection("cars")
                            .document(documentReference.getId())
                            .update("id", documentReference.getId());

                    //set status documents
                    addStatus(new Status(car.getStatus()), "cars", documentReference.getId());

                    System.out.println("Added successfully car with ID: " + documentReference.getId());
                }).addOnFailureListener(e -> {
                    System.out.println("Added car failure " + e);
                });


    }

    // Function to add a new status to the Firestore database
    public void addStatus(Status status, String refString, String refID) {
        final DocumentReference docRef = db.collection(refString).document(refID);
        status.setDocRef(docRef);
        db.collection("status")
                .add(status)
                .addOnSuccessListener(documentReference -> {
                    //set document id to status id
                    db.collection("status")
                            .document(documentReference.getId())
                            .update("id", documentReference.getId());
                    System.out.println("DocumentSnapshot added with ID: " + documentReference.getId());
                }).addOnFailureListener(e -> {
                    System.out.println("Error adding document " + e);
                });

    }

    // Function to fetch status by reference from the Firestore database
    public void fetchStatusByRef(String refString, String refID, onCallback<Status> callBack) {
        List<Status> statusList = new ArrayList<>();
        db.collection("status")
                .whereEqualTo("docRef", db.collection(refString).document(refID))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Status status = document.toObject(Status.class);
                            status.setId(document.getId());
                            statusList.add(status);
                        }
                        callBack.onCallback(statusList);
                        Log.d("Success", "fetchStatusByRef: " + statusList.toString());
                    } else {
                        Log.d("Error", "Error getting documents: ", task.getException());
                    }
                });

    }
    // Function to fetch cars by reference from the Firestore database
    public void fetchCars(onCallback<Car> callBack) {
        List<Car> carList = new ArrayList<>();
        db.collection("cars")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Car car = document.toObject(Car.class);
                            car.setId(document.getId());
                            carList.add(car);
                        }
                        callBack.onCallback(carList);
                        Log.d("Success", "fetchCarsByRef: " + carList.toString());
                    } else {
                        Log.d("Error", "Error getting documents: ", task.getException());
                    }
                });

    }

}
