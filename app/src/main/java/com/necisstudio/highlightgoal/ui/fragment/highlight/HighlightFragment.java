package com.necisstudio.highlightgoal.ui.fragment.highlight;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.necisstudio.highlightgoal.R;
import com.necisstudio.highlightgoal.adapter.HighlightAdapter;
import com.necisstudio.highlightgoal.item.HighlightItem;
import com.necisstudio.highlightgoal.manage.ApplicationConfig;
import com.necisstudio.highlightgoal.manage.Cache_String;
import com.necisstudio.highlightgoal.network.RestClient;
import com.necisstudio.highlightgoal.network.xxmdk.XXmdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jarod on 2015-10-21.
 */
public class HighlightFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    SmoothProgressBar smoothProgressBar;
    ProgressBar progressBar;
    View view;
    Call<ResponseBody> call;
    List<HighlightItem> feedItemList, feedItemListurl;
    Cache_String cache_string;
    HighlightAdapter adapterList;
    String category;
    LinearLayout linEmpty;
    SwipeRefreshLayout refreshLayout;
    public static HighlightFragment newInstance(String category) {
        Bundle args = new Bundle();
        args.putString("category", category);
        HighlightFragment fragment = new HighlightFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = getArguments().getString("category");
        cache_string = new Cache_String();
        cache_string.sharedpreferences = getActivity().getSharedPreferences(cache_string.MyPREFERENCES,
                getActivity().getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = cache_string.sharedpreferences.edit();
        editor.putString(cache_string.datesave, "");
        editor.commit();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initUi();
        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));
        recyclerView.getItemAnimator().setAddDuration(700);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        feedItemList = new ArrayList<>();
        feedItemListurl = new ArrayList<>();
        adapterList = new HighlightAdapter(getActivity(), feedItemList, feedItemListurl);
        recyclerView.setAdapter(adapterList);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getData();
    }

    void initUi() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        smoothProgressBar = (SmoothProgressBar) view.findViewById(R.id.progressbarbottom);
        linEmpty = (LinearLayout) view.findViewById(R.id.linEmpty);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(this);
    }

    private void getData() {
        if (call != null) {
            call.cancel();
        }
        String url = setCategory();
        RestClient.ApiInterface service = RestClient.getClient(getActivity());
        Call<ResponseBody> call = service.getData(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        refreshLayout.setRefreshing(false);
                        final String resRest = response.body().string();
                        final JSONObject jsonObject = new JSONObject(resRest);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int b = 0; b < jsonArray.length(); b++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(b);
                            String date = jsonObject1.getString("date");

                            String hometeam = jsonObject1.getString("clubhome");
                            String awayteam = jsonObject1.getString("clubaway");
                            String id = jsonObject1.getString("id");
                            String goalhome = jsonObject1.getString("homescore");
                            String goalaway = jsonObject1.getString("awayscore");
                            String iconhome = jsonObject1.getString("homeicon");
                            String iconaway = jsonObject1.getString("awayicon");

                            HighlightItem item = new HighlightItem();
                            item.setAwayscore(goalaway);
                            item.setHomescore(goalhome);
                            item.setAwayteam(awayteam);
                            item.setHometeam(hometeam);
                            item.setId(id);
                            item.setLatest("0");
                            item.setClubcategory(category);
                            item.setDate(date);
                            item.setUrlhome(iconhome);
                            item.setUrlaway(iconaway);
                            adapterList.add(item, b);
                            if (b == 0) {
                                progressBar.setVisibility(View.GONE);
                                smoothProgressBar.setVisibility(View.GONE);
                                SharedPreferences.Editor editor = cache_string.sharedpreferences.edit();
                                editor.putString(cache_string.datesave, date);
                                editor.commit();
                            }
                        }
                        if (feedItemList.isEmpty()) {
                            linEmpty.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            smoothProgressBar.setVisibility(View.GONE);
                        }

                    }

                } catch (Exception e) {
                    if (feedItemList.isEmpty() || feedItemList.size() == 0) {
                        linEmpty.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        smoothProgressBar.setVisibility(View.GONE);
                    }
                    Log.e("error", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    if (feedItemList.isEmpty() || feedItemList.size() == 0) {
                        linEmpty.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        smoothProgressBar.setVisibility(View.GONE);
                    }
                    refreshLayout.setRefreshing(false);
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(getActivity(), "Connection Timeout", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Check your Connection", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception f) {

                }
            }
        });
    }

    private String setCategory() {
        String url = null;
        XXmdk xXmdk = new XXmdk();
        switch (category) {
            case "champions":
                url = xXmdk.ligachampion();
                break;
            case "europa":
                url = xXmdk.ligaeuropa();
                break;
            case "inggris":
                url = xXmdk.ligaingris();
                break;
            case "italia":
                url = xXmdk.ligaseria();
                break;
            case "spain":
                url = xXmdk.ligaspanyol();
                break;
            case "jerman":
                url = xXmdk.ligajerman();
                break;
            case "france":
                url = xXmdk.ligafrance();
                break;
        }
        return url;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            call.cancel();
        } catch (Exception e) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            call.cancel();
        } catch (Exception e) {

        }
    }
    @Override
    public void onRefresh() {
        try {
            if (refreshLayout.isRefreshing() && feedItemList.isEmpty()) {
                getData();
            }else {
                refreshLayout.setRefreshing(false);
            }
        } catch (Exception e) {

        }

    }
}
