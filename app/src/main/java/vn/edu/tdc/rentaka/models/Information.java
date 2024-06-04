package vn.edu.tdc.rentaka.models;

public class Information {
    private String title;
    private int content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }

    public Information(String title, int content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Information{" +
                "title='" + title + '\'' +
                ", content=" + content +
                '}';
    }
}
