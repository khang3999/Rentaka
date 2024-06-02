package vn.edu.tdc.rentaka.models;

public class Discount {
    private String id;
    private String description;
    private Double percentage;
    private Date validFrom;
    private Date validTo;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public Discount() {
    }
    public Discount(String description, Date validFrom, Date validTo, Double percentage) {
        this.description = description;
        this.percentage = percentage;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "name=" + description +
                "id='" + id + '\'' +
                ", percentage=" + percentage +
                ", validFrom=" + validFrom +
                ", validTo=" + validTo +
                '}';
    }
}
