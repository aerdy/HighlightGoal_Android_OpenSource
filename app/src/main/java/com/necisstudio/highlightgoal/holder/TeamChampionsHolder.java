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
public class TeamChampionsHolder extends RecyclerView.ViewHolder {
    public TextView txtteam,txtkeuangan;
    public ImageView imgteam;
    public CardView cardView;
    public TeamChampionsHolder(View view) {
        super(view);
        this.txtteam = (TextView)view.findViewById(R.id.team);
        this.txtkeuangan = (TextView)view.findViewById(R.id.keuangan);

        this.imgteam = (ImageView)view.findViewById(R.id.imageViewteam);
        this.cardView = (CardView)view.findViewById(R.id.cardview);
    }
}
