package vn.edu.tdc.rentaka.models;

public class Notification {
    private String id;
    private Order order;
    private Status status;
    private String dateCreated;

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Notification(String id, Order order, Status status, String dateCreated) {
        this.id = id;
        this.order = order;
        this.status = status;
        this.dateCreated = dateCreated;
    }
    public Notification() {
        this.order = new Order();
        this.status = new Status();
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", order=" + order +
                ", status=" + status +
                '}';
    }
}
