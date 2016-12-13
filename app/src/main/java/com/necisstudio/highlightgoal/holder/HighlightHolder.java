package com.necisstudio.highlightgoal.holder;

import android.media.Image;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.necisstudio.highlightgoal.R;

/**
 * Created by Jarod on 2015-10-21.
 */
public class HighlightHolder extends RecyclerView.ViewHolder {
    public TextView txtHome,txtAway,txtHomeScore,txtAwayScore,txtdate,txtdate2;
    public ImageView imghome,imgaway;
    public CardView cardView;
    public RelativeLayout header;
    public HighlightHolder(View view) {
        super(view);
        this.cardView = (CardView)view.findViewById(R.id.cardview);
        this.txtHome = (TextView) view.findViewById(R.id.textViewhome);
        this.txtAway = (TextView) view.findViewById(R.id.textViewaway);
        this.txtAwayScore = (TextView) view.findViewById(R.id.textViewscoreaway);
        this.txtHomeScore = (TextView) view.findViewById(R.id.textViewscorehome);
        this.txtdate = (TextView)view.findViewById(R.id.date1);
        this.txtdate2 = (TextView)view.findViewById(R.id.date2);

        this.header = (RelativeLayout)view.findViewById(R.id.header);
        this.imgaway = (ImageView) view.findViewById(R.id.imageViewaway);
        this.imghome = (ImageView) view.findViewById(R.id.imageViewhome);
    }
}
