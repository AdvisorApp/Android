package com.advisorapp.view.studyplanlist;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.advisorapp.AdvisorAppApplication;
import com.advisorapp.R;
import com.advisorapp.api.APIHelper;
import com.advisorapp.api.Token;
import com.advisorapp.holder.StudyPlanListAdapter;
import com.advisorapp.model.StudyPlan;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class StudyPlanListActivity extends AppCompatActivity
{

    private static final String TAG = "StudyPlanListActivity";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mStudyPlanListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private List<StudyPlan> studyPlans = new ArrayList<>();

    private static final int REQUEST_SIGNUP = 0;
    private Token token;
    private ObjectMapper mMapper;

    private RequestQueue mRequestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studyplanlist);

        ButterKnife.bind(this);

        this.token = getIntent().getParcelableExtra("token");

        Toast.makeText(getBaseContext(), token.getToken().toString(), Toast.LENGTH_LONG);

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
                        new StudyPlanTask().doInBackground();
                    }
                });
            }
        }).start();

/*        StudyPlan sp = new StudyPlan();
        sp.setName("SP1");
        studyPlanList.add(sp);
        studyPlanList.add(sp);
        studyPlanList.add(sp);
        studyPlanList.add(sp);*/

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mStudyPlanListAdapter = new StudyPlanListAdapter(this.studyPlans);
        mRecyclerView.setAdapter(mStudyPlanListAdapter);

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

/*    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }*/

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }


    private class StudyPlanTask extends AsyncTask<String, String, Boolean> {

        private MaterialDialog progressDialog;

        private Token token;

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
            progressDialog.dismiss();
        }
    }

    private void getStudyPlanList(long userId) {
        JsonArrayRequest myRequest = APIHelper.getStudyPlans(this.token, userId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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
}
