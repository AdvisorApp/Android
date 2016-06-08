package com.advisorapp.api;

/**
 * Created by Steeve on 29/05/2016.
 */
public interface API {
    String IP = "192.168.42.213";
    String URL = "http://"+IP+":8090/api/";
    String AUTHENT_ROUTE = URL+"auths/token";
    String SIGNUP = URL+"auths/signup";
    String GET_REMAINING_UVS = URL+"api/studyPlans/3/remainingUvs";
}
