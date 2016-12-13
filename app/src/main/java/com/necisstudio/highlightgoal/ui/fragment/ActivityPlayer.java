package com.necisstudio.highlightgoal.ui.fragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.necisstudio.highlightgoal.R;
import com.necisstudio.highlightgoal.adapter.DetailPlayerAdapter;
import com.necisstudio.highlightgoal.item.DetailPlayerItem;
import com.necisstudio.highlightgoal.manage.Cache_String;
import com.necisstudio.highlightgoal.utils.DetailPlayerUtils;
import com.necisstudio.highlightgoal.network.xxmdk.XXmdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by Jarod on 2015-10-23.
 */
public class ActivityPlayer extends AppCompatActivity {
    LinearLayout up,down;
    ImageView imageup;
    RecyclerView recyclerView;
    SmoothProgressBar smoothProgressBar;
    ProgressBar progressBar;
    String idteam;
    TextView kiper,DML,DMR,DMC1,DMC2,CMC1,CMC2,CML,CMR,CF1,CF2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        uiInit();
        Bundle extras = getIntent().getExtras();
        idteam = extras.getString("idteam");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detail Player");
        toolbar.setLogo(R.mipmap.ic_back);
        setSupportActionBar(toolbar);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               dialogdetail();
            }
        });

        getPosition();

    }

    void uiInit(){
        imageup = (ImageView)findViewById(R.id.imageup);
        up = (LinearLayout)findViewById(R.id.up);
        down = (LinearLayout)findViewById(R.id.down);
        kiper = (TextView)findViewById(R.id.kiper);
        DML = (TextView)findViewById(R.id.DML);
        DMC1 = (TextView)findViewById(R.id.DMC1);
        DMC2 = (TextView)findViewById(R.id.DMC2);
        DMR = (TextView)findViewById(R.id.DMR);

        CML = (TextView)findViewById(R.id.CML);
        CMC1 = (TextView)findViewById(R.id.CMC1);
        CMC2 = (TextView)findViewById(R.id.CMC2);
        CMR = (TextView)findViewById(R.id.CMR);

        CF1 = (TextView)findViewById(R.id.CF1);
        CF2 = (TextView)findViewById(R.id.CF2);

    }

    void dialogdetail() {
        LayoutInflater li = LayoutInflater.from(this);
        View prompts = li.inflate(R.layout.dialog_detailplayer, null);
        final Dialog dialog = new Dialog(this,
                R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(prompts);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();

        recyclerView = (RecyclerView) prompts.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar)prompts.findViewById(R.id.progressbar);
        smoothProgressBar = (SmoothProgressBar)prompts.findViewById(R.id.progressbarbottom);
        Button header = (Button)prompts.findViewById(R.id.header);

        Cache_String cache_string = new Cache_String();

        cache_string.sharedpreferences = getSharedPreferences(cache_string.MyPREFERENCES,
                getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = cache_string.sharedpreferences.edit();
        editor.putString(cache_string.datesave, "");
        editor.commit();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final List<DetailPlayerItem> feedItemList = new ArrayList<>();
        final DetailPlayerAdapter adapterList = new DetailPlayerAdapter(this, feedItemList);
        recyclerView.setAdapter(adapterList);

        new DetailPlayerUtils(idteam,this,feedItemList,adapterList,progressBar);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    void getPosition(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = new XXmdk().player(idteam);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("players");
                    String count = jsonObject.getString("count");
                    for(int a = 0 ; a<jsonArray.length();a++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(a);
                        String team = jsonObject1.getString("name");
                        String mareket = jsonObject1.getString("marketValue");
                        String jerseyNumber = jsonObject1.getString("jerseyNumber");
                        String position = jsonObject1.getString("position");

                        if(kiper.getText().equals("")){
                            if(position.equals("Keeper")){
                                kiper.setText(jerseyNumber);
                            }
                        }

                        if(DML.getText().equals("")){
                            if(position.equals("Left-Back")){
                                DML.setText(jerseyNumber);
                            }
                        }

                        if(!DMC1.getText().equals("")){
                            if(DMC2.getText().equals("")){
                                if(position.equals("Centre Back")||position.equals("Defensive Midfield")){
                                    DMC2.setText(jerseyNumber);
                                }
                            }
                        }

                        if(DMC1.getText().equals("")){
                            if(position.equals("Centre Back")){
                                DMC1.setText(jerseyNumber);
                            }
                        }
                        if(DMR.getText().equals("")){
                            if(position.equals("Right-Back")){
                                DMR.setText(jerseyNumber);
                            }
                        }



                        if(CML.getText().equals("")){
                            if(position.equals("Left Wing")){
                                CML.setText(jerseyNumber);
                            }
                        }

                        if(!CMC1.getText().equals("")){
                            if(CMC2.getText().equals("")){
                                if(position.equals("Central Midfield")||position.equals("Defensive Midfield")){
                                    CMC2.setText(jerseyNumber);
                                }
                            }
                        }

                        if(CMC1.getText().equals("")){
                            if(position.equals("Central Midfield")){
                                CMC1.setText(jerseyNumber);
                            }
                        }
                        if(CMR.getText().equals("")){
                            if(position.equals("Right Wing")){
                                CMR.setText(jerseyNumber);
                            }
                        }


                        if(CF1.getText().equals("")){
                            if(position.equals("Centre Forward")){
                                CF1.setText(jerseyNumber);
                            }
                        }
                        if(CF2.getText().equals("")){
                            if(position.equals("Secondary Striker") || position.equals("Attacking Midfield")){
                                CF2.setText(jerseyNumber);
                            }
                        }
                    }

                } catch (Exception e) {
                    Log.e("error",e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                } catch (UnsupportedEncodingException e) {
                    parsed = new String(response.data);
                }
                return Response.success(parsed, parseIgnoreCacheHeaders(response));
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Auth-Token", "3c81f6b93eb04ee9acb2540d55406895");
                return params;
            }

        };
        stringRequest.setRetryPolicy(new
                        DefaultRetryPolicy(
                        1000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );
        queue.add(stringRequest);
        queue.start();
    }
    public static Cache.Entry parseIgnoreCacheHeaders(NetworkResponse response) {
        long now = System.currentTimeMillis();

        Map<String, String> headers = response.headers;
        long serverDate = 0;
        String serverEtag = null;
        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }

        serverEtag = headers.get("ETag");

        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
        final long softExpire = now + cacheHitButRefreshed;
        final long ttl = now + cacheExpired;

        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = ttl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;

        return entry;
    }
}
