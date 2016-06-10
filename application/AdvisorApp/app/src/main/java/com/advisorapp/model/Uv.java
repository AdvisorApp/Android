package com.advisorapp.model;


import java.util.Set;

public class Uv {

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

    private Set<Semester> semesters;

    private Set<Uv> corequisitesUv;

    private Set<Uv> corequisitesUvOf;

    private Set<Uv> prerequisitesUv;

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

    public Set<Semester> getSemesters() {
        return semesters;
    }

    public void setSemesters(Set<Semester> semesters) {
        this.semesters = semesters;
    }

    public void addSemester(Semester semester) {
        this.semesters.add(semester);
    }

    public Set<Uv> getCorequisitesUv() {
        return corequisitesUv;
    }

    public void setCorequisitesUv(Set<Uv> corequisitesUv) {
        this.corequisitesUv = corequisitesUv;
    }

    public Set<Uv> getCorequisitesUvOf() {
        return corequisitesUvOf;
    }

    public void setCorequisitesUvOf(Set<Uv> corequisitesUvOf) {
        this.corequisitesUvOf = corequisitesUvOf;
    }

    public Set<Uv> getPrerequisitesUv() {
        return prerequisitesUv;
    }

    public void setPrerequisitesUv(Set<Uv> prerequisitesUv) {
        this.prerequisitesUv = prerequisitesUv;
    }

    @Override
    public String toString() {
        return "Uv{" +
                "id=" + id +
                ", remoteId='" + remoteId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", minSemester=" + minSemester +
                ", availableForCart=" + availableForCart +
                ", chs=" + chs +
                ", location=" + location +
                ", uvType=" + uvType +
                ", semesters=" + semesters +
                ", prerequisitesUv=" + prerequisitesUv +
                '}';
    }
}
