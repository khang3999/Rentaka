package vn.edu.tdc.rentaka.models;

import java.time.LocalDate;

public class Date {
    private int day;
    private int month;
    private int year;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date() {
        // No argument constructor needed for Firestore deserialization
    }

    public Date(LocalDate localDate) {
        this.day = localDate.getDayOfMonth();
        this.month = localDate.getMonthValue();
        this.year = localDate.getYear();
    }

    public LocalDate toLocalDate() {
        return LocalDate.of(year, month, day);
    }
    @Override
    public String toString() {
        return day+"/"+month+"/"+year;
    }

    // Add getters and setters for day, month, and year
}