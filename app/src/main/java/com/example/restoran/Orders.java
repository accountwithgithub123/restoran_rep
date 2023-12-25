package com.example.restoran;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Orders implements Parcelable {
    private int orderNo;

    private String name;
    private String email;
    private String date;
    private String time;
    private String numberOfGuests;
    private String request;

    public Orders() {
    }

    // Constructor

    public Orders(int orderNo, String name, String email, String date, String time, String numberOfGuests, String request) {
        this.orderNo = orderNo;
        this.name = name;
        this.email = email;
        this.date = date;
        this.time = time;
        this.numberOfGuests = numberOfGuests;
        this.request = request;
    }
    public Orders(String name, String email, String date, String time, String numberOfGuests, String request) {
        this.name = name;
        this.email = email;
        this.date = date;
        this.time = time;
        this.numberOfGuests = numberOfGuests;
        this.request = request;
    }

    protected Orders(Parcel in) {
        orderNo = in.readInt();
        name = in.readString();
        email = in.readString();
        date = in.readString();
        time = in.readString();
        numberOfGuests = in.readString();
        request = in.readString();
    }

    public static final Creator<Orders> CREATOR = new Creator<Orders>() {
        @Override
        public Orders createFromParcel(Parcel in) {
            return new Orders(in);
        }

        @Override
        public Orders[] newArray(int size) {
            return new Orders[size];
        }
    };

    // Getter methods
    public String getName() {
        return name;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
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

    public String getNumberOfGuests() {
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

    public void setNumberOfGuests(String numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(orderNo);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(numberOfGuests);
        dest.writeString(request);
    }
}
