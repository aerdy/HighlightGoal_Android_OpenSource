package com.necisstudio.highlightgoal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.necisstudio.highlightgoal.holder.TeamChampionsHolder;
import com.necisstudio.highlightgoal.item.TeamChampion;
import com.necisstudio.highlightgoal.manage.Cache_String;
import com.necisstudio.highlightgoal.R;

import java.util.List;

/**
 * Created by Jarod on 2015-10-21.
 */
public class TeamChampionsAdapter extends RecyclerView.Adapter<TeamChampionsHolder> {
    private List<TeamChampion> feedItemList;
    private Context mContext;
    TeamChampionsHolder feedListRowHolder;
    Cache_String cache_string;
    String date1;

    public TeamChampionsAdapter(Context context, List<TeamChampion> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        cache_string = new Cache_String();

        cache_string.sharedpreferences = context.getSharedPreferences(cache_string.MyPREFERENCES,
                context.getApplicationContext().MODE_PRIVATE);

    }


    @Override
    public TeamChampionsHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        View header = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_teamchampions, viewGroup, false);
        TeamChampionsHolder mh = new TeamChampionsHolder(header);
        return mh;
    }

    @Override
    public void onBindViewHolder(final TeamChampionsHolder feedListRowHolder, final int i) {
        try {
            this.feedListRowHolder = feedListRowHolder;
            final TeamChampion feedItem = feedItemList.get(i);

            feedListRowHolder.txtteam.setText(Html.fromHtml(feedItem.getTeam()));
            feedListRowHolder.txtkeuangan.setText(Html.fromHtml(feedItem.getKeuangan()));
            Glide.with(mContext)
                    .load(feedItem.getUrlteam())
                    .error(R.mipmap.ic_ball_gray)
                    .into(feedListRowHolder.imgteam);
//            feedListRowHolder.cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, ActivityPlayer.class);
//                    Bundle extras = new Bundle();
//                    extras.putString("idteam", feedItemList.get(i).getIdteam());
//                    intent.putExtras(extras);
//                    mContext.startActivity(intent);
//                }
//            });
//            if (i <=3) {
//                //genap
//                feedListRowHolder.utama.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_blue_dark));
//
//                feedListRowHolder.txtteam.setTextColor(mContext.getResources().getColor(android.R.color.white));
//                feedListRowHolder.txtdifferent.setTextColor(mContext.getResources().getColor(android.R.color.white));
//                feedListRowHolder.txtplayer.setTextColor(mContext.getResources().getColor(android.R.color.white));
//                feedListRowHolder.txtpoint.setTextColor(mContext.getResources().getColor(android.R.color.white));
//            }else if (i == feedItemList.size()-2){
//                feedListRowHolder.utama.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
//                feedListRowHolder.txtteam.setTextColor(mContext.getResources().getColor(android.R.color.white));
//                feedListRowHolder.txtdifferent.setTextColor(mContext.getResources().getColor(android.R.color.white));
//                feedListRowHolder.txtplayer.setTextColor(mContext.getResources().getColor(android.R.color.white));
//                feedListRowHolder.txtpoint.setTextColor(mContext.getResources().getColor(android.R.color.white));
//            }else if( i == feedItemList.size()-1){
//                feedListRowHolder.utama.setBackgroundColor(mContext.getResources().getColor(android.R.color.holo_red_dark));
//                feedListRowHolder.txtteam.setTextColor(mContext.getResources().getColor(android.R.color.white));
//                feedListRowHolder.txtdifferent.setTextColor(mContext.getResources().getColor(android.R.color.white));
//                feedListRowHolder.txtplayer.setTextColor(mContext.getResources().getColor(android.R.color.white));
//                feedListRowHolder.txtpoint.setTextColor(mContext.getResources().getColor(android.R.color.white));
//            }
        } catch (Exception e) {
            Log.e("error adapter", e.getMessage());
        }


    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public void removeAt(int position) {
        feedItemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, feedItemList.size());
    }


}

