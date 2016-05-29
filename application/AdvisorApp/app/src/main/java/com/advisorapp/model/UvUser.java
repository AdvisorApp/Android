package com.advisorapp.model;


public class UvUser {

    private long id;

    private User user;

    private Uv uv;

    private double userAverage;

    private double uvMark;

    private String uvComment;

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Uv getUv() {
        return uv;
    }

    public void setUv(Uv uv) {
        this.uv = uv;
    }

    public double getUserAverage() {
        return userAverage;
    }

    public void setUserAverage(double userAverage) {
        this.userAverage = userAverage;
    }

    public double getUvMark() {
        return uvMark;
    }

    public void setUvMark(double uvMark) {
        this.uvMark = uvMark;
    }

    public String getUvComment() {
        return uvComment;
    }

    public void setUvComment(String uvComment) {
        this.uvComment = uvComment;
    }
}
