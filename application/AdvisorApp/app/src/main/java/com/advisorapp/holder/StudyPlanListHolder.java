package com.advisorapp.holder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.advisorapp.R;
import com.advisorapp.model.StudyPlan;

/**
 * Created by adric on 09/06/2016.
 */
public class StudyPlanListHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    private static int DELETE_MENU_ID = 0;
    private TextView textView;

    //itemView est la vue correspondante à 1 cellule
    public StudyPlanListHolder(View itemView) {
        super(itemView);
        //c'est ici que l'on fait nos findView

        textView = (TextView) itemView.findViewById(R.id.studyplan_name);
        itemView.setOnCreateContextMenuListener(this);
    }

    //puis ajouter une fonction pour remplir la cellule en fonction d'un StudyPlan
    public void bind(StudyPlan myObject){
        textView.setText(myObject.getName());
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(DELETE_MENU_ID, v.getId(), 0, "Delete");//groupId, itemId, order, title
        menu.getItem(DELETE_MENU_ID).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("dee", textView.getText().toString());
                return true;
            }
        });
    }
}
