package com.advisorapp.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Uv implements Parcelable{

    private long id;

    private String remoteId;

    private String name;

    private String description;

    private int minSemester;

    private boolean availableForCart;

    private double chs;

    private Location location;

    public Uv(String remoteId, String name, String description, int minSemester, boolean availableForCart, double chs, Location location) {
        this.remoteId = remoteId;
        this.name = name;
        this.description = description;
        this.minSemester = minSemester;
        this.availableForCart = availableForCart;
        this.chs = chs;
        this.location = location;
    }

    private UvType uvType;

    private ArrayList<Uv> prerequisitesUv;

    public Uv(){
        this.prerequisitesUv = new ArrayList<Uv>();
    }

    public Location getLocation() {
        return location;
    }

    public long getId() {
        return id;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMinSemester() {
        return minSemester;
    }

    public void setMinSemester(int minSemester) {
        this.minSemester = minSemester;
    }

    public boolean isAvailableForCart() {
        return availableForCart;
    }

    public void setIsAvailableForCard(boolean isAvailableForCard) {
        this.availableForCart = isAvailableForCard;
    }

    public double getChs() {
        return chs;
    }

    public void setChs(double chs) {
        this.chs = chs;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UvType getUvType() {
        return uvType;
    }

    public void setUvType(UvType uvType) {
        this.uvType = uvType;
    }

    public ArrayList<Uv> getPrerequisitesUv() {
        return prerequisitesUv;
    }

    public void setPrerequisitesUv(ArrayList<Uv> prerequisitesUv) {
        this.prerequisitesUv = prerequisitesUv;
    }

    @Override
    public String toString() {
        return "Uv{" +
                "id=" + id +
                ", remoteId='" + remoteId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", availableForCart=" + availableForCart +
                ", chs=" + chs +
                ", location=" + location +
                ", uvType=" + uvType +
                ", prerequisitesUv=" + prerequisitesUv +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.remoteId);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeByte((byte) (this.availableForCart ? 1 : 0));
        dest.writeDouble(this.chs);
        dest.writeString(this.location.name());
        dest.writeParcelable(this.uvType, flags);
        dest.writeTypedList(this.prerequisitesUv);
    }

    public static final Parcelable.Creator<Uv> CREATOR
            = new Parcelable.Creator<Uv>() {
        public Uv createFromParcel(Parcel in) {
            return new Uv(in);
        }

        public Uv[] newArray(int size) {
            return new Uv[size];
        }
    };

    private Uv(Parcel in) {
        this();

        this.id = in.readLong();
        this.remoteId = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.availableForCart = in.readByte() != 0;
        this.chs = in.readDouble();
        this.location = Location.valueOf(in.readString());
        this.uvType = (UvType) in.readParcelable(UvType.class.getClassLoader());
        in.readTypedList(this.prerequisitesUv, Uv.CREATOR);
    }
}
