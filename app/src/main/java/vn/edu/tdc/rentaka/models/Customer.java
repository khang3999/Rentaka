package vn.edu.tdc.rentaka.models;

public class Customer {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Location location;

    private String driverLicenseNumber;

    private String role;

    public enum CustomerType{
        owner,
        renter
    }
    public  enum CustomerProperties{
        id,
        firstName,
        lastName,
        email,
        phone,
        driverLicenseNumber,
        role

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }

    public void setDriverLicenseNumber(String driverLicenseNumber) {
        this.driverLicenseNumber = driverLicenseNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Customer() {
    }

    public Customer(String firstName, String lastName, String email, String phone, Location location, String driverLicenseNumber,  CustomerType role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.location = location;
        this.driverLicenseNumber = driverLicenseNumber;
        this.role = role.toString();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", location=" + location +
                ", driverLicenseNumber='" + driverLicenseNumber + '\'' +
                ", Role='" + role + '\'' +
                '}';
    }
}
