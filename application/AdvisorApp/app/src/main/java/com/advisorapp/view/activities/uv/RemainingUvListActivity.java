package com.advisorapp.view.activities.uv;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

import com.advisorapp.AdvisorAppApplication;
import com.advisorapp.R;
import com.advisorapp.api.APIHelper;
import com.advisorapp.api.Token;
import com.advisorapp.listeners.RecyclerItemClickListener;
import com.advisorapp.model.StudyPlan;
import com.advisorapp.model.Uv;
import com.advisorapp.view.activities.login.LoginActivity;
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

import butterknife.ButterKnife;

/**
 * Created by Alexis on 08/06/2016.
 */
public class RemainingUvListActivity extends AppCompatActivity {

    private static final String TAG = "UvListActivity";

    private Token token;

    private RequestQueue mRequestQueue;
    private ObjectMapper mMapper;

    private RecyclerView mRecyclerView;
    private UvListAdapter mUvListAdapter;

    private StudyPlan studyPlan;
    private List<Uv> uvs = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvlist);
        ButterKnife.bind(this);

        this.token = getIntent().getParcelableExtra("token");
        Log.d("token", token.getToken());
        this.studyPlan = getIntent().getParcelableExtra("studyPlan");

        AdvisorAppApplication app = (AdvisorAppApplication) getApplication();
        this.mRequestQueue = app.getmVolleyRequestQueue();
        this.mMapper = new ObjectMapper();

        this.runOnUiThread(new Runnable() {
            public void run() {
                new DownloadUvTask().doInBackground();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUvListAdapter = new UvListAdapter(this.uvs);
        mRecyclerView.setAdapter(mUvListAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Log.d(TAG, ""+uvs.get(position));
                        Intent intent = new Intent(getApplicationContext(), UvDetailActivity.class);
                        intent.putExtra("token", token.getToken());
                        intent.putExtra("studyPlan", studyPlan);
                        intent.putExtra("uv", uvs.get(position));
                        intent.putExtra("uvAlreadyAdd", false);
                        startActivity(intent);
                    }
                })
        );
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

    private class DownloadUvTask extends AsyncTask<String, String, Boolean> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Boolean doInBackground(String... params) {
            getUvList();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
        }
    }

    private void getUvList() {
        JsonArrayRequest myRequest = APIHelper.getRemainingUvs(this.token, this.studyPlan.getId(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                Uv uv = mMapper.readValue(response.getJSONObject(i).toString(), Uv.class);
                                uvs.add(uv);
                                mUvListAdapter.notifyDataSetChanged();
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
