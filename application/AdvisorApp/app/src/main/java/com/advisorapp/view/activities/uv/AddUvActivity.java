package com.advisorapp.view.activities.uv;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.advisorapp.AdvisorAppApplication;
import com.advisorapp.R;
import com.advisorapp.api.APIHelper;
import com.advisorapp.api.Token;
import com.advisorapp.model.ErrorMessage;
import com.advisorapp.model.Semester;
import com.advisorapp.model.StudyPlan;
import com.advisorapp.model.Uv;
import com.advisorapp.view.adapters.UvListAdapter;
import com.afollestad.materialdialogs.DialogAction;
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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

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

    private ArrayList<Uv> coRequisiteUvs;
    private RecyclerView coRequisiteUvsRecyclerView;
    private UvListAdapter coRequisiteUvsListAdapter;

    private RecyclerView preRequisiteUvsRecyclerView;
    private UvListAdapter preRequisiteUvsListAdapter;

    private ArrayList<Semester> semesters;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvadd);
        ButterKnife.bind(this);

        this.coRequisiteUvs = new ArrayList<>();
        this.semesters = new ArrayList<>();

        this.token = new Token(getIntent().getStringExtra("token"));
        this.studyPlan = getIntent().getParcelableExtra("studyPlan");
        this.uv = getIntent().getParcelableExtra("uv");

        AdvisorAppApplication app = (AdvisorAppApplication) getApplication();
        this.mRequestQueue = app.getmVolleyRequestQueue();
        this.mMapper = new ObjectMapper();

        this.runOnUiThread(new Runnable() {
            public void run() {
                new DownloadCoRequisitesTask().doInBackground();
                new DownloadSemesterTask().doInBackground();
            }
        });

        this.handleView();

    }

    private void handleView() {

        ((TextView) this.findViewById(R.id.tv_uv_name)).setText(this.uv.getName());

        this.coRequisiteUvsRecyclerView = (RecyclerView) findViewById(R.id.coRequisRecyclerView);
        this.coRequisiteUvsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.coRequisiteUvsListAdapter = new UvListAdapter(this.coRequisiteUvs);
        this.coRequisiteUvsRecyclerView.setAdapter(this.coRequisiteUvsListAdapter);

        this.preRequisiteUvsRecyclerView = (RecyclerView) findViewById(R.id.preRequisRecyclerView);
        this.preRequisiteUvsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.preRequisiteUvsListAdapter = new UvListAdapter(this.uv.getPrerequisitesUv());
        this.preRequisiteUvsRecyclerView.setAdapter(this.preRequisiteUvsListAdapter);

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
                .items(semesters)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/
                        return true;
                    }
                })
                .positiveText("Valider")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        new AddUvTask().doInBackground(semesters.get(dialog.getSelectedIndex()).getId());
                    }
                })
                .show();
    }

    private class DownloadCoRequisitesTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {
            getCoRequisites();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
        }
    }

    private void getCoRequisites() {
        JsonArrayRequest myRequest = APIHelper.getCoRequisiteUvs(this.token, this.uv.getId(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                Uv uv = mMapper.readValue(response.getJSONObject(i).toString(), Uv.class);
                                coRequisiteUvs.add(uv);
                                coRequisiteUvsListAdapter.notifyDataSetChanged();
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

    private class AddUvTask extends AsyncTask<Long, Long, Boolean> {

        private MaterialDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new MaterialDialog.Builder(getApplicationContext())
                    .title("Adding")
                    .content("Wait a moment please...")
                    .progress(true, 0)
                    .autoDismiss(false)
                    .cancelable(false)
                    .show();
        }


        @Override
        protected Boolean doInBackground(Long... params) {
            putUv(params[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
        }
    }

    private void putUv(long semesterId) {
        JsonArrayRequest myRequest = APIHelper.addUvToSemester(this.token, semesterId, this.uv.getId(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String errorString = new String(error.networkResponse.data, "UTF-8");
                            Log.d("Error.Response", errorString);
                            ErrorMessage errorMessage = mMapper.readValue(errorString, ErrorMessage.class);
                            displayError(errorMessage.getMessage());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JsonMappingException e) {
                            e.printStackTrace();
                        } catch (JsonParseException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
        mRequestQueue.add(myRequest);

    }

    public void displayError(String error){
        new MaterialDialog.Builder(this)
                .title("Information")
                .content(error)
                .positiveText("Ok")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        finish();
                    }
                })
                .show();
    }

}
