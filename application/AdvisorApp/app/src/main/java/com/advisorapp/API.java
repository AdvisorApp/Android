package com.advisorapp;

/**
 * Created by Steeve on 29/05/2016.
 */
public interface API {
    String IP = "192.168.43.216";
    String URL = "http://"+IP+":8090/api/";
    String AUTHENT_ROUTE = URL+"auths/token";
    String USERS = URL+"users";
}
