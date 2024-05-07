package vn.edu.tdc.rentaka.models;


public class Reservation {
    private String reservationID;
    private String carID;
    private String customerID;
    private Date pickUpDate;
    private Date returnDate;
    private Location pickUpLocation;
    private Location returnLocation;
    private Double totalCost;
    private String status;

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

    public String getReservationID() {
        return reservationID;
    }

    public void setReservationID(String reservationID) {
        this.reservationID = reservationID;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }



    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Reservation() {
    }

    public Reservation(String reservationID, String carID, String customerID, Date pickUpDate, Date returnDate, Location pickUpLocation, Location returnLocation, Double totalCost, String status) {
        this.reservationID = reservationID;
        this.carID = carID;
        this.customerID = customerID;
        this.pickUpDate = pickUpDate;
        this.returnDate = returnDate;
        this.pickUpLocation = pickUpLocation;
        this.returnLocation = returnLocation;
        this.totalCost = totalCost;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationID='" + reservationID + '\'' +
                ", carID='" + carID + '\'' +
                ", customerID='" + customerID + '\'' +
                ", pickUpDate=" + pickUpDate +
                ", returnDate=" + returnDate +
                ", pickUpLocation=" + pickUpLocation +
                ", returnLocation=" + returnLocation +
                ", totalCost=" + totalCost +
                ", status='" + status + '\'' +
                '}';
    }
}
