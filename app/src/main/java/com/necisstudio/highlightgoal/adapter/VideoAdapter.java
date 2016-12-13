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
import com.necisstudio.highlightgoal.holder.VideoHolder;
import com.necisstudio.highlightgoal.item.VideoItem;
import com.necisstudio.highlightgoal.manage.Cache_String;
import com.necisstudio.highlightgoal.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Jarod on 2015-10-21.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoHolder> {
    private List<VideoItem> feedItemList;
    private Context mContext;
    Cache_String cache_string;

    public VideoAdapter(Context context, List<VideoItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        cache_string = new Cache_String();

        cache_string.sharedpreferences = context.getSharedPreferences(cache_string.MyPREFERENCES,
                context.getApplicationContext().MODE_PRIVATE);


    }


    @Override
    public VideoHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        View header = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_video, viewGroup, false);
        VideoHolder mh = new VideoHolder(header);
        return mh;
    }

    @Override
    public void onBindViewHolder(final VideoHolder feedListRowHolder, final int i) {
        try {
            feedListRowHolder.txtjudul.setText(Html.fromHtml(feedItemList.get(i).getTitle()));
            if(feedItemList.get(i).getAuthor().equals("")){
                feedListRowHolder.txtauthor.setText("Published at Youtube");
            }else{
                feedListRowHolder.txtauthor.setText("Published at "+feedItemList.get(i).getAuthor());
            }
            feedListRowHolder.date.setText(getDate(feedItemList.get(i).getDate()));
            Glide.with(mContext).load(feedItemList.get(i).getUrlimage()).into(feedListRowHolder.video);
        } catch (Exception e) {
            Log.e("error adapter", e.getMessage());
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

    public void add(VideoItem item, int position) {
        feedItemList.add(position,item);
        notifyItemInserted(position);
    }

    private String getDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date value = null;
        try {
            value = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");
        dateFormatter.setTimeZone(TimeZone.getDefault());
        String dt = dateFormatter.format(value);

        SimpleDateFormat dateFormatter2 = new SimpleDateFormat("HH:mm aa");
        dateFormatter2.setTimeZone(TimeZone.getDefault());
        String dt2 = dateFormatter2.format(value);
        return dt+" at "+dt2;
    }


}

