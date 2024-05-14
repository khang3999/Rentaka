package vn.edu.tdc.rentaka.models;

public class Car {
    private String id;
    private String ownerID;
    private String model;
    private String autoMaker;
    private String color;
    private String licensePlate;
    private String statusID;
    private String description;
    private int year;
    private int seat;

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
    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getStatusID() {
        return statusID;
    }

    public void setStatusID(String statusID) {
        this.statusID = statusID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    public Car() {
    }

    public Car( String ownerID, String model, String autoMaker, String color, String licensePlate, String description, int year, int seat) {
        this.id = id;
        this.ownerID = ownerID;
        this.model = model;
        this.autoMaker = autoMaker;
        this.color = color;
        this.licensePlate = licensePlate;
        this.description = description;
        this.year = year;
        this.seat = seat;
    }


    @Override
    public String toString() {
        return "Car{" +
                "id='" + id + '\'' +
                ", ownerID=" + ownerID +
                ", model='" + model + '\'' +
                ", autoMaker='" + autoMaker + '\'' +
                ", color='" + color + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", statusID='" + statusID + '\'' +
                ", description='" + description + '\'' +
                ", year=" + year +
                ", seat=" + seat +
                '}';
    }

}
