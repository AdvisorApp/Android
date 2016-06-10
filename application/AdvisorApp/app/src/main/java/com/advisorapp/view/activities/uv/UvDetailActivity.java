package com.advisorapp.view.activities.uv;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.advisorapp.AdvisorAppApplication;
import com.advisorapp.R;
import com.advisorapp.api.APIHelper;
import com.advisorapp.api.Token;
import com.advisorapp.model.StudyPlan;
import com.advisorapp.model.User;
import com.advisorapp.model.Uv;
import com.advisorapp.model.UvUser;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    private List<UvUser> uvUsers = new ArrayList<UvUser>();
    private UvUser uvUser;
    private User user;

    private StudyPlan studyplan;

    private Uv uv;
    private boolean uvAlreadyAdd;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvdetail);
        ButterKnife.bind(this);

        this.token = new Token(getIntent().getStringExtra("token"));
        this.studyplan = getIntent().getParcelableExtra("studyPlan");
        this.uv = getIntent().getParcelableExtra("uv");
        this.uvAlreadyAdd = getIntent().getBooleanExtra("uvAlreadyAdd", true);

        AdvisorAppApplication app = (AdvisorAppApplication) getApplication();
        this.mRequestQueue = app.getmVolleyRequestQueue();
        this.mMapper = new ObjectMapper();

        this.runOnUiThread(new Runnable() {
            public void run() {
                getUser();
            }
        });

        this.handleView();

    }

    private void handleView() {

        // Header
        ((TextView) this.findViewById(R.id.uvName)).setText(this.uv.getName());
        TextView chsUV = (TextView)findViewById(R.id.uv_chs);
        chsUV.setText(String.format("%d",(long)this.uv.getChs()));
        TextView idUV = (TextView)findViewById(R.id.idUv);
        idUV.setText(uv.getRemoteId());

        // Description
        ((TextView) this.findViewById(R.id.uv_description_content)).setText("Description de l'UV");

        // Popularity and comments
        ((TextView) this.findViewById(R.id.uv_popularity)).setText("Popularity = 4");
        ((TextView) this.findViewById(R.id.uv_comments)).setText("Comments = 2");

        Log.d(TAG, "" + this.uv.getName());

        if(!this.uvAlreadyAdd){
            this.findViewById(R.id.btn_add).setVisibility(View.VISIBLE);
        }
        else{
            this.findViewById(R.id.linear_average_comment).setVisibility(View.VISIBLE);
            ((TextView) this.findViewById(R.id.uv_average)).setText("Your average : 5" );
            ((TextView) this.findViewById(R.id.uv_mark)).setText("You have marked :  4");

            ((TextView) this.findViewById(R.id.uv_is_commented)).setText("You have commented this UV");

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
        intent.putExtra("studyplan", this.studyplan);
        intent.putExtra("uv", this.uv);
        startActivity(intent);
    }

    private class DownloadUvUsersTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {
            getUvUsers();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
        }
    }

    private void getUvUsers() {
        JsonArrayRequest myRequest = APIHelper.getUvUsers(this.token, uv.getId(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                UvUser uvuser = mMapper.readValue(response.getJSONObject(i).toString(), UvUser.class);
                                uvUsers.add(uvuser);
                                Log.d(TAG, uvuser.toString());
                                /*if(uvuser.getUser().getId() == user.getId()){
                                    uvUser = uvuser;
                                }*/
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                });
        mRequestQueue.add(myRequest);

    }

    public void getUser() {
        JsonObjectRequest myRequest = APIHelper.getHello(this.token,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            user = mMapper.readValue(response.toString(), User.class);
                            Log.d(TAG, response.toString());
                            //new DownloadUvUsersTask().doInBackground();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                });
        mRequestQueue.add(myRequest);
    }
    
}
