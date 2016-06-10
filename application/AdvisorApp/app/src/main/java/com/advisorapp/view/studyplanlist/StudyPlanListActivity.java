package com.advisorapp.view.studyplanlist;

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
import com.advisorapp.api.APIHelper;
import com.advisorapp.api.Token;
import com.advisorapp.model.StudyPlan;
import com.advisorapp.view.adapters.StudyPlanListAdapter;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import java.util.HashMap;
import java.util.List;

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

    @BindView(R.id.floating_button)
    FloatingActionButton fab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyplanlist);

        ButterKnife.bind(this);

        this.token = getIntent().getParcelableExtra("token");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdvisorAppApplication app = (AdvisorAppApplication) getApplication();
        this.mRequestQueue = app.getmVolleyRequestQueue();
        this.mMapper = new ObjectMapper();

        new Thread(new Runnable() {

            @Override
            public void run() {
                StudyPlanListActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        new DownloadStudyPlansTask().doInBackground();
                    }
                });
            }
        }).start();

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
                        postStudyPlanList(parameter, 1);
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
            getStudyPlanList(1);
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
            postStudyPlanList(parameters, Long.getLong(params[1]));
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
            Log.d("dddd", params[0]);
            deleteStudyPlan(Long.parseLong(params[0]));
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
        }
    }

    public void deleteStudyPlan(final long studyPlanId) {
        JsonObjectRequest myRequest = APIHelper.deleteStudyPlan(this.token, studyPlanId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        for(int i = 0; i < studyPlans.size(); i++){
                            if (studyPlans.get(i).getId() == studyPlanId){
                                studyPlans.get(i);
                                break;
                            }
                        }
                        mStudyPlanListAdapter.notifyDataSetChanged();
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

    private void getStudyPlanList(long userId) {
        JsonArrayRequest myRequest = APIHelper.getStudyPlans(this.token, userId,
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

    private void postStudyPlanList(HashMap<String, String> parameters, final long userId) {
        JsonObjectRequest myRequest = APIHelper.postStudyPlan(this.token, parameters, userId,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        studyPlans.clear();
                        getStudyPlanList(userId);
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
