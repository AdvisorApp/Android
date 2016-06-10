package com.advisorapp.view.semesters;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.advisorapp.AdvisorAppApplication;
import com.advisorapp.R;
import com.advisorapp.api.API;
import com.advisorapp.api.APIHelper;
import com.advisorapp.api.Token;
import com.advisorapp.model.Location;
import com.advisorapp.model.Semester;
import com.advisorapp.model.StudyPlan;
import com.advisorapp.model.Uv;
import com.advisorapp.view.activities.uv.AddUvActivity;
import com.advisorapp.view.activities.uv.RemainingUvListActivity;
import com.advisorapp.view.activities.uv.UvDetailActivity;
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
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Steeve on 09/06/2016.
 */
public class SemestersFragment extends Fragment{
    private AndroidTreeView tView;

    private Token token;
    private StudyPlan studyPlan;

    private RequestQueue mRequestQueue;
    private ObjectMapper mMapper;

    private ArrayList<Semester> semesters;

    private TreeNode root;

    public SemestersFragment(Token token, StudyPlan studyPlan, ArrayList<Semester> semesters){
        this.token = token;
        this.studyPlan = studyPlan;
        this.semesters = semesters;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_default, null, false);
        final ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);
        rootView.findViewById(R.id.status_bar).setVisibility(View.GONE);

        AdvisorAppApplication app = (AdvisorAppApplication) getActivity().getApplication();
        this.mRequestQueue = app.getmVolleyRequestQueue();
        this.mMapper = new ObjectMapper();

        this.root = TreeNode.root();

        this.buildTree();

/*
        TreeNode myProfile = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.drawable.ic_semester, "Semester 1")).setViewHolder(new SemesterHolder(getActivity()));
        TreeNode bruce = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.drawable.ic_semester, "Semester 2")).setViewHolder(new SemesterHolder(getActivity()));
        TreeNode clark = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.drawable.ic_semester, "Semester 3")).setViewHolder(new SemesterHolder(getActivity()));
        TreeNode barry = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.drawable.ic_semester, "Semester 4")).setViewHolder(new SemesterHolder(getActivity()));
        addProfileData(myProfile);
        addProfileData(clark);
        addProfileData(bruce);
        addProfileData(barry);
        root.addChildren(myProfile, bruce, barry, clark);
*/
        tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        containerView.addView(tView.getView());

        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }

        tView.setDefaultNodeClickListener(nodeClickListener);
        tView.setDefaultNodeLongClickListener(nodeLongClickListener);
        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab);
        fab.setOnClickListener(fabListener);
        return rootView;
    }

    private void addProfileData(TreeNode profile) {

        Uv uv = new Uv("0202103","Physic 1","blabla",1,false,1, Location.DEPARTMENT);
        TreeNode lake = new TreeNode(new UVHolder.UvItem(uv)).setViewHolder(new UVHolder(getActivity()));
        TreeNode mountains = new TreeNode(new UVHolder.UvItem(uv)).setViewHolder(new UVHolder(getActivity()));

        profile.addChildren(lake, mountains);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            if(node.isLeaf() && node.getLevel()>1){
                UVHolder.UvItem item = (UVHolder.UvItem) value;

                Intent intent = new Intent(getActivity().getApplicationContext(), UvDetailActivity.class);
                intent.putExtra("token", token.getToken());
                Log.d("hey", token.getToken());
                intent.putExtra("studyPlan", studyPlan);
                intent.putExtra("uv", item.uv);
                intent.putExtra("uvAlreadyAdd", true);
                startActivity(intent);
            }
        }
    };

    private TreeNode.TreeNodeLongClickListener nodeLongClickListener = new TreeNode.TreeNodeLongClickListener() {
        @Override
        public boolean onLongClick(TreeNode node, Object value) {
            if (node.isLeaf() && node.getLevel()>1){
                Uv uv = ((UVHolder.UvItem) value).uv;
                new MaterialDialog.Builder(getActivity())
                        .title("Remove UV")
                        .content("Do you want remove UV-" + uv.getRemoteId() + " from this semester ?")
                        .positiveText("Yes, delete UV")
                        .negativeText("No")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                //todo remove l'uv
                                dialog.dismiss();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .autoDismiss(false)
                        .show();
                return true;
            }
            return false;
        }
    };

    private View.OnClickListener fabListener = new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            final ArrayList<String> listActions = new ArrayList<>();
            listActions.add("Add new semester");
            listActions.add("Add UV");
            listActions.add("Add Cart UV");
            new MaterialDialog.Builder(getActivity())
                    .title("Action")
                    .items(listActions)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                            switch (which) {
                                case 0:
                                    postStudySemester();
                                    break;
                                case 1:
                                    Intent intent = new Intent(getActivity().getApplicationContext(), RemainingUvListActivity.class);
                                    intent.putExtra("token", token);
                                    intent.putExtra("studyPlan", studyPlan);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    //todo panier
                                    break;
                            }
                            return true;
                        }
                    })
                    .positiveText("Choose")
                    .show();
        }
    };

    private void buildTree() {
        for(Semester semester : semesters){
            TreeNode semesterNode = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.drawable.ic_semester, "Semester " + semester.getNumber())).setViewHolder(new SemesterHolder(getActivity()));
            for (Uv uv : semester.getUvs()) {
                TreeNode uvNode = new TreeNode(new UVHolder.UvItem(uv)).setViewHolder(new UVHolder(getActivity()));
                semesterNode.addChild(uvNode);
            }
            this.root.addChild(semesterNode);
        }
    }

    private void postStudySemester() {
        JsonObjectRequest myRequest = APIHelper.postSemester(this.token, studyPlan.getId(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        Intent intent = new Intent(getActivity().getApplicationContext(), SemestersActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("studyPlan", studyPlan);
                        startActivity(intent);
                        getActivity().finish();
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
