package vn.edu.tdc.rentaka.models;

public class Order {
    private String id;
    private Car car;
    private UserModel customer;
    private UserModel owner;
    private Date dateFrom;
    private  Date dateTo;
    private Date dateCreated;
    private String timePickup;
    private String timeReturn;
    private Status status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public UserModel getCustomer() {
        return customer;
    }

    public void setCustomer(UserModel customer) {
        this.customer = customer;
    }

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getTimePickup() {
        return timePickup;
    }

    public void setTimePickup(String timePickup) {
        this.timePickup = timePickup;
    }

    public String getTimeReturn() {
        return timeReturn;
    }

    public void setTimeReturn(String timeReturn) {
        this.timeReturn = timeReturn;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Order(String id, Car car, UserModel customer, UserModel owner, Date dateFrom, Date dateTo, Date dateCreated, String timePickup, String timeReturn, Status status) {
        this.id = id;
        this.car = car;
        this.customer = customer;
        this.owner = owner;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.dateCreated = dateCreated;
        this.timePickup = timePickup;
        this.timeReturn = timeReturn;
        this.status = status;
    }

    public Order() {

    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", car=" + car +
                ", customer=" + customer +
                ", owner=" + owner +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", dateCreated=" + dateCreated +
                ", timePickup='" + timePickup + '\'' +
                ", timeReturn='" + timeReturn + '\'' +
                ", status=" + status +
                '}';
    }
}
