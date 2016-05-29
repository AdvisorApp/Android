package com.advisorapp.model;


public class UvType {

    private long id;

    private String type;

    private double hoursByCredit;

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
}
