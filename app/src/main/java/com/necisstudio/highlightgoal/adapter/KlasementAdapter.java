package com.necisstudio.highlightgoal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.necisstudio.highlightgoal.holder.KlasementHolder;
import com.necisstudio.highlightgoal.item.KlasementItem;
import com.necisstudio.highlightgoal.manage.Cache_String;
import com.necisstudio.highlightgoal.R;

import java.util.List;

/**
 * Created by Jarod on 2015-10-21.
 */
public class KlasementAdapter extends RecyclerView.Adapter<KlasementHolder> {
    private List<KlasementItem> feedItemList;
    private Context mContext;
    KlasementHolder feedListRowHolder;
    Cache_String cache_string;
    String date1;
    int posisi;
    int posisiakhir1,posisiakhir2;
    public KlasementAdapter(Context context, List<KlasementItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        cache_string = new Cache_String();

        cache_string.sharedpreferences = context.getSharedPreferences(cache_string.MyPREFERENCES,
                context.getApplicationContext().MODE_PRIVATE);

    }


    @Override
    public KlasementHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        View header = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_kelasemen, viewGroup, false);
        KlasementHolder mh = new KlasementHolder(header);
        posisi = i;
        posisiakhir1 = feedItemList.size()-1;
        posisiakhir2 = feedItemList.size()-2;
        if (posisi ==0 || posisi == 1 || posisi ==2 || posisi == 3) {
            mh.utama.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_blue_dark));
            mh.txtteam.setTextColor(mContext.getResources().getColor(android.R.color.white));
            mh.txtdifferent.setTextColor(mContext.getResources().getColor(android.R.color.white));
            mh.txtplayer.setTextColor(mContext.getResources().getColor(android.R.color.white));
            mh.txtpoint.setTextColor(mContext.getResources().getColor(android.R.color.white));

        } else if (posisi ==4 || posisi == 5 ) {
            mh.utama.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_orange_dark));
            mh.txtteam.setTextColor(mContext.getResources().getColor(android.R.color.white));
            mh.txtdifferent.setTextColor(mContext.getResources().getColor(android.R.color.white));
            mh.txtplayer.setTextColor(mContext.getResources().getColor(android.R.color.white));
            mh.txtpoint.setTextColor(mContext.getResources().getColor(android.R.color.white));

        }
        else if (posisiakhir1 == posisi){
            mh.utama.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
            mh.txtteam.setTextColor(mContext.getResources().getColor(android.R.color.white));
            mh.txtdifferent.setTextColor(mContext.getResources().getColor(android.R.color.white));
            mh.txtplayer.setTextColor(mContext.getResources().getColor(android.R.color.white));
            mh.txtpoint.setTextColor(mContext.getResources().getColor(android.R.color.white));
        }else if(posisiakhir2 == posisi){
            mh.utama.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
            mh.txtteam.setTextColor(mContext.getResources().getColor(android.R.color.white));
            mh.txtdifferent.setTextColor(mContext.getResources().getColor(android.R.color.white));
            mh.txtplayer.setTextColor(mContext.getResources().getColor(android.R.color.white));
            mh.txtpoint.setTextColor(mContext.getResources().getColor(android.R.color.white));
        }
        return mh;
    }

    @Override
    public void onBindViewHolder(final KlasementHolder feedListRowHolder, final int i) {
        try {
            if (i == 0) {
                feedListRowHolder.top.setVisibility(View.VISIBLE);
            } else {
                feedListRowHolder.top.setVisibility(View.GONE);
            }
            feedListRowHolder.txtteam.setText(Html.fromHtml(feedItemList.get(i).getPosisi() + "." + feedItemList.get(i).getTeam()));
            feedListRowHolder.txtpoint.setText(Html.fromHtml(feedItemList.get(i).getPoint()));
            feedListRowHolder.txtdifferent.setText(Html.fromHtml(feedItemList.get(i).getDifferent()));
            feedListRowHolder.txtplayer.setText(Html.fromHtml(feedItemList.get(i).getPlayer()));

//            feedListRowHolder.utama.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, ActivityPlayer.class);
//                    Bundle extras = new Bundle();
//                    extras.putString("idteam", feedItemList.get(i).getIdtema());
//                    intent.putExtras(extras);
//                    mContext.startActivity(intent);
//                }
//            });

        } catch (Exception e) {
            Log.e("error adapter", e.getMessage());
        }


    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }
    @Override
    public int getItemViewType(int position) {
        return  position;
    }
    public void removeAt(int position) {
        feedItemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, feedItemList.size());
    }


}

