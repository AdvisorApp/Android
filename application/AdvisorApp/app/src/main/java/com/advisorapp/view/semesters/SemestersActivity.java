package com.advisorapp.view.semesters;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.advisorapp.AdvisorAppApplication;
import com.advisorapp.R;
import com.advisorapp.api.APIHelper;
import com.advisorapp.api.Token;
import com.advisorapp.model.Semester;
import com.advisorapp.model.StudyPlan;
import com.advisorapp.model.Uv;
import com.advisorapp.view.adapters.UvListAdapter;
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

/**
 * Created by Steeve on 08/06/2016.
 */
public class SemestersActivity extends AppCompatActivity{

    private Token token;

    private RequestQueue mRequestQueue;
    private ObjectMapper mMapper;

    private StudyPlan studyPlan;
    private ArrayList<Semester> semesters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semesters);

        this.semesters = new ArrayList<>();

        this.token = getIntent().getParcelableExtra("token");
        this.studyPlan = getIntent().getParcelableExtra("studyPlan");

        AdvisorAppApplication app = (AdvisorAppApplication) getApplication();
        this.mRequestQueue = app.getmVolleyRequestQueue();
        this.mMapper = new ObjectMapper();

        this.runOnUiThread(new Runnable() {
            public void run() {
                new DownloadSemesterTask().doInBackground();
            }
        });
    }

    private class DownloadSemesterTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {
            getSemesters();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

        }
    }

    private void getSemesters() {
        JsonArrayRequest myRequest = APIHelper.getSemesterOfSP(this.token, this.studyPlan.getId(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                Semester semester = mMapper.readValue(response.getJSONObject(i).toString(), Semester.class);
                                semesters.add(semester);
                                Log.d("semestre", semester.toString());
                            }
                            Fragment f = new SemestersFragment(token, studyPlan, semesters);
                            getFragmentManager().beginTransaction().replace(R.id.fragment, f, "SemestersFragment").commit();
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
