package com.advisorapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Set;

public class StudyPlan implements Parcelable{

    private long id;

    private User user;

    private String name;

    public StudyPlan(){

    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "StudyPlan{" +
                "id=" + id +
                ", user=" + user +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.name);
    }

    public static final Parcelable.Creator<StudyPlan> CREATOR
            = new Parcelable.Creator<StudyPlan>() {
        public StudyPlan createFromParcel(Parcel in) {
            return new StudyPlan(in);
        }

        public StudyPlan[] newArray(int size) {
            return new StudyPlan[size];
        }
    };

    private StudyPlan(Parcel in) {
        this.id = in.readLong();
        this.user = (User) in.readParcelable(User.class.getClassLoader());
        this.name = in.readString();
    }
}
