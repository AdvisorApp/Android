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
import android.widget.TimePicker;
import android.widget.Toast;

import com.advisorapp.API;
import com.advisorapp.AdvisorAppApplication;
import com.advisorapp.R;
import com.advisorapp.model.User;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        User user = new User();
        user.setLastName(lastnameText.getText().toString().trim());
        user.setFirstName(firstnameText.getText().toString().trim());
        user.setRemoteId(remoteIdText.getText().toString().trim());
        user.setEmail(_emailText.getText().toString().trim());
        user.setPassword(_passwordText.getText().toString().trim());


//        new SignupTask().doInBackground(firstname,lastname,remoteID,email,password);
        new SignupTask().doInBackground(user);
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
        String lastname = lastnameText.getText().toString();
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

        if (lastname.isEmpty()){
            lastnameText.setError("this field is empty");
            valid = false;
        } else {
            lastnameText.setError(null);
        }

        return valid;
    }

    private class SignupTask extends AsyncTask<User, String, Boolean> {

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
        protected Boolean doInBackground(User... user) {
            postUserV1(user[0]);
//            postUserV2(user[0]); //on garde pour l'exemple

            return true; //todo manage en fonction token
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
        }
    }

    private void postUserV1(final User user){

        StringRequest postRequest = new StringRequest(Request.Method.POST, API.SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        onSignupSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        onSignupFailed();
                        _emailText.setError("This mail adresse is already link to an existing account");
                        _signupButton.setEnabled(true);
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    return mapper.writeValueAsString(user).getBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        mRequestQueue.add(postRequest);
    }

    private void postUserV2(User user){
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonUser = mapper.writeValueAsString(user);
            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST,
                    API.SIGNUP, new JSONObject(jsonUser), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Response", response.toString());
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.toString());
                            _emailText.setError("This mail adresse is already link to an existing account");
                            _signupButton.setEnabled(true);
                        }
                    }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json; charset=utf-8");
                    return headers;
                }
            };
            mRequestQueue.add(myRequest);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}