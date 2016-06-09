package com.advisorapp.model;


import android.os.Parcel;
import android.os.Parcelable;

public class UvType implements Parcelable{

    private long id;

    private String type;

    private double hoursByCredit;

    public UvType(){

    }

    public long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getHoursByCredit() {
        return hoursByCredit;
    }

    public void setHoursByCredit(double hoursByCredit) {
        this.hoursByCredit = hoursByCredit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.type);
        dest.writeDouble(this.hoursByCredit);
    }

    public static final Parcelable.Creator<UvType> CREATOR
            = new Parcelable.Creator<UvType>() {
        public UvType createFromParcel(Parcel in) {
            return new UvType(in);
        }

        public UvType[] newArray(int size) {
            return new UvType[size];
        }
    };

    private UvType(Parcel in) {
        this.id = in.readLong();
        this.type = in.readString();
        this.hoursByCredit = in.readDouble();
    }
}
