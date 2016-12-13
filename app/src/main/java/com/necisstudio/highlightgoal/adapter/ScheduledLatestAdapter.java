package com.necisstudio.highlightgoal.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.necisstudio.highlightgoal.R;
import com.necisstudio.highlightgoal.holder.SchaduleLatestHolder;
import com.necisstudio.highlightgoal.item.ScheduleItem;
import com.necisstudio.highlightgoal.item.VideoItem;
import com.necisstudio.highlightgoal.manage.Cache_String;
import com.necisstudio.highlightgoal.manipulation.RecyclerItemClickListener;
import com.necisstudio.highlightgoal.network.xxmdk.XXmdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Jarod on 2015-10-21.
 */
public class ScheduledLatestAdapter extends RecyclerView.Adapter<SchaduleLatestHolder> {
    private List<ScheduleItem> feedItemList;
    private Context mContext;
    SchaduleLatestHolder feedListRowHolder;
    Cache_String cache_string;
    String date1;
    Dialog dialog;
    String category1;
    public ScheduledLatestAdapter(Context context, List<ScheduleItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
        cache_string = new Cache_String();

        cache_string.sharedpreferences = context.getSharedPreferences(cache_string.MyPREFERENCES,
                context.getApplicationContext().MODE_PRIVATE);

    }


    @Override
    public SchaduleLatestHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        View header = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_schedulelatest, viewGroup, false);
        SchaduleLatestHolder mh = new SchaduleLatestHolder(header);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SchaduleLatestHolder feedListRowHolder, final int i) {
        date1 = cache_string.sharedpreferences.getString(
                cache_string.datesave, null);
        try {
            this.feedListRowHolder = feedListRowHolder;
            final ScheduleItem feedItem = feedItemList.get(i);
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
            feedListRowHolder.txtdate.setText(getDate(date));
            feedListRowHolder.txtdate2.setText(getDate(date));
//            String imgSrcHtml = "<html><center><img  width='75' src='" + feedItemListurl.get(i).getUrlhome() + "' /></center></html>";
//            feedListRowHolder.imghome.loadData(imgSrcHtml, "text/html", "UTF-8");

//            feedListRowHolder.cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String a = feedItem.getHometeam();
//                    a = a.replace(" ", "%20");
//                    a = a.replace("FC", "");
//                    a = a.replace("-", "");
//                    a = a.replace("SK", "");
//                    a = a.replace("Turin", "");
//                    a = a.replace("FF", "");
//                    a = a.replace("CF", "");
//                    a = a.replace("St.", "");
//                    a = a.replace("Club", "");
//                    a = a.replace("F.C.", "");
//
//                    String b = feedItem.getAwayteam();
//                    b = b.replace(" ", "%20");
//                    b = b.replace("FC", "");
//                    b = b.replace("-", "");
//                    b = b.replace("SK", "");
//                    b = b.replace("Turin", "");
//                    b = b.replace("FF", "");
//                    b = b.replace("CF", "");
//                    b = b.replace("St.", "");
//                    b = b.replace("Club", "");
//                    b = b.replace("F.C.", "");
//                    dialogvideo(a + "vs" + b);
//                }
//            });


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

    public void add(ScheduleItem item, int position) {
        feedItemList.add(position,item);
        notifyItemInserted(position);
    }
    void dialogvideo(String data) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View prompts = li.inflate(R.layout.fragment_video, null);
        dialog = new Dialog(mContext,
                R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(prompts);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        RecyclerView recyclerView = (RecyclerView) prompts.findViewById(R.id.recyclerView);
        final ProgressBar progressBar = (ProgressBar) prompts.findViewById(R.id.progressbar);
        final LinearLayout notfound = (LinearLayout)prompts.findViewById(R.id.notfound);
        final Button header = (Button)prompts.findViewById(R.id.header);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        final List<VideoItem> feedItemList = new ArrayList<>();
        final VideoAdapter adapterList = new VideoAdapter(mContext, feedItemList);
        recyclerView.setAdapter(adapterList);
        searchData(data, progressBar, feedItemList, adapterList, notfound);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    void searchData(String data, final ProgressBar progressBar, final List<VideoItem> feedItemList, final VideoAdapter adapter,final LinearLayout notfound) {
//        final Dialog dialog = new Dialog(mContext);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_progressbar);
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        dialog.setCancelable(false);
//        dialog.show();

        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url = new XXmdk().searchyoutube(data);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    for(int a = 0 ;a<jsonArray.length();a++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(a);
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("id");
                        String id = jsonObject2.getString("videoId");
                        JSONObject jsonObject3 = jsonObject1.getJSONObject("snippet");
                        String title = jsonObject3.getString("title");
                        String date = jsonObject3.getString("publishedAt");
                        String author = jsonObject3.getString("channelTitle");
                        JSONObject jsonObject4 = jsonObject3.getJSONObject("thumbnails");
                        JSONObject jsonObject5 = jsonObject4.getJSONObject("high");
                        String urliamge = jsonObject5.getString("url");

                        VideoItem item = new VideoItem();
                        item.setIdvideo(id);
                        item.setTitle(title);
                        item.setUrlimage(urliamge);
                        item.setAuthor(author);
                        item.setDate(date);
                        feedItemList.add(item);
                    }
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);

                    if(feedItemList.size() == 0){
                        notfound.setVisibility(View.VISIBLE);
                    }

                } catch (Exception e) {
                    progressBar.setVisibility(View.GONE);
                    dialog.dismiss();
                    Toast.makeText(mContext, "Sorry Video Not Found", Toast.LENGTH_SHORT).show();
                    Log.d("Error ", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        stringRequest.setRetryPolicy(new
                        DefaultRetryPolicy(
                        1000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );
        queue.add(stringRequest);
        queue.start();
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

