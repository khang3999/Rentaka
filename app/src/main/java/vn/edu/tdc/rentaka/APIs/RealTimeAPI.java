package vn.edu.tdc.rentaka.APIs;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.edu.tdc.rentaka.activities.CreateCarActivity;
import vn.edu.tdc.rentaka.models.Car;
import vn.edu.tdc.rentaka.models.City;
import vn.edu.tdc.rentaka.models.Date;
import vn.edu.tdc.rentaka.models.Discount;
import vn.edu.tdc.rentaka.models.Rate;
import vn.edu.tdc.rentaka.models.Service;
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

    // Method to get all City
    public void fetchCities(FetchListener<City> listener){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cities");
        Log.d("databaseRef", "fetchCities: "+databaseReference);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<City> listCities = new ArrayList<>();
                for (DataSnapshot s : snapshot.getChildren()) {
                    City city = s.getValue(City.class);
                    listCities.add(city);
                    Log.d("databaseRef", "onDataChange: "+city);
                }

                listener.onFetched(listCities);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.toException());
            }
        });
    }

    //Method to add rate to the database ** working properly
    public void addRate(Rate rate){
        DatabaseReference ratesRef = mDatabase.child("rates");
        ratesRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long highestId = -1; // Start with -1 to handle the case where no rates exist

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        highestId = Long.parseLong(snapshot.getKey());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID format in the database: " + snapshot.getKey());
                        return;
                    }

                    long newId = highestId + 1;
                    DatabaseReference newRateRef = ratesRef.child(String.valueOf(newId));
                    Map<String, Object> rateData = new HashMap<>();
                    rateData.put("id", newId);
                    rateData.put("recipientID", rate.getRecipientID());
                    rateData.put("reviewerID", rate.getReviewerID());
                    rateData.put("rating", rate.getRating());
                    rateData.put("comment", rate.getComment());
                    rateData.put("createdAt", rate.getCreatedAt());
                    rateData.put("updatedAt", rate.getUpdatedAt());

                    newRateRef.setValue(rateData, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                System.out.println("Data could not be saved " + databaseError.getMessage());
                            } else {
                                System.out.println("Rate saved successfully.");
                            }
                        }
                    });

                    highestId = newId; // Update highestId for the next rate
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error fetching highest ID: " + databaseError.getMessage());
            }
        });
    }


    //Method to fetch all valid discounts from the database ** working properly
    public void fetchValidDiscount(FetchListener<Discount> listener) {
        DatabaseReference discountsRef = mDatabase.child("discounts");
        discountsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Discount> allDiscounts = new ArrayList<>();
                for (DataSnapshot discountSnapshot : dataSnapshot.getChildren()) {
                    String discountName = discountSnapshot.child("name").getValue(String.class);
                    Double discountPercentage = discountSnapshot.child("percentage").getValue(Double.class);
                    Date validFrom = discountSnapshot.child("validFrom").getValue(Date.class);
                    Date validTo = discountSnapshot.child("validTo").getValue(Date.class);
                    Discount discount = new Discount(discountName, validFrom, validTo, discountPercentage);
                    allDiscounts.add(discount);
                }
                listener.onFetched(allDiscounts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error fetching discounts: " + databaseError.getMessage());
            }
        });
    }

    //Method to add discount to the database ** working properly
    public void addDiscount(Discount discount) {
        DatabaseReference discountsRef = mDatabase.child("discounts");
        discountsRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long highestId = -1; // Start with -1 to handle the case where no services exist

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        highestId = Long.parseLong(snapshot.getKey());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID format in the database: " + snapshot.getKey());
                        return;
                    }


                    long newId = highestId + 1;
                    DatabaseReference newDiscountRef = discountsRef.child(String.valueOf(newId));
                    Map<String, Object> discountData = new HashMap<>();
                    discountData.put("id", newId);
                    discountData.put("name", discount.getDescription());
                    discountData.put("percentage", discount.getPercentage());
                    discountData.put("validFrom", discount.getValidFrom());
                    discountData.put("validTo", discount.getValidTo());

                    newDiscountRef.setValue(discountData, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                System.out.println("Data could not be saved " + databaseError.getMessage());
                            } else {
                                System.out.println("Discount saved successfully.");
                            }
                        }
                    });

                    highestId = newId; // Update highestId for the next discount
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error fetching highest ID: " + databaseError.getMessage());
            }
        });

    }

    //Method to fetch services from the database ** working properly
    public void fetchAllServices(FetchListener<Service> listener) {
        DatabaseReference servicesRef = mDatabase.child("services");
        servicesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Service> allServices = new ArrayList<>();

                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                    String serviceName = serviceSnapshot.child("data").getValue(String.class);
                    Service service = new Service(serviceName);
                    if (serviceName != null) {
                        allServices.add(service);
                    }
                }

                listener.onFetched(allServices);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onError(databaseError.toException());
            }
        });
    }

    //Method to add services to the database ** working properly
    // Add services with incrementing ID
    public void addService(Service... services) {
        DatabaseReference servicesRef = mDatabase.child("services");

        // Fetch the current highest ID
        servicesRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long highestId = -1; // Start with -1 to handle the case where no services exist

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    try {
                        highestId = Long.parseLong(snapshot.getKey());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid ID format in the database: " + snapshot.getKey());
                        return;
                    }
                }

                // Add new services with incrementing IDs
                for (Service s : services) {
                    long newId = highestId + 1;
                    DatabaseReference newServiceRef = servicesRef.child(String.valueOf(newId));
                    Map<String, Object> serviceData = new HashMap<>();
                    serviceData.put("id", newId);
                    serviceData.put("data", s.getName());

                    newServiceRef.setValue(serviceData, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError != null) {
                                System.out.println("Data could not be saved " + databaseError.getMessage());
                            } else {
                                System.out.println("Service saved successfully.");
                            }
                        }
                    });

                    highestId = newId; // Update highestId for the next service
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Error fetching highest ID: " + databaseError.getMessage());
            }
        });
    }

    // Method to fetch all cities ** working properly
