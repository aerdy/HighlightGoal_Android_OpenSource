package com.necisstudio.highlightgoal.utils.klasemen;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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
import com.necisstudio.highlightgoal.adapter.TeamChampionsAdapter;
import com.necisstudio.highlightgoal.item.TeamChampion;
import com.necisstudio.highlightgoal.manage.Cache_String;
import com.necisstudio.highlightgoal.network.xxmdk.XXmdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jarod on 2015-10-21.
 */
public class TeamEuropaUtils {
    int a = 0;
    Activity activity;
    Cache_String cache_string;

    public TeamEuropaUtils(final Activity activity, final List<TeamChampion> listitem, final TeamChampionsAdapter adapter, final ProgressBar progressBar) {
        cache_string = new Cache_String();

        cache_string.sharedpreferences = activity.getSharedPreferences(cache_string.MyPREFERENCES,
                activity.getApplicationContext().MODE_PRIVATE);
        this.activity = activity;
        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = new XXmdk().kelasligaeuropa();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    listitem.clear();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    for(int a = 0 ; a<jsonArray.length();a++){
                        JSONObject jsonObject1 = jsonArray.getJSONObject(a);
                        String team = jsonObject1.getString("clubname");
                        String player = jsonObject1.getString("play");
                        String point = jsonObject1.getString("point");
                        String different = jsonObject1.getString("gd");
                        String icon = jsonObject1.getString("icon");

                        TeamChampion item = new TeamChampion();
                        item.setTeam(team);
                        item.setUrlteam(icon);
                        item.setJumlah("");
                        item.setKeuangan("");
                        item.setIdteam("");
                        listitem.add(item);
                    }

                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    Log.d("Error ", e.getMessage());
                    progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
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
