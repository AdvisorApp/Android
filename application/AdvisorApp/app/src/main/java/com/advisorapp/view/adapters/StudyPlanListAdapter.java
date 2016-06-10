package com.advisorapp.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.advisorapp.R;
import com.advisorapp.model.StudyPlan;
import com.advisorapp.view.holders.StudyPlanListHolder;
import com.advisorapp.view.studyplanlist.StudyPlanListActivity;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

/**
 * Created by adric on 09/06/2016.
 */
public class StudyPlanListAdapter extends RecyclerView.Adapter<StudyPlanListHolder>{

    List<StudyPlan> list;
    StudyPlanListActivity activity;

    //ajouter un constructeur prenant en entrée une liste
    public StudyPlanListAdapter(List<StudyPlan> list, StudyPlanListActivity activity) {
        this.list = list;
        this.activity = activity;
    }

    //cette fonction permet de créer les viewHolder
    //et par la même indiquer la vue à inflater (à partir des layout xml)
    @Override
    public StudyPlanListHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_studyplan,viewGroup,false);
        StudyPlanListHolder holder = new StudyPlanListHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final StudyPlanListHolder holder, final int position) {
        final StudyPlan studyPlan = list.get(position);
        holder.bind(studyPlan);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new MaterialDialog.Builder(v.getContext())
                        .title("Remove StudyPlan")
                        .content("Do you want remove UV-" + studyPlan.getName() + " ?")
                        .positiveText("Yes, delete StudyPlan")
                        .negativeText("No")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                // TODO
                                activity.runDeleteStudyPlanTask(list.get(position).getId());
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

        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    activity.startSemesterActivity(studyPlan);
                }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}
