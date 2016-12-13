package com.necisstudio.highlightgoal.ui.fragment.klasemen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.necisstudio.highlightgoal.R;
import com.necisstudio.highlightgoal.adapter.KlasementAdapter;
import com.necisstudio.highlightgoal.item.KlasementItem;
import com.necisstudio.highlightgoal.manage.Cache_String;
import com.necisstudio.highlightgoal.network.RestClient;
import com.necisstudio.highlightgoal.network.xxmdk.XXmdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
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
public class KlasementLigaFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    SmoothProgressBar smoothProgressBar;
    ProgressBar progressBar;
    View view;
    String category;
    Cache_String cache_string;
    List<KlasementItem> feedItemList;
    KlasementAdapter adapterList;
    Call<ResponseBody> call;
    LinearLayout linEmpty;
    SwipeRefreshLayout refreshLayout;

    public static KlasementLigaFragment newInstance(String category) {

        Bundle args = new Bundle();
        args.putString("category", category);
        KlasementLigaFragment fragment = new KlasementLigaFragment();
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_klasement, container, false);
        initUi();
        SharedPreferences.Editor editor = cache_string.sharedpreferences.edit();
        editor.putString(cache_string.datesave, "");
        editor.commit();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        feedItemList = new ArrayList<>();
        adapterList = new KlasementAdapter(getActivity(), feedItemList);
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

        feedItemList.clear();
        String url = setCategory();
        RestClient.ApiInterface service = RestClient.getClient(getActivity());
        Call<ResponseBody> call = service.getData(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        refreshLayout.setRefreshing(false);
                        String resRest = response.body().string();
                        JSONObject jsonObject = new JSONObject(resRest);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int a = 0; a < jsonArray.length(); a++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(a);
                            String team = jsonObject1.getString("clubname");
                            String player = jsonObject1.getString("play");
                            String point = jsonObject1.getString("point");
                            String different = jsonObject1.getString("gd");

                            KlasementItem item = new KlasementItem();
                            item.setDifferent(different);
                            item.setPlayer(player);
                            item.setPoint(point);
                            item.setTeam(team);
                            item.setIdtema("0");
                            item.setPosisi(String.valueOf(a + 1));
                            feedItemList.add(item);
                        }
                        adapterList.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        if (feedItemList.isEmpty() || feedItemList.size() == 0) {
                            linEmpty.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (Exception e) {
                    // Log.e("error", e.getMessage());

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

    private String setCategory() {
        String url = null;
        XXmdk xXmdk = new XXmdk();
        switch (category) {
            case "inggris":
                url = xXmdk.kelasligaingris();
                break;
            case "italia":
                url = xXmdk.kelasligaseria();
                break;
            case "spain":
                url = xXmdk.kelasemenligaspanyol();
                break;
            case "jerman":
                url = xXmdk.kelasligajerman();
                break;
            case "france":
                url = xXmdk.kelasligafrance();
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
            if (refreshLayout.isRefreshing()) {
                getData();
            }
        } catch (Exception e) {

        }
    }
}
