package com.advisorapp.api.requests;

import com.advisorapp.api.Token;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alexis on 10/06/2016.
 */
public class AbstractStringRequest extends StringRequest{


    private Token token;

    public AbstractStringRequest(Token token, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Request.Method.GET, url, listener, errorListener);
        this.token = token;
    }

    public AbstractStringRequest(Token token, int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.token = token;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        if(this.token != null) headers.put("X-Authorization", this.token.getToken());
        return headers;
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=UTF-8";
    }

}
