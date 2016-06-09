package com.advisorapp.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.advisorapp.R;
import com.advisorapp.model.Uv;
import com.advisorapp.view.holders.UvListViewHolder;

import java.util.List;

/**
 * Created by Alexis on 09/06/2016.
*/
public class UvListAdapter extends RecyclerView.Adapter<UvListViewHolder> {

    List<Uv> list;

    public UvListAdapter(List<Uv> list) {
        this.list = list;
    }

    @Override
    public UvListViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.uvlist_cards,viewGroup,false);
        return new UvListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UvListViewHolder myViewHolder, int position) {
        Uv uv = list.get(position);
        myViewHolder.bind(uv);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}
