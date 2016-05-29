package com.advisorapp.model;



import java.util.Date;
import java.util.Set;


public class User {

    private long id;

    private String firstName;

    private String lastName;

    private Date birthday;

    private String remoteId;

    private String email;

    private String password;

    private Set<StudyPlan> studyPlans;


    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }

    public Set<StudyPlan> getStudyPlans() {
        return studyPlans;
    }

    public void addStudyPlan(StudyPlan studyPlan){
        this.studyPlans.add(studyPlan);
        studyPlan.setUser(this);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setStudyPlans(Set<StudyPlan> studyPlans) {
        this.studyPlans = studyPlans;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("id=").append(id);
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", birthday=").append(birthday);
        sb.append(", remoteId='").append(remoteId).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", studyPlans=").append(studyPlans);
        sb.append('}');
        return sb.toString();
    }
}
