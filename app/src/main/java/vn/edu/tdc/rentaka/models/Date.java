package vn.edu.tdc.rentaka.models;

import java.time.LocalDate;
import java.util.Objects;

public class Date {
    private int day;
    private int month;
    private int year;

    public enum DateType{
       returnDate,
        pickUpDate
    }

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
        return String.format("%02d/%02d/%d", day, month, year);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Date date = (Date) o;
        return day == date.day && month == date.month && year == date.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, month, year);
    }

    //Function compare to date
    public Integer compareTo(Date date){
        if(this.year > date.year){
            return 1;
        }else if(this.year < date.year){
            return -1;
        }else{
            if(this.month > date.month){
                return 1;
            }else if(this.month < date.month){
                return -1;
            }else{
                if(this.day > date.day){
                    return 1;
                }else if(this.day < date.day){
                    return -1;
                }else{
                    return 0;
                }
            }
        }
    }

}