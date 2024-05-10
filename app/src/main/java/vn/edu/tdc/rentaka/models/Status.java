package vn.edu.tdc.rentaka.models;

import com.google.firebase.firestore.DocumentReference;

public class Status {
    private String id;
    private String name;

    public enum StatusName {
        PENDING,
        ACCEPTED,
        REJECTED,
        CANCELLED,
        COMPLETED,
        UNAVAILABLE,
        AVAILABLE,
        CREATED
    }


    public Status() {
    }

    public Status(String id, StatusName statusName) {
        this.id = id;
        this.name = statusName.toString();
    }

    public Status(StatusName statusName) {
        this.name = statusName.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Status{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
