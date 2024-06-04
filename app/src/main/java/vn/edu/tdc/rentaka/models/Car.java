package vn.edu.tdc.rentaka.models;

import java.io.Serializable;

public class Car  {
    private String id;
    private String ownerID;
    private String imageCarUrl;
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
    // So giay dang kiem
    private String inspection;
    // So giay bao hiem xe
    private String insurance;
    // So cavet xe
    private String registration;
    // The chap
    private int mortgage;

    private int priceDaily;
    private int salaryDriver;

    private Status statusId;

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

    public String getImageCarUrl() {
        return imageCarUrl;
    }

    public void setImageCarUrl(String imageCarUrl) {
        this.imageCarUrl = imageCarUrl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getSince() {
        return since;
    }

    public void setSince(int since) {
        this.since = since;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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

    public String getInspection() {
        return inspection;
    }

    public void setInspection(String inspection) {
        this.inspection = inspection;
    }

    public String getinsurance() {
        return insurance;
    }

    public void setinsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public int getMortgage() {
        return mortgage;
    }

    public void setMortgage(int mortgage) {
        this.mortgage = mortgage;
    }

    public int getPriceDaily() {
        return priceDaily;
    }

    public void setPriceDaily(int priceDaily) {
        this.priceDaily = priceDaily;
    }

    public int getSalaryDriver() {
        return salaryDriver;
    }

    public void setSalaryDriver(int salaryDriver) {
        this.salaryDriver = salaryDriver;
    }

    public Status getStatusId() {
        return statusId;
    }

    public void setStatusId(Status statusId) {
        this.statusId = statusId;
    }

    public Car(String id, String ownerID, String imageCarUrl, String brand, String model, int since, String licensePlate, String color, String description, City city, String fuel, String typeGearbox, int seat, String inspection, String insurance, String registration, int mortgage, int priceDaily, int salaryDriver, Status statusId) {
        this.id = id;
        this.ownerID = ownerID;
        this.imageCarUrl = imageCarUrl;
        this.brand = brand;
        this.model = model;
        this.since = since;
        this.licensePlate = licensePlate;
        this.color = color;
        this.description = description;
        this.city = city;
        this.fuel = fuel;
        this.typeGearbox = typeGearbox;
        this.seat = seat;
        this.inspection = inspection;
        this.insurance = insurance;
        this.registration = registration;
        this.mortgage = mortgage;
        this.priceDaily = priceDaily;
        this.salaryDriver = salaryDriver;
        this.statusId = statusId;
    }

    public Car() {
    }

    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", ownerID='" + ownerID + '\'' +
                ", imageCarUrl='" + imageCarUrl + '\'' +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", since=" + since +
                ", licensePlate='" + licensePlate + '\'' +
                ", color='" + color + '\'' +
                ", description='" + description + '\'' +
                ", city=" + city +
                ", fuel='" + fuel + '\'' +
                ", typeGearbox='" + typeGearbox + '\'' +
                ", seat=" + seat +
                ", inspection='" + inspection + '\'' +
                ", insurance='" + insurance + '\'' +
                ", registration='" + registration + '\'' +
                ", mortgage=" + mortgage +
                ", priceDaily=" + priceDaily +
                ", salaryDriver=" + salaryDriver +
                ", statusId=" + statusId +
                '}';
    }
}
