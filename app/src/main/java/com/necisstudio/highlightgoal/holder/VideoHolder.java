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
public class VideoHolder extends RecyclerView.ViewHolder {
    public TextView txtjudul,txtauthor,date;
    public ImageView video;
    public VideoHolder(View view) {
        super(view);
        this.txtjudul = (TextView)view.findViewById(R.id.title);
        this.date = (TextView)view.findViewById(R.id.date);
        this.txtauthor = (TextView)view.findViewById(R.id.author);
        this.video = (ImageView)view.findViewById(R.id.video);
    }
}
