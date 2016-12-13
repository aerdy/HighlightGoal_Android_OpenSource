package com.necisstudio.highlightgoal.ui.fragment.highlight;

import android.content.SharedPreferences;
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
import com.necisstudio.highlightgoal.adapter.HighlightLatestAdapter;
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
public class HighlightLatestFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    SmoothProgressBar smoothProgressBar;
    ProgressBar progressBar;
    View view;
    Call<ResponseBody> call;
    List<HighlightItem> feedItemList, feedItemListurl;
    Cache_String cache_string;
    HighlightLatestAdapter adapterList1;
    HighlightAdapter adapterList2;
    String category;
    LinearLayout linEmpty;
    SwipeRefreshLayout refreshLayout;

    public static HighlightLatestFragment newInstance(String category) {
        Bundle args = new Bundle();
        args.putString("category", category);
        HighlightLatestFragment fragment = new HighlightLatestFragment();
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
        adapterList1 = new HighlightLatestAdapter(getActivity(), feedItemList, feedItemListurl);
        recyclerView.setAdapter(adapterList1);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!category.matches("")) {
            adapterList2 = new HighlightAdapter(getActivity(), feedItemList, feedItemListurl);
            recyclerView.setAdapter(adapterList2);
            getDataSearch();
        } else {
            getData();
        }
    }

    void initUi() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        smoothProgressBar = (SmoothProgressBar) view.findViewById(R.id.progressbarbottom);
        linEmpty = (LinearLayout) view.findViewById(R.id.linEmpty);
        refreshLayout.setOnRefreshListener(this);

    }

    private void getData() {
        if (call != null) {
            call.cancel();
        }
        RestClient.ApiInterface service = RestClient.getClient(getActivity());
        Call<ResponseBody> call = service.getData(new XXmdk().ligalatest());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        refreshLayout.setRefreshing(false);
                        String resRest = response.body().string();
                        // Log.e("data",resRest);
                        JSONObject jsonObject = new JSONObject(resRest);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int b = 0; b < jsonArray.length(); b++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(b);
                            String name = jsonObject1.getString("clubname");
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("result");
                            for (int c = 0; c < jsonArray1.length(); c++) {
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(c);
                                String date = jsonObject2.getString("date");
                                String hometeam = jsonObject2.getString("clubhome");
                                String awayteam = jsonObject2.getString("clubaway");

                                String goalhome = jsonObject2.getString("homescore");
                                String goalaway = jsonObject2.getString("awayscore");
                                String iconhome = jsonObject2.getString("homeicon");
                                String iconaway = jsonObject2.getString("awayicon");


                                HighlightItem item = new HighlightItem();
                                item.setClubcategory(setCategory(name));
                                item.setAwayscore(goalaway);
                                item.setHomescore(goalhome);
                                item.setAwayteam(awayteam);
                                item.setHometeam(hometeam);
                                item.setDate(date);
                                item.setLatest("1");
                                item.setUrlhome(iconhome);
                                item.setUrlaway(iconaway);
                                item.setLogo(setLogo(name));
                                adapterList1.add(item, c);

                                progressBar.setVisibility(View.GONE);
                                smoothProgressBar.setVisibility(View.GONE);
                                if (b == 0) {
                                    SharedPreferences.Editor editor = cache_string.sharedpreferences.edit();
                                    editor.putString(cache_string.datesave, date);
                                    editor.commit();
                                }

                            }


                        }


                    }

                    if (feedItemList.isEmpty() || feedItemList.size() == 0) {
                        linEmpty.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        smoothProgressBar.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    Log.e("error", e.getMessage());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
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


    private void getDataSearch() {
        if (call != null) {
            call.cancel();
        }
        RestClient.ApiInterface service = RestClient.getClient(getActivity());
        Call<ResponseBody> call = service.getData(new XXmdk().search(category));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        refreshLayout.setRefreshing(false);
                        String resRest = response.body().string();
                        // Log.e("data",resRest);
                        JSONObject jsonObject = new JSONObject(resRest);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int c = 0; c < jsonArray.length(); c++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(c);
                            String date = jsonObject2.getString("date");
                            String hometeam = jsonObject2.getString("clubhome");
                            String awayteam = jsonObject2.getString("clubaway");

                            String goalhome = jsonObject2.getString("homescore");
                            String goalaway = jsonObject2.getString("awayscore");
                            String iconhome = jsonObject2.getString("homeicon");
                            String iconaway = jsonObject2.getString("awayicon");

                            HighlightItem item = new HighlightItem();
                            item.setAwayscore(goalaway);
                            item.setHomescore(goalhome);
                            item.setAwayteam(awayteam);
                            item.setHometeam(hometeam);
                            item.setDate(date);
                            item.setLatest("1");
                            item.setUrlhome(iconhome);
                            item.setUrlaway(iconaway);
                            adapterList2.add(item, c);

                            progressBar.setVisibility(View.GONE);
                            smoothProgressBar.setVisibility(View.GONE);
                            if (c == 0) {
                                SharedPreferences.Editor editor = cache_string.sharedpreferences.edit();
                                editor.putString(cache_string.datesave, date);
                                editor.commit();
                            }

                        }
                        if (feedItemList.isEmpty() || feedItemList.size() == 0) {
                            linEmpty.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            smoothProgressBar.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    Log.e("error", e.getMessage());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
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

    private String setCategory(String data) {
        String url = null;
        switch (data) {
            case "champions":
                url = "Champions League";
                break;
            case "eropa":
                url = "Europa League";
                break;
            case "inggris":
                url = "Premier League";
                break;
            case "italia":
                url = "Seri A";
                break;
            case "spain":
                url = "BBVA League";
                break;
            case "jerman":
                url = "Bundesliga";
                break;
            case "france":
                url = "League 1";
                break;
        }
        return url;
    }

    private int setLogo(String data) {
        int logo = 0;
        switch (data) {
            case "champions":
                logo = R.mipmap.champion;
                break;
            case "eropa":
                logo = R.mipmap.europa;
                break;
            case "inggris":
                logo = R.mipmap.premier;
                break;
            case "italia":
                logo = R.mipmap.seria;
                break;
            case "spain":
                logo = R.mipmap.bbva;
                break;
            case "jerman":
                logo = R.mipmap.bundes;
                ;
                break;
            case "france":
                logo = R.mipmap.ligue;
                break;
        }
        return logo;
    }

    @Override
    public void onRefresh() {
        try {
            if (refreshLayout.isRefreshing() && feedItemList.isEmpty()) {
                getData();
            } else {
                refreshLayout.setRefreshing(false);
            }
        } catch (Exception e) {

        }
    }
}
