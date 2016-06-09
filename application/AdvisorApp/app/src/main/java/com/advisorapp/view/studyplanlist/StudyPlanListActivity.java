package com.advisorapp.view.studyplanlist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.advisorapp.AdvisorAppApplication;
import com.advisorapp.R;
import com.advisorapp.api.API;
import com.advisorapp.api.Token;
import com.advisorapp.holder.StudyPlanListAdapter;
import com.advisorapp.model.StudyPlan;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class StudyPlanListActivity extends AppCompatActivity
{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private static final String TAG = "StudyPlanActivity";
    private static final int REQUEST_SIGNUP = 0;
    private Token token;


    @BindView(R.id.input_email)
    EditText emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button loginButton;
    @BindView(R.id.link_signup)
    TextView signupLink;

    private RequestQueue mRequestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyplanlist);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        //définit l'agencement des cellules, ici de façon verticale, comme une ListView
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //pour adapter en grille comme une RecyclerView, avec 2 cellules par ligne
        //mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));



        AdvisorAppApplication app = (AdvisorAppApplication) getApplication();
        mRequestQueue = app.getmVolleyRequestQueue();

        List<StudyPlan> studyPlanList = new ArrayList<>();

        new Thread(new Runnable() {

            @Override
            public void run() {
                StudyPlanListActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        new StudyPlanTask().doInBackground("testToken", "1");
                    }
                });
            }
        }).start();

        StudyPlan sp = new StudyPlan();
        sp.setName("SP1");
        studyPlanList.add(sp);
        studyPlanList.add(sp);
        studyPlanList.add(sp);
        studyPlanList.add(sp);

        //puis créer un MyAdapter, lui fournir notre liste de villes.
        //cet adapter servira à remplir notre recyclerview
        mRecyclerView.setAdapter(new StudyPlanListAdapter(studyPlanList));

    }

    @Override
    protected void onResume() {
        super.onResume();
        new StudyPlanTask().doInBackground("testToken", "1");
    }


    @Override
    protected void onStop() {
        mRequestQueue.cancelAll(this);
        super.onStop();
    }

   /* @OnClick(R.id.btn_login)
    public void login(View v) {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        loginButton.setEnabled(false);


        String email = emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new AuthentificationTask().doInBackground(email, password);
    }

    @OnClick(R.id.link_signup)
    public void signUp(View v) {
        //TODO : after creating SignupActivity, decomment
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;


        String email = emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between x(min) and y(max) alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }


    private class StudyPlanTask extends AsyncTask<String, String, Boolean> {

        private MaterialDialog progressDialog;

        private Token token;

        @Override
        protected void onPreExecute() {
            progressDialog = new MaterialDialog.Builder(getApplicationContext())
                    .title("Getting study plans")
                    .content("Wait a moment please...")
                    .progress(true, 0)
                    .autoDismiss(false)
                    .cancelable(false)
                    .show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
//
            HashMap<String, String> parametres = new HashMap<>();
            parametres.put("token", "testToken");
            parametres.put("user", "1");

            getStudyPlans(parametres);
            return true; //todo manage en fonction token ?
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
        }
    }

    private void getStudyPlans(HashMap<String, String> params) {
        JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.GET,
                API.USER+params.get("user")+API.STUDY_PLANS, new JSONObject(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                 onLoginSuccess();
                 new Token((String) response.get("token"));
                   //TODO
                    Intent intent = new Intent(getApplication(), StudyPlanListActivity.class);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        /*onLoginFailed();
                        loginButton.setEnabled(true);*/
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        mRequestQueue.add(myRequest);

    }
}
