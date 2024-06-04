package vn.edu.tdc.rentaka.models;

public class UserModel {
    private String phone;
    private String name;
    private String email;
    private String gender;
    private String registrationDate;
    private String imageUser;
    private String address;
    private String birthday;
    private String id;



    private Double balance;

    public DrivingLicense getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(DrivingLicense drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public BankCard getBankCard() {
        return bankCard;
    }

    public void setBankCard(BankCard bankCard) {
        this.bankCard = bankCard;
    }

    public CitizenIdentificationCard getCitizenIdCard() {
        return citizenIdCard;
    }

    public void setCitizenIdCard(CitizenIdentificationCard citizenIdCard) {
        this.citizenIdCard = citizenIdCard;
    }

    private DrivingLicense drivingLicense;
    private BankCard bankCard;
    private CitizenIdentificationCard citizenIdCard;

    public UserModel(String id,String phone, String name, String email, String gender, String registrationDate, String imageUser, String address, String birthday, Double balance, DrivingLicense drivingLicense, BankCard bankCard,
                     CitizenIdentificationCard citizenIdCard) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.registrationDate = registrationDate;
        this.imageUser = imageUser;
        this.address = address;
        this.birthday = birthday;
        this.balance = balance;
        this.drivingLicense = drivingLicense;
        this.bankCard = bankCard;
        this.citizenIdCard = citizenIdCard;
    }

    public UserModel(String id,String phone, String name, String email, String registrationDate, DrivingLicense drivingLicense, BankCard bankCard,
                     CitizenIdentificationCard citizenIdCard) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.balance = 0.0;
        this.gender = "None";
        this.registrationDate = registrationDate;
        this.imageUser = "";
        this.address = "";
        this.birthday = "00/00/0000";
        this.drivingLicense = drivingLicense;
        this.bankCard = bankCard;
        this.citizenIdCard = citizenIdCard;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getImageUser() {
        return imageUser;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Double getBalance() {
        return balance;
    }
    public void setBalance(Double balance) {
        this.balance = balance;
    }
}