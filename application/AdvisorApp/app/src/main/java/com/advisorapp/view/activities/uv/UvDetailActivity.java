package com.advisorapp.view.activities.uv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.advisorapp.AdvisorAppApplication;
import com.advisorapp.R;
import com.advisorapp.api.Token;
import com.advisorapp.model.StudyPlan;
import com.advisorapp.model.Uv;
import com.advisorapp.view.adapters.UvListAdapter;
import com.android.volley.RequestQueue;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Alexis on 09/06/2016.
 */
public class UvDetailActivity extends AppCompatActivity {

    private static final String TAG = "UvDetailActivity";

    private Token token;

    private RequestQueue mRequestQueue;
    private ObjectMapper mMapper;

    private StudyPlan studyPlan;
    private Uv uv;
    private boolean uvAlreadyAdd;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvdetail);
        ButterKnife.bind(this);

        this.token = new Token(getIntent().getStringExtra("token"));
        this.studyPlan = getIntent().getParcelableExtra("studyPlan");
        this.uv = getIntent().getParcelableExtra("uv");
        this.uvAlreadyAdd = getIntent().getBooleanExtra("uvAlreadyAdd", true);

        AdvisorAppApplication app = (AdvisorAppApplication) getApplication();
        this.mRequestQueue = app.getmVolleyRequestQueue();
        this.mMapper = new ObjectMapper();

        this.runOnUiThread(new Runnable() {
            public void run() {
            }
        });
        
        this.handleView();

    }

    private void handleView() {

        ((TextView) this.findViewById(R.id.tv_uv_name)).setText(this.uv.getName());
        Log.d(TAG, "" + this.uv);

        if(!this.uvAlreadyAdd){
            this.findViewById(R.id.btn_add).setVisibility(View.VISIBLE);
        }
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

    @OnClick(R.id.btn_add)
    public void addUv(View v){
        Intent intent = new Intent(getApplicationContext(), AddUvActivity.class);
        intent.putExtra("token", token.getToken());
        intent.putExtra("studyPlan", this.studyPlan);
        intent.putExtra("uv", this.uv);
        startActivity(intent);
    }

}
