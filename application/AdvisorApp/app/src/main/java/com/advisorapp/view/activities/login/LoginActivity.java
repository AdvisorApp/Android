package com.advisorapp.view.activities.login;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.advisorapp.AdvisorAppApplication;
import com.advisorapp.R;
import com.advisorapp.api.APIHelper;
import com.advisorapp.api.Token;
import com.advisorapp.view.activities.register.SignupActivity;
import com.advisorapp.view.studyplanlist.StudyPlanListActivity;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
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
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        AdvisorAppApplication app = (AdvisorAppApplication) getApplication();
        mRequestQueue = app.getmVolleyRequestQueue();
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

    @OnClick(R.id.btn_login)
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
    }


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
        this.saveToken();
        Intent intent = new Intent(getApplicationContext(), StudyPlanListActivity.class);
        intent.putExtra("token", this.token);
        startActivity(intent);
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

    private class AuthentificationTask extends AsyncTask<String, String, Boolean> {

        private MaterialDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new MaterialDialog.Builder(getApplicationContext())
                    .title("Authenticating")
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
            parametres.put("email", params[0]);
            parametres.put("password", params[1]);
            postLogin(parametres);
            return true; //todo manage en fonction token ?
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
        }
    }

    private void postLogin(HashMap<String, String> params) {
        JsonObjectRequest myRequest = APIHelper.postAuth(params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        try {
                            token = new Token((String) response.get("token"));
                            onLoginSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error.Response", error.toString());
                onLoginFailed();
                loginButton.setEnabled(true);
            }
        });
        mRequestQueue.add(myRequest);
    }

    public void saveToken() {
        SharedPreferences sharedPref = this.getSharedPreferences("advisorapp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_token), this.token.getToken());
        editor.commit();
        Log.d("token", this.token.getToken());
    }

}