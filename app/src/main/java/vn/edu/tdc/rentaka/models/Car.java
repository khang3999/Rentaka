package vn.edu.tdc.rentaka.models;

public class Car {
    private String id;
    private String ownerID;
    private String model;
    // Auto maker <=> brand
    private String autoMaker;
    private int year;
    private String licensePlate;
    private String fuel;
    private String type;
    private int seat;
    private String color;
    private String inspection;
    private String insurrance;
    private String registration;
    private String mortgage;
    private double priceSelf;
    private double priceDriver;
    private String description;

    private String statusID;



    public enum CarProperties{
        id,
        model,
        autoMaker,
        color,
        licensePLATE,
        statusID,
        description,
        year,
        seat
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAutoMaker() {
        return autoMaker;
    }

    public void setAutoMaker(String autoMaker) {
        this.autoMaker = autoMaker;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getInspection() {
        return inspection;
    }

    public void setInspection(String inspection) {
        this.inspection = inspection;
    }

    public String getInsurrance() {
        return insurrance;
    }

    public void setInsurrance(String insurrance) {
        this.insurrance = insurrance;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getMortgage() {
        return mortgage;
    }

    public void setMortgage(String mortgage) {
        this.mortgage = mortgage;
    }

    public double getPriceSelf() {
        return priceSelf;
    }

    public void setPriceSelf(double priceSelf) {
        this.priceSelf = priceSelf;
    }

    public double getPriceDriver() {
        return priceDriver;
    }

    public void setPriceDriver(double priceDriver) {
        this.priceDriver = priceDriver;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }
    public Car() {
    }

    public Car(String ownerID, String model, String autoMaker,
               int year, String licensePlate, String fuel, String type,
               int seat, String color, String inspection, String insurrance,
               String registration, String mortgage, double priceSelf,
               double priceDriver, String description) {
        this.id = id;
        this.ownerID = ownerID;
        this.model = model;
        this.autoMaker = autoMaker;
        this.year = year;
        this.licensePlate = licensePlate;
        this.fuel = fuel;
        this.type = type;
        this.seat = seat;
        this.color = color;
        this.inspection = inspection;
        this.insurrance = insurrance;
        this.registration = registration;
        this.mortgage = mortgage;
        this.priceSelf = priceSelf;
        this.priceDriver = priceDriver;
        this.description = description;
        if(priceSelf > 0 && priceDriver > 0){
            this.statusID = "both";
        } else if (priceSelf > 0){
            this.statusID = "self";
        } else if (priceSelf > 0){
            this.statusID = "driver";
        }
    }


    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", ownerID='" + ownerID + '\'' +
                ", model='" + model + '\'' +
                ", autoMaker='" + autoMaker + '\'' +
                ", year=" + year +
                ", licensePlate='" + licensePlate + '\'' +
                ", fuel='" + fuel + '\'' +
                ", type='" + type + '\'' +
                ", seat=" + seat +
                ", color='" + color + '\'' +
                ", inspection='" + inspection + '\'' +
                ", insurrance='" + insurrance + '\'' +
                ", registration='" + registration + '\'' +
                ", mortgage='" + mortgage + '\'' +
                ", priceSelf=" + priceSelf +
                ", priceDriver=" + priceDriver +
                ", description='" + description + '\'' +
                ", statusID='" + statusID + '\'' +
                '}';
    }
}
