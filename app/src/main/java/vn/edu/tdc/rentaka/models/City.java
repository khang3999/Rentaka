package vn.edu.tdc.rentaka.models;

public class City {
    private String name;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public City( int id,String name) {
        this.name = name;
        this.id = id;
    }
    public City( String name) {
        this.name = name;
    }

    public City(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
