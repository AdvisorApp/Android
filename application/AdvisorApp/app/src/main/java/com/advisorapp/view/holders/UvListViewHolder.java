package com.advisorapp.view.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.advisorapp.R;
import com.advisorapp.model.Uv;

/**
 * Created by Alexis on 09/06/2016.
 */
public class UvListViewHolder extends RecyclerView.ViewHolder{

    private TextView nameTextView;
   // private ImageView imageView;

    public UvListViewHolder(View itemView) {
        super(itemView);

        this.nameTextView = (TextView) itemView.findViewById(R.id.name);
        // imageView = (ImageView) itemView.findViewById(R.id.image);
    }

    public void bind(Uv uv){
        this.nameTextView.setText(uv.getName());
       // Picasso.with(imageView.getContext()).load(myObject.getImageUrl()).centerCrop().fit().into(imageView);
    }
}