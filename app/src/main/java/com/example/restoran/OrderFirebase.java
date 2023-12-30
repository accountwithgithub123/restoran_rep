package com.example.restoran;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class OrderFirebase implements Parcelable {

    private String fbKey;
    private String name;
    private String email;
    private String date;
    private String time;
    private String numberOfGuests;
    private String request;

    public OrderFirebase() {
    }

    // Constructor

    public OrderFirebase(String fbKey,String name, String email, String date, String time, String numberOfGuests, String request) {
        this.fbKey = fbKey;
        this.name = name;
        this.email = email;
        this.date = date;
        this.time = time;
        this.numberOfGuests = numberOfGuests;
        this.request = request;
    }

    protected OrderFirebase(Parcel in) {

        fbKey = in.readString();
        name = in.readString();
        email = in.readString();
        date = in.readString();
        time = in.readString();
        numberOfGuests = in.readString();
        request = in.readString();
    }

    public static final Creator<OrderFirebase> CREATOR = new Creator<OrderFirebase>() {
        @Override
        public OrderFirebase createFromParcel(Parcel in) {
            return new OrderFirebase(in);
        }

        @Override
        public OrderFirebase[] newArray(int size) {
            return new OrderFirebase[size];
        }
    };

    // Getter methods
    public String getName() {
        return name;
    }

    public String getFbKey() {
        return fbKey;
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

    public void setFbKey(String fbKey) {
        this.fbKey = fbKey;
    }
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
        dest.writeString(fbKey);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(numberOfGuests);
        dest.writeString(request);
    }
}
