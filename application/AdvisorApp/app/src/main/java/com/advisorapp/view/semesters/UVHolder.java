package com.advisorapp.view.semesters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.advisorapp.R;
import com.advisorapp.model.Uv;
import com.github.johnkil.print.PrintView;
import com.unnamed.b.atv.model.TreeNode;

import java.util.Random;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class UVHolder extends TreeNode.BaseNodeViewHolder<UVHolder.UvItem> {



    public UVHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, UvItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.uv_node, null, false);


        TextView idUV = (TextView) view.findViewById(R.id.idUv);
        idUV.setText(value.uv.getRemoteId());

        TextView nameUV = (TextView) view.findViewById(R.id.uvName);
        nameUV.setText(value.uv.getName());

        TextView chsUV = (TextView) view.findViewById(R.id.uv_chs);
        chsUV.setText(String.format("%d",(long)value.uv.getChs()));

        PrintView likeView = (PrintView) view.findViewById(R.id.uvType);
        likeView.setBackgroundResource(value.icone);


//        likeView.setIconText(context.getResources().getString(value.icone));
//        likeView.setIconText(context.getString(like ? R.string.ic_thumbs_up : R.string.ic_thumbs_down)); //icone à droite
        return view;
    }

    @Override
    public void toggle(boolean active) {
    }


    public static class UvItem {
        public String name;
        public int icone;
        public Uv uv;

        public UvItem(Uv uv) {
            this.uv = uv;
            this.name = name;
            this.icone = R.drawable.ic_tp;
        }

        public UvItem(int icone, String name){
            this.name = name;
            this.icone = icone;
        }
        // rest will be hardcoded
    }

}

