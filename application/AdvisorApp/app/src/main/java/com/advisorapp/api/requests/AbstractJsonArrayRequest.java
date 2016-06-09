package com.advisorapp.api.requests;

import android.os.AsyncTask;
import android.util.Log;

import com.advisorapp.api.API;
import com.advisorapp.api.Token;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexis on 09/06/2016.
 */
public class AbstractJsonArrayRequest extends JsonArrayRequest{

    private Token token;

    public AbstractJsonArrayRequest(Token token, String url, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(url, listener, errorListener);
        this.token = token;
    }

    public AbstractJsonArrayRequest(Token token, int method, String url, JSONArray jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        this.token = token;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        if(this.token != null) headers.put("X-Authorization", this.token.getToken());
        return headers;
    }

}
