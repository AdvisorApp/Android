package com.advisorapp.api;

import com.advisorapp.api.requests.AbstractJsonArrayRequest;
import com.advisorapp.api.requests.AbstractJsonObjectRequest;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Alexis on 09/06/2016.
 */
public class APIHelper {

    public static AbstractJsonObjectRequest postAuth(HashMap<String, String> params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        return new AbstractJsonObjectRequest(null, Request.Method.POST, API.AUTHENT_ROUTE,  new JSONObject(params), listener, errorListener);
    }

    public static AbstractJsonObjectRequest getHello(Token token, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        return new AbstractJsonObjectRequest(token, API.USERS_ME_ROUTE,  null, listener, errorListener);
    }

    public static AbstractJsonArrayRequest getRemainingUvs(Token token, long studyPlanId, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
        String url = API.URL + "studyPlans/" + studyPlanId + "/remainingUvs";
        return new AbstractJsonArrayRequest(token, url, listener, errorListener);
    }

    public static AbstractJsonArrayRequest getCoRequisiteUvs(Token token, long uvId, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        String url = API.URL + "uvs/" + uvId + "/corequisites";
        return new AbstractJsonArrayRequest(token, url, listener, errorListener);
    }

    public static AbstractJsonArrayRequest getSemesterOfSP(Token token, long studyPlanId, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        String url = API.URL + "studyPlans/" + studyPlanId + "/semesters";
        return new AbstractJsonArrayRequest(token, url, listener, errorListener);
    }

    public static AbstractJsonArrayRequest addUvToSemester(Token token, long semesterId, long uvId, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
        String url = API.URL + "semesters/" + semesterId + "/uv/" + uvId;
        return new AbstractJsonArrayRequest(token, Request.Method.PUT, url,  null, listener, errorListener);
    }

    public static AbstractJsonArrayRequest getStudyPlans(Token token, long userId, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
        String url = API.USER + userId + API.STUDY_PLANS;
        return new AbstractJsonArrayRequest(token, url, listener, errorListener);
    }

    public static AbstractJsonObjectRequest postStudyPlan(Token token, HashMap<String, String> params, long userId, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        String url = API.USER + userId + API.STUDY_PLANS;
        return new AbstractJsonObjectRequest(token, Request.Method.POST, url,  new JSONObject(params), listener, errorListener);
    }

    public static AbstractJsonObjectRequest deleteStudyPlan(Token token, long studyPlanId, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener){
        String url = API.URL + "studyPlans/" + studyPlanId;
        return new AbstractJsonObjectRequest(token, Request.Method.DELETE, url,  null, listener, errorListener);
    }

    public static AbstractJsonArrayRequest getUvUsers(Token token, long userId, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener){
        String url = API.URL + API.UV_ROUTE + userId + API.UV_USER;
        return new AbstractJsonArrayRequest(token, url, listener, errorListener);
    }

}
