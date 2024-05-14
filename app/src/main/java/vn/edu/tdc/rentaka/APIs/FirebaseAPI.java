package vn.edu.tdc.rentaka.APIs;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;

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

    private int totalTasks;

    //Methods

    // Callback interface for fetching status
    public interface onCallBack<T> {
        void onCallBack(List<T> List);
    }





    //Function fetch reservations by total cost **working properly
    public void fetchReservationsByTotalCost(int totalCost, onCallBack<Reservation> callBack){
        List<Reservation> reservationList = new ArrayList<>();
        db.collection("reservations").whereEqualTo("totalCost", totalCost).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Reservation reservation = document.toObject(Reservation.class);
                    reservation.setId(document.getId());
                    reservationList.add(reservation);
                }
                callBack.onCallBack(reservationList);
                Log.d("Success", "fetchReservationsByTotalCost: " + reservationList.toString());
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });
    }
    //Function fetch reservations by property from the Firestore database ***Working properly
    public void fetchReservationsByProperty(Reservation.ReservationProperties property, String keyword, onCallBack<Reservation> callBack) {
        List<Reservation> reservationList = new ArrayList<>();
        db.collection("reservations").whereEqualTo(property.toString(), keyword).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Reservation reservation = document.toObject(Reservation.class);
                    reservation.setId(document.getId());
                    reservationList.add(reservation);
                }
                callBack.onCallBack(reservationList);
                Log.d("Success", "fetchReservationsByProperty: " + reservationList.toString());
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });
    }

    //Function to fetch reservations by date from the Firestore database ***Working properly
    public void fetchReservationsByDate(Date.DateType dateType, Date date, onCallBack<Reservation> callBack){
        List<Reservation> reservationList = new ArrayList<>();
        db.collection("reservations").
                whereEqualTo(dateType.toString(), date)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Reservation reservation = document.toObject(Reservation.class);
                    reservation.setId(document.getId());
                    reservationList.add(reservation);
                }
                callBack.onCallBack(reservationList);
                Log.d("Success", "fetchReservationsByDate: " + reservationList.toString());
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });
    }

    //function to fetch reservations from the Firestore database **working properly
    public void fetchReservations(onCallBack<Reservation> callBack) {
        List<Reservation> reservationList = new ArrayList<>();
        db.collection("reservations").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Reservation reservation = document.toObject(Reservation.class);
                    reservation.setId(document.getId());
                    reservationList.add(reservation);
                }
                callBack.onCallBack(reservationList);
                Log.d("Success", "fetchReservations: " + reservationList.toString());
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });

    }
    //Function to fetch reservations by location from the Firestore database ***Working properly
    public void fetchReservationsByCity(Location.LocationType locationType,String City, onCallBack<Reservation> callBack) {
        List<Reservation> reservationList = new ArrayList<>();
        if (!locationType.toString().equals(Location.LocationType.customer)) {
            db.collection("reservations").whereEqualTo(locationType.toString()+".city", City).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Reservation reservation = document.toObject(Reservation.class);
                        reservation.setId(document.getId());
                        reservationList.add(reservation);
                    }
                    callBack.onCallBack(reservationList);
                    Log.d("Success", "fetchReservationsByCity: " + reservationList.toString());
                } else {
                    Log.d("Error", "Error getting documents: ", task.getException());
                }
            });
        }else{
            Log.d("Error", "Location type must be either pickUpLocation or returnLocation");
        }

    }

