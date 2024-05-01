package vn.edu.tdc.rentaka.models;

public class PersonalProfileModel {
    private String image;
    private String content;

    public PersonalProfileModel(String image, String content) {
        this.image = image;
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
