package vn.edu.tdc.rentaka.models;

public class Identification {
    private String image;
    private String fullName;
    private String dateIssued;
    private String number;

    public Identification(String image, String fullName, String dateIssued, String number) {
        this.image = image;
        this.fullName = fullName;
        this.dateIssued = dateIssued;
        this.number = number;
    }
    public Identification() {
        this.image = "";
        this.fullName = "";
        this.dateIssued = "";
        this.number = "";
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(String dateIssued) {
        this.dateIssued = dateIssued;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