//    function update reservation when owner accepts a renter's request **working properly
    public void updateReservationWhenOwnerAcceptsRenterRequest(Reservation reservation) {
            //update status to accepted when owner accepts a renter's request
            updateStatus(new Status(reservation.getStatusID(),Status.StatusName.accepted));

            db.collection("reservations").document(reservation.getId())
                    .update("statusID", reservation.getStatusID()).addOnSuccessListener(aVoid -> {
                Log.d("Success", "updateReservation: " + reservation.toString());
            }).addOnFailureListener(e -> {
                Log.d("Error", "Error getting documents: ", e);
            });
        }
    //function update reservation missing properties when a renter rents a car *** working properly
    public void updateReservationWhenRenterRentsCar(Reservation reservation) {
        //update status to pending when renter rents a car
        updateStatus(new Status(reservation.getStatusID(),Status.StatusName.pending));

        db.collection("reservations").document(reservation.getId())
                .update("renterID", reservation.getRenterID(),
                        "pickUpDate", reservation.getPickUpDate(),
                        "returnDate", reservation.getReturnDate(),
                        "pickUpLocation", reservation.getPickUpLocation(),
                        "returnLocation", reservation.getReturnLocation()).addOnSuccessListener(aVoid -> {
            Log.d("Success", "updateReservation: " + reservation.toString());
        }).addOnFailureListener(e -> {
            Log.d("Error", "Error getting documents: ", e);
        });
    }

    //Function to add a new reservation to the Firestore database ** working properly
    public void addReservation(Reservation reservation) {

        //add new status for the reservation
        addStatus(new Status(Status.StatusName.created));

        db.collection("reservations").add(reservation).addOnSuccessListener(documentReference -> {
            db.collection("reservations")
                    .document(documentReference.getId())
                    .update("id", documentReference.getId(), "statusID", mRefString);

            reservation.setId(documentReference.getId());

            System.out.println("Added successfully reservation with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            System.out.println("Added reservation failure " + e);
        });


       //update car status to unavailable

        mRefString = "";
    }

    public void fetchCustomerByCity(String city, onCallBack<Customer> callBack) {
        List<Customer> customerList = new ArrayList<>();
        db.collection("customers").whereEqualTo("location.city", city).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Customer customer = document.toObject(Customer.class);
                    customer.setId(document.getId());
                    customerList.add(customer);
                }
                callBack.onCallBack(customerList);
                Log.d("Success", "fetchCustomerByCity: " + customerList.toString());
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });
    }
    //function fetch customers by property from the Firestore database ***Working properly
    public void fetchCustomersByProperty(Customer.CustomerProperties property, String keyword, onCallBack<Customer> callBack) {
        List<Customer> customerList = new ArrayList<>();
        db.collection("customers").whereEqualTo(property.toString(), keyword).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Customer customer = document.toObject(Customer.class);
                    customer.setId(document.getId());
                    customerList.add(customer);
                }
                callBack.onCallBack(customerList);
                Log.d("Success", "fetchCustomersByProperty: " + customerList.toString());
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });
    }

    //Function fetch customers from the Firestore database ***Working properly
    public void fetchCustomers(onCallBack<Customer> callBack) {
        List<Customer> customerList = new ArrayList<>();
        db.collection("customers").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Customer customer = document.toObject(Customer.class);
                    customer.setId(document.getId());
                    customerList.add(customer);
                }
                callBack.onCallBack(customerList);
                Log.d("Success", "fetchCustomers: " + customerList.toString());
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });
    }
    //Function to add a new customer to the Firestore database **working properly
    public void  addCustomer(Customer customer) {
        db.collection("customers").add(customer).addOnSuccessListener(documentReference -> {

            //set document id to customer id
            db.collection("customers").document(documentReference.getId()).update("id", documentReference.getId());

            System.out.println("Added successfully customer with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            System.out.println("Added customer failure " + e);
        });
    }
    //Function remove customer from the Firestore database **working properly
    public void removeCustomer(String customerID) {
        db.collection("customers").document(customerID).delete().addOnSuccessListener(aVoid -> {
            Log.d("Success", "removeCustomer: " + customerID);
        }).addOnFailureListener(e -> {
            Log.d("Error", "Error getting documents: ", e);
        });

    }
    public void updateStatus(Status status){
        db.collection("status").document(status.getId()).update("name", status.getName()).addOnSuccessListener(aVoid -> {
            Log.d("Success", "updateStatus: " + status.toString());
        }).addOnFailureListener(e -> {
            Log.d("Error", "Error getting documents: ", e);
        });
    }
    // Function to add a new status to the Firestore database ***Working properly
    public void addStatus(Status status) {
        db.collection("status").add(status).addOnSuccessListener(documentReference -> {

            //set document id to status id
            db.collection("status").document(documentReference.getId()).update("id", documentReference.getId());
            mRefString = documentReference.getId();
            System.out.println("Added successfully status with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            System.out.println("Added status failure " + e);
        });

    }

    //Function fetch status by car id from the Firestore database ***Working properly
    public void fetchStatusByCarID(String carID, onCallBack<Status> callBack) {
        List<Status> statusList = new ArrayList<>();
        db.collection("cars").document(carID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                String statusID = document.getString("statusID");
                db.collection("status").document(statusID).get().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        DocumentSnapshot document1 = task1.getResult();
                        Status status = document1.toObject(Status.class);
                        status.setId(document1.getId());
                        statusList.add(status);
                        callBack.onCallBack(statusList);
                        Log.d("Success", "fetchStatusesByCarID: " + statusList.toString());
                    } else {
                        Log.d("Error", "Error getting documents: ", task1.getException());
                    }
                });
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });

    }

    // Function to fetch statuses ** working properly
    public void fetchStatuses(onCallBack<Status> callBack) {
        List<Status> statusList = new ArrayList<>();
        db.collection("status").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Status status = document.toObject(Status.class);
                    status.setId(document.getId());
                    statusList.add(status);
                }
                callBack.onCallBack(statusList);
                Log.d("Success", "fetchStatuses: " + statusList.toString());
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });
    }
    //Function remove status from the Firestore database ***Working properly
    public void removeStatus(String statusID) {
        db.collection("status").document(statusID).delete().addOnSuccessListener(aVoid -> {
            Log.d("Success", "removeStatus: " + statusID);
        }).addOnFailureListener(e -> {
            Log.d("Error", "Error getting documents: ", e);
        });
    }
    // Function to fetch cars from the Firestore database ***Working properly
    public void fetchCars(onCallBack<Car> callBack) {
        List<Car> carList = new ArrayList<>();
        db.collection("cars").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Car car = document.toObject(Car.class);
                    car.setId(document.getId());
                    carList.add(car);
                }
                callBack.onCallBack(carList);
                Log.d("Success", "fetchCarsByRef: " + carList.toString());
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });

    }

    //function to remove a car from the Firestore database ***Working properly
    public void removeCar(Car car) {
        //remove status
        removeStatus(car.getStatusID());

       db.collection("cars").document(car.getId()).delete().addOnSuccessListener(aVoid -> {
           Log.d("Success", "removeCar: " + car.toString());
       }).addOnFailureListener(e -> {
           Log.d("Error", "Error getting documents: ", e);
       });
    }

    //Function fetch cars by property from the Firestore database *** Working properly
    public void fetchCarsByProperty(Car.CarProperties property, String keyword, onCallBack<Car> callBack) {
        List<Car> carList = new ArrayList<>();
        keyword = keyword.substring(0,1).toUpperCase() + keyword.substring(1).toLowerCase();
        db.collection("cars").whereEqualTo(property.toString(), keyword).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Car car = document.toObject(Car.class);
                    car.setId(document.getId());
                    carList.add(car);
                }
                callBack.onCallBack(carList);
                Log.d("Success", "fetchCarsByProperty: " + carList.toString());
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });
    }

    //Function fetch cars by status name from the Firestore database ***Working properly
    public void fetchCarsByStatusName(Status.StatusName statusName, onCallBack<Car> callBack){
        db.collection("status").whereEqualTo("name", statusName.toString()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Car> carList = new ArrayList<>();
                List<Task<QuerySnapshot>> tasks = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String statusID = document.getId();
                    Task<QuerySnapshot> fetchTask = db.collection("cars").whereEqualTo("statusID", statusID).get();
                    tasks.add(fetchTask);
                }

                Tasks.whenAllSuccess(tasks).addOnSuccessListener(results -> {
                    for (Object result : results) {
                        QuerySnapshot querySnapshot = (QuerySnapshot) result;
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            Car car = document.toObject(Car.class);
                            car.setId(document.getId());
                            if(!carList.contains(car)) {
                                carList.add(car);
                            }
                        }
                    }
                    callBack.onCallBack(carList);
                    Log.d("Success", "fetchCarsByStatusName: " + carList.toString());
                }).addOnFailureListener(e -> {
                    Log.d("Error", "Error getting documents: ", e);
                });
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });
    }

    // Function to fetch cars by reference from the Firestore database *** Don't call after addCar function has been invoked
    public void fetchCarsByOwnerID(String refID, onCallBack<Car> callBack) {
        List<Car> carList = new ArrayList<>();
        db.collection("cars").whereEqualTo("ownerID", refID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Car car = document.toObject(Car.class);
                    car.setId(document.getId());
                    // Set the statusID of the car
                    String statusID = document.getString("statusID");
                    car.setStatusID(statusID);
                    carList.add(car);
                }
                callBack.onCallBack(carList);
                Log.d("Success", "fetchCarsByRef: " + carList.toString());
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });

    }

    // Function to add a new car to the Firestore database ***Working properly
    public void addCar(Car car) {
        //create a new status for the car with deault status "Unavailable"
        addStatus(new Status(Status.StatusName.unavailable));

        db.collection("cars").add(car).addOnSuccessListener(documentReference -> {

            //set document id to car id
            db.collection("cars")
                    .document(documentReference.getId())
                    .update("id", documentReference.getId(), "statusID", mRefString);

            //archive car id and status id
            car.setId(documentReference.getId());
            car.setStatusID(mRefString);

            System.out.println("Added successfully car with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            System.out.println("Added car failure " + e);
        });
        mRefString = "";

    }

}
