package vn.edu.tdc.rentaka.models;

public class UserModel {
    private String phone;
    private String name;
    private String password ;
   private  String email;
   private String gender;

    public UserModel(  String name,String phone, String password, String email) {

        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;

    }
    public UserModel(){

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
}
