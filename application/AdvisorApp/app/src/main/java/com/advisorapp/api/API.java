package com.advisorapp.api;

/**
 * Created by Steeve on 29/05/2016.
 */
public interface API {
    String IP = "172.17.3.49";
    String URL = "http://"+IP+":8090/api/";
    String AUTHENT_ROUTE = URL+"auths/token";
    String SIGNUP = URL+"auths/signup";
}
