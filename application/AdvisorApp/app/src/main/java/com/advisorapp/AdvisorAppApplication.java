package com.advisorapp;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Steeve on 29/05/2016.
 */
public class AdvisorAppApplication extends Application {

    private RequestQueue mVolleyRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();

        //Initialisation du Thread-Pool
        mVolleyRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mVolleyRequestQueue.start();
    }

    public RequestQueue getmVolleyRequestQueue() {
        return mVolleyRequestQueue;
    }

    @Override
    public void onTerminate() {
        mVolleyRequestQueue.start();
        super.onTerminate();
    }


}
