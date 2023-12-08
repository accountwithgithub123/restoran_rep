package com.example.restoran;

public class Orders {
    private String name;
    private String email;
    private String date;
    private String time;
    private int numberOfGuests;
    private String request;

    public Orders() {
    }

    // Constructor
    public Orders(String name, String email, String date, String time, int numberOfGuests, String request) {
        this.name = name;
        this.email = email;
        this.date = date;
        this.time = time;
        this.numberOfGuests = numberOfGuests;
        this.request = request;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public String getRequest() {
        return request;
    }

    // Setter methods
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
