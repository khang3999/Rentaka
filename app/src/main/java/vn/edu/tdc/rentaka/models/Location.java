package vn.edu.tdc.rentaka.models;

import com.google.firebase.firestore.DocumentReference;

public class Location {
    private String city;
    private String address;
    private LocationType type;

    public enum LocationType {
        pickUpLocation,
        returnLocation,
        customer
    }

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Location() {
    }

    public Location( String city, String address,LocationType type) {
        this.city = city;
        this.address = address;
        this.type = type;

    }

    @Override
    public String toString() {
        return address + ", " + city;
    }
}

