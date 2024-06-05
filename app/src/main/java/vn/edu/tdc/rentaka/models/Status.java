package vn.edu.tdc.rentaka.models;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Status {
    private int id;
    private String description;
    private String title;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public  Status(){

        this.id = -1;
        this.title = "unknown";
        this.description = "unknown";

    };

    public Status(int id, String title, String description) {
        this.id = id;
        this.description = description;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
