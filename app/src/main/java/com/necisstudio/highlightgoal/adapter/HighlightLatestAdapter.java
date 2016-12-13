package com.necisstudio.highlightgoal.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.necisstudio.highlightgoal.MainActivity;
import com.necisstudio.highlightgoal.R;
import com.necisstudio.highlightgoal.holder.HighlightHolder;
import com.necisstudio.highlightgoal.holder.HighlightLatestHolder;
import com.necisstudio.highlightgoal.item.HighlightItem;
import com.necisstudio.highlightgoal.manage.Cache_String;
import com.necisstudio.highlightgoal.ui.fragment.VideoFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Jarod on 2015-10-21.
 */
public class HighlightLatestAdapter extends RecyclerView.Adapter<HighlightLatestHolder> {
    private List<HighlightItem> feedItemList;
    private List<HighlightItem> feedItemListurl;
    private Activity mContext;
    HighlightLatestHolder feedListRowHolder;
    Cache_String cache_string;
    String date1,category1;
    Dialog dialog;
    public HighlightLatestAdapter(Activity context, List<HighlightItem> feedItemList, List<HighlightItem> feedItemListurl) {
        this.feedItemList = feedItemList;
        this.feedItemListurl = feedItemListurl;
        this.mContext = context;
        cache_string = new Cache_String();

        cache_string.sharedpreferences = context.getSharedPreferences(cache_string.MyPREFERENCES,
                context.getApplicationContext().MODE_PRIVATE);
    }


    @Override
    public HighlightLatestHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        View header = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_highlightlatest, viewGroup, false);
        HighlightLatestHolder mh = new HighlightLatestHolder(header);
        return mh;
    }

    @Override
    public void onBindViewHolder(final HighlightLatestHolder feedListRowHolder, final int i) {
        date1 = cache_string.sharedpreferences.getString(
                cache_string.datesave, null);
        try {
            this.feedListRowHolder = feedListRowHolder;
            final HighlightItem feedItem = feedItemList.get(i);
            String date = feedItem.getDate();
            if(i==0){
                category1 = feedItem.getClubcategory();
            }else{
                String categ = feedItem.getClubcategory();
                if(!category1.equals(categ)){
                    category1 = categ;
                    feedListRowHolder.txtName.setVisibility(View.VISIBLE);
                    feedListRowHolder.imgLogo.setVisibility(View.VISIBLE);
                }else{
                    feedListRowHolder.txtName.setVisibility(View.GONE);
                    feedListRowHolder.imgLogo.setVisibility(View.GONE);
                }
            }

            if (!date1.equals(date)) {
                SharedPreferences.Editor editor = cache_string.sharedpreferences.edit();
                editor.putString(cache_string.datesave, date);
                editor.commit();
                feedListRowHolder.header.setVisibility(View.VISIBLE);
            } else {
                feedListRowHolder.header.setVisibility(View.GONE);
            }
            Glide.with(mContext)
                    .load(feedItemList.get(i).getUrlhome())
                    .placeholder(R.mipmap.ic_ball_gray)
                    .error(R.mipmap.ic_ball_gray)
                    .into(feedListRowHolder.imghome);
            Glide.with(mContext)
                    .load(feedItemList.get(i).getUrlaway())
                    .placeholder(R.mipmap.ic_ball_gray)
                    .error(R.mipmap.ic_ball_gray)
                    .into(feedListRowHolder.imgaway);
            feedListRowHolder.imgLogo.setImageResource(feedItem.getLogo());
            feedListRowHolder.txtName.setText(Html.fromHtml(feedItem.getClubcategory()));
            feedListRowHolder.txtHome.setText(Html.fromHtml(feedItem.getHometeam()));
            feedListRowHolder.txtAway.setText(Html.fromHtml(feedItem.getAwayteam()));
            feedListRowHolder.txtAwayScore.setText(Html.fromHtml(feedItem.getAwayscore()));
            feedListRowHolder.txtHomeScore.setText(Html.fromHtml(feedItem.getHomescore()));
            feedListRowHolder.txtdate.setText(getDate(date));
            feedListRowHolder.txtdate2.setText(getDate(date));
//            String imgSrcHtml = "<html><center><img  width='75' src='" + feedItemListurl.get(i).getUrlhome() + "' /></center></html>";
//            feedListRowHolder.imghome.loadData(imgSrcHtml, "text/html", "UTF-8");

            feedListRowHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String a = feedItem.getHometeam();
                    a = a.replace(" ", "%20");
                    a = a.replace("FC", "");
                    a = a.replace("-", "");
                    a = a.replace("SK", "");
                    a = a.replace("Turin", "");
                    a = a.replace("FF", "");
                    a = a.replace("CF", "");
                    a = a.replace("St.", "");
                    a = a.replace("Club", "");
                    a = a.replace("F.C.", "");

                    String b = feedItem.getAwayteam();
                    b = b.replace(" ", "%20");
                    b = b.replace("FC", "");
                    b = b.replace("-", "");
                    b = b.replace("SK", "");
                    b = b.replace("Turin", "");
                    b = b.replace("FF", "");
                    b = b.replace("CF", "");
                    b = b.replace("St.", "");
                    b = b.replace("Club", "");
                    b = b.replace("F.C.", "");
//                    dialogvideo(a + "vs" + b);
                    VideoFragment.newInstance(a+"vs"+b).show(((MainActivity)mContext).getSupportFragmentManager(),"video");
                }
            });


        } catch (Exception e) {
            Log.e("error adapter",e.getMessage());
        }


    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public void remove(int position) {
        feedItemList.remove(position);
        notifyItemRemoved(position);
    }

    public void add(HighlightItem item, int position) {
        feedItemList.add(position,item);
        notifyItemInserted(position);
    }
    private String getDate(String dateString) {
        String timezoneID = TimeZone.getDefault().getID();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone(timezoneID));
        Date value = null;
        try {
            value = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        dateFormatter.setTimeZone(TimeZone.getTimeZone(timezoneID));
        String dt = dateFormatter.format(value);

        SimpleDateFormat dateFormatter2 = new SimpleDateFormat("HH:mm aa");
        dateFormatter2.setTimeZone(TimeZone.getTimeZone(timezoneID));;
        String dt2 = dateFormatter2.format(value);
        return dt+" at "+dt2;
    }
}

