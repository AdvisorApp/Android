package com.advisorapp.view.semesters;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.advisorapp.R;
import com.advisorapp.model.Location;
import com.advisorapp.model.Uv;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;

/**
 * Created by Steeve on 09/06/2016.
 */
public class SemestersFragment extends Fragment{
    private AndroidTreeView tView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_default, null, false);
        final ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.container);
        rootView.findViewById(R.id.status_bar).setVisibility(View.GONE);

        final TreeNode root = TreeNode.root();

        TreeNode myProfile = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.drawable.ic_semester, "Semester 1")).setViewHolder(new SemesterHolder(getActivity()));
        TreeNode bruce = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.drawable.ic_semester, "Semester 2")).setViewHolder(new SemesterHolder(getActivity()));
        TreeNode clark = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.drawable.ic_semester, "Semester 3")).setViewHolder(new SemesterHolder(getActivity()));
        TreeNode barry = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.drawable.ic_semester, "Semester 4")).setViewHolder(new SemesterHolder(getActivity()));
        addProfileData(myProfile);
        addProfileData(clark);
        addProfileData(bruce);
        addProfileData(barry);
        root.addChildren(myProfile, bruce, barry, clark);

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
            UVHolder.UvItem item = (UVHolder.UvItem) value;
            //TODO t'as l'uv en faisant item.uv
        }
    };

    private TreeNode.TreeNodeLongClickListener nodeLongClickListener = new TreeNode.TreeNodeLongClickListener() {
        @Override
        public boolean onLongClick(TreeNode node, Object value) {
            if (value instanceof  UVHolder){
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
                                    //todo intent pour new semester
                                    break;
                                case 1:
                                    Log.d("DEBUG", "TEST");
                                    //todo ADD UV
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
}
