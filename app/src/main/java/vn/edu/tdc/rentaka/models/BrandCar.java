package vn.edu.tdc.rentaka.models;

public class BrandCar {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public BrandCar(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return "BrandCar{" +
                "name='" + name + '\'' +
                '}';
    }

}
