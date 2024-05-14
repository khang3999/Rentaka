package vn.edu.tdc.rentaka.models;

public class UserModel {
    private String phone;
    private String name;
    private String password;
    private String email;
    private String gender;
    private String registrationDate;
    private String imageUser;
    private String address;
    private String birthday;

    public UserModel(String phone, String name, String password, String email, String gender, String registrationDate, String imageUser, String address, String birthday) {
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.registrationDate = registrationDate;
        this.imageUser = imageUser;
        this.address = address;
        this.birthday = birthday;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}