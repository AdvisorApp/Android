package com.advisorapp.model;



import java.util.Set;

public class Semester {


    private long id;

    private int number;

    private StudyPlan studyPlan;

    private Set<Uv> uvs;

    public Set<Uv> getUvs() {
        return this.uvs;
    }

    public long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public StudyPlan getStudyPlan() {
        return studyPlan;
    }

    public void setStudyPlan(StudyPlan studyPlan) {
        this.studyPlan = studyPlan;
    }

    public void addUv(Uv uv){
        this.uvs.add(uv);
    }

    public void setUvs(Set<Uv> uvs) {
        this.uvs = uvs;
    }
}