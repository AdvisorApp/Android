package com.advisorapp.model;

import java.io.Serializable;
import java.util.Set;

public class StudyPlan implements Serializable{

    private long id;

    private User user;

    private String name;

    private Set<Semester> semesters;

    private Option option;

    public long getId() {
        return id;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addSemester(Semester semester){
        this.semesters.add(semester);
        semester.setStudyPlan(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Semester> getSemesters() {
        return semesters;
    }

    public void setSemesters(Set<Semester> semesters) {
        this.semesters = semesters;
    }

    public Option getOption() {
        return option;
    }

    public void setOption(Option option) {
        this.option = option;
    }

    @Override
    public String toString() {
        return "StudyPlan{" +
                "id=" + id +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", semesters=" + semesters +
                ", option=" + option +
                '}';
    }
}
