package vn.edu.tdc.rentaka.models;

public class DrivingLicense extends Identification{

    public DrivingLicense(String image, String fullName, String dateIssued, String number) {
        super(image, fullName, dateIssued, number);
    }

    public DrivingLicense() {
        super();
    }

}
