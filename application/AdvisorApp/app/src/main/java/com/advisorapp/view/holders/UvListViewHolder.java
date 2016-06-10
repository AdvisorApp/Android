package com.advisorapp.view.holders;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.advisorapp.R;
import com.advisorapp.model.Uv;
import com.advisorapp.view.activities.login.LoginActivity;
import com.github.johnkil.print.PrintView;

/**
 * Created by Alexis on 09/06/2016.
 */
public class UvListViewHolder extends RecyclerView.ViewHolder {

    private TextView nameTextView;
    private TextView idUV;
    private TextView chsUV;
    private RelativeLayout layout;

    private PrintView likeView;
    public UvListViewHolder(View itemView) {
        super(itemView);

        this.nameTextView = (TextView) itemView.findViewById(R.id.uvName);
        this.idUV = (TextView) itemView.findViewById(R.id.idUv);
        this.chsUV = (TextView) itemView.findViewById(R.id.uv_chs);

        likeView = (PrintView) itemView.findViewById(R.id.uvType);

        layout = (RelativeLayout)itemView.findViewById(R.id.layoutUV);

    }

    public void bind(Uv uv) {
        this.nameTextView.setText(uv.getName());
        this.idUV.setText(uv.getRemoteId());
        this.chsUV.setText(String.format("%d", (long) uv.getChs()));

        if (uv.getUvType().getType().equals("TP"))
            likeView.setBackgroundResource(R.drawable.ic_tp);
        else if (uv.getUvType().getType().equals("TD"))
            likeView.setBackgroundResource(R.drawable.ic_td);

        switch (uv.getLocation()) {
            case DEPARTMENT:
                layout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.department));
                break;
            case UNIVERSITY:
                layout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.university));
                break;
            case FACULTY:
                layout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.college));
                break;
//        }
        }
    }

}