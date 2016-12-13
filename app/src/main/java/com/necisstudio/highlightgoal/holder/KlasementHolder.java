package com.necisstudio.highlightgoal.holder;

import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.necisstudio.highlightgoal.R;

/**
 * Created by Jarod on 2015-10-21.
 */
public class KlasementHolder extends RecyclerView.ViewHolder {
    public TextView txtteam,txtplayer,txtdifferent,txtpoint;
    public LinearLayout top,utama;
    public KlasementHolder(View view) {
        super(view);
        this.txtteam = (TextView)view.findViewById(R.id.team);
        this.txtplayer = (TextView)view.findViewById(R.id.player);
        this.txtdifferent = (TextView)view.findViewById(R.id.different);
        this.txtpoint = (TextView)view.findViewById(R.id.point);

        this.top = (LinearLayout)view.findViewById(R.id.top);
        this.utama = (LinearLayout)view.findViewById(R.id.utama);
    }
}
