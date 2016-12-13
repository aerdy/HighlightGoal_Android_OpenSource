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
import android.widget.ProgressBar;

import com.necisstudio.highlightgoal.R;
import com.necisstudio.highlightgoal.adapter.TeamChampionsAdapter;
import com.necisstudio.highlightgoal.item.TeamChampion;
import com.necisstudio.highlightgoal.manage.Cache_String;
import com.necisstudio.highlightgoal.utils.klasemen.TeamChampionsUtils;
import com.necisstudio.highlightgoal.utils.klasemen.TeamEuropaUtils;

import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * Created by Jarod on 2015-10-21.
 */
public class TeamEuropaFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    RecyclerView recyclerView;
    SmoothProgressBar smoothProgressBar;
    ProgressBar progressBar;
    View view;
    SwipeRefreshLayout refreshLayout;
    List<TeamChampion> feedItemList;
    TeamChampionsAdapter adapterList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_klasement,container,false);
        initUi();
        Cache_String cache_string = new Cache_String();

        cache_string.sharedpreferences = getActivity().getSharedPreferences(cache_string.MyPREFERENCES,
                getActivity().getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = cache_string.sharedpreferences.edit();
        editor.putString(cache_string.datesave, "");
        editor.commit();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        feedItemList = new ArrayList<>();
        adapterList = new TeamChampionsAdapter(getActivity(), feedItemList);
        recyclerView.setAdapter(adapterList);

        return view;
    }
    void initUi() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar)view.findViewById(R.id.progressbar);
        smoothProgressBar = (SmoothProgressBar)view.findViewById(R.id.progressbarbottom);
        refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        try{
            if(refreshLayout.isRefreshing()){
                feedItemList.clear();
                new TeamEuropaUtils(getActivity(),feedItemList,adapterList,progressBar);
                refreshLayout.setRefreshing(false);
            }
        }catch (Exception e){

        }
    }
}
