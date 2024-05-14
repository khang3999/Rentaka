package vn.edu.tdc.rentaka.models;


public class Reservation {
    private String id;
    private String carID;
    private String ownerID;
    private String renterID;
    private Date pickUpDate;
    private Date returnDate;
    private Location pickUpLocation;
    private Location returnLocation;
    private Double totalCost;
    private String statusID;


    public enum ReservationProperties{
        id,
        carID,
        ownerID,
        renterID,

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getRenterID() {
        return renterID;
    }

    public void setRenterID(String renterID) {
        this.renterID = renterID;
    }

    public Date getPickUpDate() {
        return pickUpDate;
    }

    public void setPickUpDate(Date pickUpDate) {
        this.pickUpDate = pickUpDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Location getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(Location pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

    public Location getReturnLocation() {
        return returnLocation;
    }

    public void setReturnLocation(Location returnLocation) {
        this.returnLocation = returnLocation;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public Reservation() {
    }

    public Reservation(String carID, String ownerID, String renterID, Date pickUpDate, Date returnDate, Location pickUpLocation, Location returnLocation, Double totalCost) {
        this.carID = carID;
        this.ownerID = ownerID;
        this.renterID = renterID;
        this.pickUpDate = pickUpDate;
        this.returnDate = returnDate;
        this.pickUpLocation = pickUpLocation;
        this.returnLocation = returnLocation;
        this.totalCost = totalCost;

    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", carID='" + carID + '\'' +
                ", ownerID='" + ownerID + '\'' +
                ", renterID='" + renterID + '\'' +
                ", pickUpDate=" + pickUpDate +
                ", returnDate=" + returnDate +
                ", pickUpLocation=" + pickUpLocation +
                ", returnLocation=" + returnLocation +
                ", totalCost=" + totalCost +
                ", statusID='" + statusID + '\'' +
                '}';
    }
}
