package com.advisorapp.view.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.advisorapp.AdvisorAppApplication;
import com.advisorapp.R;
import com.advisorapp.api.APIHelper;
import com.advisorapp.api.Token;
import com.advisorapp.model.Uv;
import com.advisorapp.view.activities.login.LoginActivity;
import com.advisorapp.view.activities.register.SignupActivity;
import com.advisorapp.view.activities.uv.RemainingUvListActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alexis on 09/06/2016.
 */
public class SplashscreenActivity extends AppCompatActivity {
    private static final String TAG = "SplashscreenActivity";
    private Token token;

    private RequestQueue mRequestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        AdvisorAppApplication app = (AdvisorAppApplication) getApplication();
        mRequestQueue = app.getmVolleyRequestQueue();

        this.retrieveToken();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        mRequestQueue.cancelAll(this);
        super.onStop();
    }

    public void retrieveToken() {
        SharedPreferences sharedPref = this.getSharedPreferences("advisorapp", Context.MODE_PRIVATE);
        String savedToken = sharedPref.getString(getString(R.string.saved_token), "");
        if (savedToken != null && !savedToken.equals("")) {
            this.token = new Token(savedToken);
            testToken();
        } else{
            this.startLoginActivity();
        }
    }

    public void testToken() {
        JsonObjectRequest myRequest = APIHelper.getHello(this.token,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Intent intent = new Intent(getApplicationContext(), RemainingUvListActivity.class);
                        intent.putExtra("token", token);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        startLoginActivity();
                    }
                });
        mRequestQueue.add(myRequest);
    }

    public void startLoginActivity(){
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        this.finish();
    }
}