package com.necisstudio.highlightgoal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.necisstudio.highlightgoal.holder.DetailPlayerHolder;
import com.necisstudio.highlightgoal.item.DetailPlayerItem;
import com.necisstudio.highlightgoal.manage.Cache_String;
import com.necisstudio.highlightgoal.R;

import java.util.List;

/**
 * Created by Jarod on 2015-10-21.
 */
public class DetailPlayerAdapter extends RecyclerView.Adapter<DetailPlayerHolder> {
    private List<DetailPlayerItem> feedItemList;
    private Context mContext;
    DetailPlayerHolder feedListRowHolder;
    Cache_String cache_string;
    String date1;

    public DetailPlayerAdapter(Context context, List<DetailPlayerItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        cache_string = new Cache_String();

        cache_string.sharedpreferences = context.getSharedPreferences(cache_string.MyPREFERENCES,
                context.getApplicationContext().MODE_PRIVATE);

    }


    @Override
    public DetailPlayerHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        View header = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_detailplayer, viewGroup, false);
        DetailPlayerHolder mh = new DetailPlayerHolder(header);
        return mh;
    }

    @Override
    public void onBindViewHolder(final DetailPlayerHolder feedListRowHolder, final int i) {
        try {
            this.feedListRowHolder = feedListRowHolder;
            final DetailPlayerItem feedItem = feedItemList.get(i);

            feedListRowHolder.txtteam.setText(Html.fromHtml(feedItem.getTeam()));
            feedListRowHolder.txtkeuangan.setText(Html.fromHtml(feedItem.getKeuangan()));
            feedListRowHolder.txtnumber.setText(Html.fromHtml(feedItem.getNumber()));
            feedListRowHolder.txtposition.setText(Html.fromHtml(feedItem.getPosition()));


            feedListRowHolder.txtnegara.setText(Html.fromHtml(feedItem.getNegara()));
            feedListRowHolder.txtdob.setText(Html.fromHtml(feedItem.getDob()));
            feedListRowHolder.txtcontract.setText(Html.fromHtml(feedItem.getContract()));

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

