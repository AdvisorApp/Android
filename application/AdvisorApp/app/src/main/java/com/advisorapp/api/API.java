package com.advisorapp.api;

/**
 * Created by Steeve on 29/05/2016.
 */
public interface API {
    
    String IP = "192.168.42.213";
    String URL = "http://"+IP+":8090/api/";
    String AUTHENT_ROUTE = URL+"auths/token";
    String USERS_ME_ROUTE = URL+"users/me";
    String SIGNUP = URL+"auths/signup";
    String STUDY_PLANS = "/studyPlans";
    String USER = URL + "users/";

}
