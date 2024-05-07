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
import vn.edu.tdc.rentaka.models.Reservation;
import vn.edu.tdc.rentaka.models.*;

public class FirebaseAPI {

    //Properties
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mRef;

    private String mRefString = "";


    //Methods

    // Callback interface for fetching status
    public interface onCallback<T> {
        void onCallback(List<T> List);
    }

    //function to fetch reservations from the Firestore database
    public void fetchReservations(onCallback<Reservation> callBack) {
        List<Reservation> reservationList = new ArrayList<>();
        db.collection("reservations").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Reservation reservation = document.toObject(Reservation.class);
                    reservation.setReservationID(document.getId());
                    reservationList.add(reservation);
                }
                callBack.onCallback(reservationList);
                Log.d("Success", "fetchReservations: " + reservationList.toString());
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });

    }


    //Function to fetch reservations by location from the Firestore database
    public void fetchReservationsByCity(String City, onCallback<Reservation> callBack) {
        List<Reservation> reservationList = new ArrayList<>();
        db.collection("locations").whereEqualTo("city", City).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("TAG", "fetchReservationsByCity: "+document.get("docRef").toString());
                    DocumentReference docRef = (DocumentReference) document.get("docRef");
                    docRef.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            DocumentSnapshot document1 = task1.getResult();
                                Reservation reservation = document1.toObject(Reservation.class);
                                reservation.setReservationID(document1.getId());
                                reservationList.add(reservation);

                            callBack.onCallback(reservationList);
                            Log.d("Success", "fetchReservationsByCity: " + reservationList.toString());
                        } else {
                            Log.d("Error", "Error getting documents: ", task1.getException());
                        }
                    });
                }
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });

    }

    //Function to add a new reservation to the Firestore database
    public void addReservation(Reservation reservation) {

        db.collection("reservations").add(reservation).addOnSuccessListener(documentReference -> {

            db.collection("reservations")
                    .document(documentReference.getId())
                    .update("id", documentReference.getId());

            //add document reference
            mRef = documentReference;

            System.out.println("Added successfully reservation with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            System.out.println("Added reservation failure " + e);
        });

        //set location document reference
        addLocation(reservation.getPickUpLocation(), reservation.getReturnLocation());

        mRef = null;
    }

    //Function to add a new location to the Firestore database
    public void addLocation(Location... locations) {
        for (Location location : locations) {
            db.collection("locations").add(location).addOnSuccessListener(documentReference -> {

                //set document id to location id
                db.collection("locations").document(documentReference.getId()).update("id", documentReference.getId(), "docRef", mRef);

                //archive location id to reservation's location id
                if(location.isPickUp()) {
                  mRef.update("pickUpLocation.id", documentReference.getId());
                }else{
                    mRef.update("returnLocation.id", documentReference.getId());
                }
                System.out.println("Added successfully location with ID: " + documentReference.getId());
            }).addOnFailureListener(e -> {
                System.out.println("Added location failure " + e);
            });
        }

    }

    // Function to add a new car to the Firestore database
    public void addCar(Car car) {
        db.collection("cars").add(car).addOnSuccessListener(documentReference -> {

            //set document id to car id
            db.collection("cars").document(documentReference.getId()).update("id", documentReference.getId());

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
        db.collection("status").add(status).addOnSuccessListener(documentReference -> {
            //set document id to status id
            db.collection("status").document(documentReference.getId()).update("id", documentReference.getId());
            System.out.println("DocumentSnapshot added with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            System.out.println("Error adding document " + e);
        });

    }

    // Function to fetch status by reference from the Firestore database
    public void fetchStatusByRef(String refString, String refID, onCallback<Status> callBack) {
        List<Status> statusList = new ArrayList<>();
        db.collection("status").whereEqualTo("docRef", db.collection(refString).document(refID)).get().addOnCompleteListener(task -> {
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
        db.collection("cars").get().addOnCompleteListener(task -> {
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
