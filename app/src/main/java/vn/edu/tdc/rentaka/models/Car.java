package vn.edu.tdc.rentaka.models;

public class Car {
    private String id;
    private String ownerID;
    private String brand;
    private String model;
    private int since;
    private String licensePlate;
    private String color;
    private String description;
    private City city;
    private String fuel;
    private String typeGearbox;
    private int seat;
    private String inspection;
    private String insurrance;
    private String registration;
    private String mortgage;
    private double priceDaily;
    private double salaryDriver;
    private String autoMaker;

    private String statusID;
    private String favourite;

    private String typeDriving;
    private Integer year;
    private double priceSelf;
    private double priceDriver;

    public String getTypeDriving() {
        return typeDriving;
    }

    public void setTypeDriving(String typeDriving) {
        this.typeDriving = typeDriving;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavorite(String favourite) {
        this.favourite = favourite;
    }





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

    public String getTypeGearbox() {
        return typeGearbox;
    }

    public void setTypeGearbox(String typeGearbox) {
        this.typeGearbox = typeGearbox;
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
               int year, String licensePlate, String fuel, String typeGearbox,
               int seat, String color, String inspection, String insurrance,
               String registration, String mortgage, double priceSelf,
               double priceDriver, String description,String favourite) {
        this.id = id;
        this.ownerID = ownerID;
        this.model = model;
        this.autoMaker = autoMaker;
        this.year = year;
        this.licensePlate = licensePlate;
        this.fuel = fuel;
        this.typeGearbox = typeGearbox;
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
            this.typeDriving = "both";
        } else if (priceSelf > 0){
            this.typeDriving = "self";
        } else if (priceSelf > 0){
            this.typeDriving = "driver";
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
                ", type='" + typeGearbox + '\'' +
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