//    public void fetchAllCities(FetchListener<City> listener) {
//        DatabaseReference citiesRef = mDatabase.child("cities");
//        citiesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<City> allCities = new ArrayList<>();
//
//                for (DataSnapshot citySnapshot : dataSnapshot.getChildren()) {
//                    City city = citySnapshot.getValue(City.class);
//                    allCities.add(city);
//                }
//
//                listener.onFetched(allCities);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                listener.onError(databaseError.toException());
//            }
//        });
//    }

    //Method to add all cites in vietnam to the database ** working properly
    public void addCitesInVietNam() {
        List<String> cityNames = Arrays.asList(
                "An Giang", "Bà Rịa - Vũng Tàu", "Bạc Liêu", "Bắc Kạn", "Bắc Giang", "Bắc Ninh",
                "Bến Tre", "Bình Định", "Bình Dương", "Bình Phước", "Bình Thuận", "Cà Mau",
                "Cao Bằng", "Cần Thơ", "Đà Nẵng", "Đắk Lắk", "Đắk Nông", "Điện Biên",
                "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang", "Hà Nam", "Hà Nội", "Hà Tĩnh",
                "Hải Dương", "Hải Phòng", "Hậu Giang", "Hòa Bình", "Hưng Yên", "Khánh Hòa",
                "Kiên Giang", "Kon Tum", "Lai Châu", "Lâm Đồng", "Lạng Sơn", "Lào Cai",
                "Long An", "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ",
                "Phú Yên", "Quảng Bình", "Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị",
                "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình", "Thái Nguyên", "Thanh Hóa",
                "Thừa Thiên Huế", "Tiền Giang", "TP Hồ Chí Minh", "Trà Vinh", "Tuyên Quang",
                "Vĩnh Long", "Vĩnh Phúc", "Yên Bái"
        );
        DatabaseReference citiesRef = mDatabase.child("cities");
        for (String cityName : cityNames) {
            DatabaseReference cityRef = citiesRef.push();
            String cityId = cityRef.getKey();
            Map<String, Object> cityData = new HashMap<>();
            cityData.put("id", cityId);
            cityData.put("data", cityName);
            cityRef.setValue(cityData, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println("Data could not be saved " + databaseError.getMessage());
                    } else {
                        System.out.println("City saved successfully.");
                    }
                }
            });
        }

    }


    public void createNewCar(String userId, Car car, Uri imageUriCar, Uri imageUriInspection, Uri imageUriInsurance, Uri imageUriRegister, Context context){

        // Lay node userIdRef
        DatabaseReference userIdRef = FirebaseDatabase.getInstance().getReference().child("cars").child(userId);

        // Tao node carId
        DatabaseReference carIdRef = userIdRef.push();
        // Lay id vua tao
        String carId = carIdRef.getKey();
        // update id cho car
        car.setId(carId);

        // Luu 4 anh : car, inspection, insurance, register nhung chi update string image car
        StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("users/"+userId).child("cars/" + carId);
        fileRef.putFile(imageUriCar)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                    car.setImageCarUrl(imageUrl);
                }))
                .addOnFailureListener(e -> Toast.makeText(context, "Tải ảnh lên thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        fileRef.putFile(imageUriInspection)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                }))
                .addOnFailureListener(e -> Toast.makeText(context, "Tải ảnh lên thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        fileRef.putFile(imageUriInsurance)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                }))
                .addOnFailureListener(e -> Toast.makeText(context, "Tải ảnh lên thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        fileRef.putFile(imageUriRegister)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();
                }))
                .addOnFailureListener(e -> Toast.makeText(context, "Tải ảnh lên thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show());

        // save car into realtime database
        carIdRef.setValue(car).addOnSuccessListener(aVoid -> Toast.makeText(context, "Car data uploaded successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to upload car data", Toast.LENGTH_SHORT).show());
    }


    // Method to fetch all existing statuses ** working properly
//    public void fetchAllStatuses(FetchListener<Status> listener) {
//        DatabaseReference statusesRef = mDatabase.child("statuses");
//        statusesRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<Status> allStatuses = new ArrayList<>();
//
//                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
//                    for (DataSnapshot statusSnapshot : groupSnapshot.getChildren()) {
//                        if (!statusSnapshot.getKey().equals("id") && !statusSnapshot.getKey().equals("data")) {
//                            String id = statusSnapshot.child("id").getValue(String.class);
//                            String name = statusSnapshot.child("data").getValue(String.class);
//                            if (id != null && name != null) {
//                                Status status = new Status(id, Status.StatusName.valueOf(name));
//                                allStatuses.add(status);
//                            }
//                        }
//                    }
//                }
//
//                listener.onFetched(allStatuses);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                listener.onError(databaseError.toException());
//            }
//        });
//    }

    // Method to add a new status to the database** working properly
//    public void addStatus(Status.StatusGroup statusGroup, Status.StatusName statusName) {
//        // Reference to the statuses node
//        DatabaseReference statusesRef = mDatabase.child("statuses");
//        String statusGroupStr = statusGroup.toString();
//
//        // Check if the status group exists
//        statusesRef.orderByChild("data").equalTo(statusGroupStr).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Status group exists, get its reference
//                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                        DatabaseReference statusGroupRef = childSnapshot.getRef();
//                        // Add status to existing status group
//                        addStatusToGroup(statusGroupRef, statusName);
//                    }
//                } else {
//                    // Status group does not exist, create new group and add status
//                    DatabaseReference statusGroupRef = statusesRef.push();
//                    String statusGroupId = statusGroupRef.getKey();
//
//                    // Create a Map to hold the status group data, including the group name
//                    Map<String, Object> statusGroupData = new HashMap<>();
//                    statusGroupData.put("data", statusGroupStr);
//                    statusGroupData.put("id", statusGroupId);
//
//                    // Set the value of the new status group, including the key
//                    statusGroupRef.setValue(statusGroupData, new DatabaseReference.CompletionListener() {
//                        @Override
//                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                            if (databaseError != null) {
//                                System.out.println("Data could not be saved " + databaseError.getMessage());
//                            } else {
//                                System.out.println("Status group saved successfully.");
//
//                                // Add status to the newly created group
//                                addStatusToGroup(statusGroupRef, statusName);
//                            }
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                System.out.println("Error checking for status group: " + databaseError.getMessage());
//            }
//        });
//    }

    // Method to add a status to a status group** working properly
//    private void addStatusToGroup(DatabaseReference statusGroupRef, Status.StatusName statusName) {
//        // Reference to the status name within the status group
//        DatabaseReference statusNameRef = statusGroupRef.push();
//        String statusNameId = statusNameRef.getKey();
//
//        // Create a Map to hold the status name data, including the generated key
//        Map<String, Object> statusNameData = new HashMap<>();
//        statusNameData.put("id", statusNameId);
//        statusNameData.put("data", statusName.toString());
//
//        // Set the value of the new status name, including the key
//        statusNameRef.setValue(statusNameData, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
//                if (databaseError != null) {
//                    System.out.println("Data could not be saved " + databaseError.getMessage());
//                } else {
//                    System.out.println("Status name saved successfully.");
//                }
//            }
//        });
//    }


}
