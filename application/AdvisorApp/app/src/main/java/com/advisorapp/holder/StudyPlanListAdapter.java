package com.advisorapp.holder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.advisorapp.R;
import com.advisorapp.model.StudyPlan;

import java.util.List;

/**
 * Created by adric on 09/06/2016.
 */
public class StudyPlanListAdapter extends RecyclerView.Adapter<StudyPlanListHolder>{

    List<StudyPlan> list;

    //ajouter un constructeur prenant en entrée une liste
    public StudyPlanListAdapter(List<StudyPlan> list) {
        this.list = list;
    }

    //cette fonction permet de créer les viewHolder
    //et par la même indiquer la vue à inflater (à partir des layout xml)
    @Override
    public StudyPlanListHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_studyplan,viewGroup,false);
        return new StudyPlanListHolder(view);
    }

    @Override
    public void onBindViewHolder(final StudyPlanListHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Test", holder.itemView.toString());
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

}
