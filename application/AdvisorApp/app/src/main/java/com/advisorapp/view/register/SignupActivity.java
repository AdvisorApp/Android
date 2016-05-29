package com.advisorapp.view.register;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.advisorapp.API;
import com.advisorapp.AdvisorAppApplication;
import com.advisorapp.R;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private RequestQueue mRequestQueue;

    @BindView(R.id.input_firstname)
    EditText firstnameText;

    @BindView(R.id.input_lastname)
    EditText lastnameText;

    @BindView(R.id.inpute_remote_id)
    EditText remoteIdText;

    @BindView(R.id.input_email)
    EditText _emailText;

    @BindView(R.id.input_password)
    EditText _passwordText;

    @BindView(R.id.btn_signup)
    Button _signupButton;

    @BindView(R.id.link_login)
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        AdvisorAppApplication app = (AdvisorAppApplication) getApplication();
        mRequestQueue = app.getmVolleyRequestQueue();

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

//        if (!validate()) {
//            onSignupFailed();
//            return;
//        }

//        _signupButton.setEnabled(false);

        String firstname = firstnameText.getText().toString();
        String lastname = lastnameText.getText().toString();
        String remoteID = remoteIdText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

//        new SignupTask().doInBackground(firstname,lastname,remoteID,email,password);
        new SignupTask().doInBackground("test", "ffzefzefz", "ffzefz", "abcd@got.com", "azerty");
        // TODO: Implement your own signup logic here.
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = firstnameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            firstnameText.setError("at least 3 characters");
            valid = false;
        } else {
            firstnameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private class SignupTask extends AsyncTask<String, String, Boolean> {

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

            StringRequest stringRequest = new StringRequest(Request.Method.POST, API.USERS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(SignupActivity.this, response, Toast.LENGTH_LONG).show();
                    //TODO la response est le token de l'user, à implémenter + récupérer les données
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                        }

                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("first_name", "firsttest");
                    params.put("last_name", "lasttest");
                    params.put("remote_id", "idtest");
                    params.put("email", "azerty@gamil.com");
                    params.put("password", "iasass");
                    return params;
                }
            };

            try {
                Log.d(TAG,stringRequest.getHeaders().toString());
            } catch (AuthFailureError authFailureError) {
                authFailureError.printStackTrace();
            }

            try {
                Map<String,String> map = stringRequest.getHeaders();
                for (String key : map.keySet()) {
                    Log.d(TAG,key);
                }
            } catch (AuthFailureError authFailureError) {
                authFailureError.printStackTrace();
            }
            mRequestQueue.add(stringRequest);
            return true; //todo manage en fonction token
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
        }
    }
}