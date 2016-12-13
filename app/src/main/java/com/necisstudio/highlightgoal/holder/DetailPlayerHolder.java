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
public class DetailPlayerHolder extends RecyclerView.ViewHolder {
    public TextView txtteam,txtkeuangan,txtnumber,txtposition,txtnegara,txtdob,txtcontract;
    public DetailPlayerHolder(View view) {
        super(view);
        this.txtteam = (TextView)view.findViewById(R.id.team);
        this.txtkeuangan = (TextView)view.findViewById(R.id.keuangan);
        this.txtnumber = (TextView)view.findViewById(R.id.number);
        this.txtposition = (TextView)view.findViewById(R.id.posisi);


        this.txtnegara = (TextView)view.findViewById(R.id.negara);
        this.txtdob = (TextView)view.findViewById(R.id.dateOfBirth);
        this.txtcontract = (TextView)view.findViewById(R.id.contractUntil);
    }
}
