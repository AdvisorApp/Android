package com.advisorapp.view.holders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.advisorapp.R;
import com.advisorapp.model.Uv;
import com.advisorapp.view.activities.login.LoginActivity;

/**
 * Created by Alexis on 09/06/2016.
 */
public class UvListViewHolder extends RecyclerView.ViewHolder {

    private TextView nameTextView;

    public UvListViewHolder(View itemView) {
        super(itemView);

        this.nameTextView = (TextView) itemView.findViewById(R.id.name);
    }

    public void bind(Uv uv){
        this.nameTextView.setText(uv.getName());
    }

}