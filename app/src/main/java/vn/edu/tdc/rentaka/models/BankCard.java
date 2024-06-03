package vn.edu.tdc.rentaka.models;

public class BankCard extends Identification{
    private String bankName;

    public BankCard(String image, String fullName, String dateIssued, String number, String bankName) {
        super(image, fullName, dateIssued, number);
        this.bankName = bankName;
    }

    public BankCard() {
        super();
        this.bankName = "";
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

}
