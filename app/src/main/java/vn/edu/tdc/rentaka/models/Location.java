package vn.edu.tdc.rentaka.models;

public class Location {
    private String id;
    private String city;
    private String address;

    private boolean isPickUp;

    public boolean isPickUp() {
        return isPickUp;
    }

    public void setPickUp(boolean pickUp) {
        isPickUp = pickUp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Location( String city, String address,boolean isPickUp) {
        this.city = city;
        this.address = address;
        this.isPickUp = isPickUp;
    }
    public Location( String city, String address) {
        this.city = city;
        this.address = address;
        this.isPickUp = false;
    }

    @Override
    public String toString() {
        return address + ", " + city;
    }
}
