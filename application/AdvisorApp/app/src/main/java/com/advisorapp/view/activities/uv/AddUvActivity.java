package com.advisorapp.view.activities.uv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.advisorapp.AdvisorAppApplication;
import com.advisorapp.R;
import com.advisorapp.api.Token;
import com.advisorapp.model.StudyPlan;
import com.advisorapp.model.Uv;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;

import org.codehaus.jackson.map.ObjectMapper;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Alexis on 10/06/2016.
 */
public class AddUvActivity extends AppCompatActivity {

    private static final String TAG = "UvDetailActivity";

    private Token token;

    private RequestQueue mRequestQueue;
    private ObjectMapper mMapper;

    private StudyPlan studyPlan;
    private Uv uv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvadd);
        ButterKnife.bind(this);

        this.token = new Token(getIntent().getStringExtra("token"));
        this.studyPlan = getIntent().getParcelableExtra("studyPlan");
        this.uv = getIntent().getParcelableExtra("uv");

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

    @OnClick(R.id.btn_confirm)
    public void confirmAddUv(View v){
        new MaterialDialog.Builder(this)
                .title("Choisir un semestre")
                .items()
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                    }
                })
                .show();
    }

}
