package com.advisorapp.view.studyplanlist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.advisorapp.AdvisorAppApplication;
import com.advisorapp.R;
import com.advisorapp.api.API;
import com.advisorapp.api.APIHelper;
import com.advisorapp.api.Token;
import com.advisorapp.model.StudyPlan;
import com.advisorapp.model.User;
import com.advisorapp.view.adapters.StudyPlanListAdapter;
import com.advisorapp.view.semesters.SemestersActivity;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudyPlanListActivity extends AppCompatActivity
{
    private static final String TAG = "StudyPlanListActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mStudyPlanListAdapter;
    private List<StudyPlan> studyPlans = new ArrayList<>();
    private Token token;
    private ObjectMapper mMapper;
    private RequestQueue mRequestQueue;
    private User user;

    @BindView(R.id.floating_button)
    FloatingActionButton fab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyplanlist);

        AdvisorAppApplication app = (AdvisorAppApplication) getApplication();
        this.mRequestQueue = app.getmVolleyRequestQueue();
        this.mMapper = new ObjectMapper();

        ButterKnife.bind(this);

        this.token = getIntent().getParcelableExtra("token");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.runOnUiThread(new Runnable() {
            public void run() {
                getUser();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mStudyPlanListAdapter = new StudyPlanListAdapter(this.studyPlans, this);
        mRecyclerView.setAdapter(mStudyPlanListAdapter);



    }

    @OnClick(R.id.floating_button)
    public void openDialog(View v) {
        Log.d(TAG, "AddStudyPlanDialog");
        new MaterialDialog.Builder(this)
                .title(R.string.add_studyplan)
                .customView(R.layout.add_studyplan, false)
                .positiveText(R.string.positive)
                .negativeText(R.string.negative)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText edit = (EditText) dialog.findViewById(R.id.new_studyplan_name);
                        String name = edit.getText().toString();
                        HashMap<String, String> parameter = new HashMap<String, String>();
                        if(name.isEmpty())
                            return;

                        parameter.put("name", name);
                        postStudyPlanList(parameter);
                    }
                })
                .show();

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

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    private class DownloadStudyPlansTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {
            getStudyPlanList();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
        }
    }

    private class UploadStudyPlanTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("name", params[0]);
            postStudyPlanList(parameters);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
        }
    }

    public void runDeleteStudyPlanTask(final long studyPlanId){
        new DeleteStudyPlanTask().doInBackground(String.valueOf(studyPlanId));
    }

    public class DeleteStudyPlanTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {
            deleteStudyPlan(Long.parseLong(params[0]));
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
        }
    }

   /* public void deleteStudyPlan(final long studyPlanId) {
        StringRequest myRequest = APIHelper.deleteStudyPlan(this.token, studyPlanId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        for(int i = 0; i < studyPlans.size(); i++){
                            if (studyPlans.get(i).getId() == studyPlanId){
                                studyPlans.get(i);
                                break;
                            }
                        }
                        Log.d(TAG, "A supprimer " + studyPlanId );
                        mStudyPlanListAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                })
        mRequestQueue.add(myRequest);

    }*/

    private void deleteStudyPlan(final long studyPlanId){
Log.d(TAG, API.URL + "studyPlans/" + studyPlanId);
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, API.URL + "studyPlans/" + studyPlanId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        for(int i = 0; i < studyPlans.size(); i++){
                            if (studyPlans.get(i).getId() == studyPlanId){
                                studyPlans.remove(i);
                                break;
                            }
                        }
                        Log.d(TAG, "A supprimer " + studyPlanId );
                        mStudyPlanListAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                if(token != null) headers.put("X-Authorization", token.getToken());
                return headers;
            }

        };
        mRequestQueue.add(postRequest);
    }

    private void getStudyPlanList() {
        JsonArrayRequest myRequest = APIHelper.getStudyPlans(this.token, user.getId(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                StudyPlan studyplan = mMapper.readValue(response.getJSONObject(i).toString(), StudyPlan.class);
                                studyPlans.add(studyplan);
                                mStudyPlanListAdapter.notifyDataSetChanged();

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

    private void postStudyPlanList(HashMap<String, String> parameters) {
        JsonObjectRequest myRequest = APIHelper.postStudyPlan(this.token, parameters, user.getId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        studyPlans.clear();
                        getStudyPlanList();
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

    public void startSemesterActivity(StudyPlan studyPlan){
        Log.d(TAG, studyPlan.toString());
        Intent intent = new Intent(getApplicationContext(), SemestersActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("studyPlan", studyPlan);
        startActivity(intent);
    }

    private class getSemesterTask extends AsyncTask<Long, Long, Boolean> {

        @Override
        protected void onPreExecute() {

        }


        @Override
        protected Boolean doInBackground(Long... params) {
            getStudyPlanList();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

        }
    }

    public void getUser() {
        JsonObjectRequest myRequest = APIHelper.getHello(this.token,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            user = mMapper.readValue(response.toString(), User.class);
                            Log.d(TAG, "response");
                            new getSemesterTask().doInBackground();
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
