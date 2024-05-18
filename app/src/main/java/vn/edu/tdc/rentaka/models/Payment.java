package vn.edu.tdc.rentaka.models;

public class Payment {
    private String id;
    private String reservationID;
    private Double cost;
    private Date paymentDate;
    private String paymentMethod;
    private String statusID;

    public enum PaymentProperties{
        id,
        reservationID,
        cost,
        paymentMethod,
        statusID
    }
    public enum PaymentMethods {
        cash,
        creditCard,
        debitCard,
        paypal,
        momo

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReservationID() {
        return reservationID;
    }

    public void setReservationID(String reservationID) {
        this.reservationID = reservationID;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public Payment() {
    }

    public Payment(String reservationID, Double cost, Date paymentDate, PaymentMethods paymentMethod) {
        this.reservationID = reservationID;
        this.cost = cost;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod.toString();
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id='" + id + '\'' +
                ", reservationID='" + reservationID + '\'' +
                ", cost=" + cost +
                ", paymentDate=" + paymentDate +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", statusID='" + statusID + '\'' +
                '}';
    }
}
