package vn.edu.tdc.rentaka.models;

import com.google.firebase.firestore.DocumentReference;

public class Status {
    private String id;
    private DocumentReference docRef;
    private String name;

    public Status() {
    }

    public Status(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DocumentReference getDocRef() {
        return docRef;
    }

    public void setDocRef(DocumentReference docRef) {
        this.docRef = docRef;
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
                ", docRef=" + docRef +
                ", name='" + name + '\'' +
                '}';
    }
}
