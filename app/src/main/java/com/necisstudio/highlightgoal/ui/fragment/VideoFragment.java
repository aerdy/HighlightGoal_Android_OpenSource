package com.necisstudio.highlightgoal.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.necisstudio.highlightgoal.MainActivity;
import com.necisstudio.highlightgoal.R;
import com.necisstudio.highlightgoal.adapter.VideoAdapter;
import com.necisstudio.highlightgoal.item.VideoItem;
import com.necisstudio.highlightgoal.manage.ApplicationConfig;
import com.necisstudio.highlightgoal.manipulation.RecyclerItemClickListener;
import com.necisstudio.highlightgoal.network.RestClientYoutube;
import com.necisstudio.highlightgoal.network.xxmdk.XXmdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Jarcode on 2016-03-06.
 */
public class VideoFragment extends BottomSheetDialogFragment {
    View view;
    InterstitialAd mInterstitialAd  = ApplicationConfig.mInterstitialAd;
    Call<ResponseBody> call;
    String response;
    ProgressBar progressBar;
    LinearLayout notfound;
    List<VideoItem> feedItemList;
    VideoAdapter adapterList;
    AdView adsNative;
    public static final VideoFragment newInstance(String response) {
        VideoFragment f = new VideoFragment();
        Bundle args = new Bundle();
        args.putString("response", response);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        response = getArguments().getString("response",null);
        requestNewInterstitial();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video,container,false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(new SlideInLeftAnimator(new OvershootInterpolator(1f)));
        recyclerView.getItemAnimator().setAddDuration(700);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        notfound = (LinearLayout)view.findViewById(R.id.notfound);
        adsNative = (AdView)view.findViewById(R.id.adView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        feedItemList = new ArrayList<>();
        adapterList = new VideoAdapter(getActivity(), feedItemList);
        recyclerView.setAdapter(adapterList);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ((MainActivity)getActivity()).playVideo(feedItemList.get(position).getIdvideo());
                    }
                })
        );

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchData(response, progressBar, feedItemList, adapterList, notfound);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(Settings.Secure.getString(getActivity().getContentResolver(),
                        Settings.Secure.ANDROID_ID)+"HighlightGoal").build();
        adsNative.loadAd(adRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1){
            mInterstitialAd.show();
        }
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(Settings.Secure.getString(getActivity().getContentResolver(),
                        Settings.Secure.ANDROID_ID)+"HighlightGoal").build();
        mInterstitialAd.loadAd(adRequest);
    }
    void searchData(String data, final ProgressBar progressBar, final List<VideoItem> feedItemList, final VideoAdapter adapter,final LinearLayout notfound) {
        RestClientYoutube.ApiInterface service = RestClientYoutube.getClient(getActivity());
        call = service.getData(new XXmdk().searchyoutube(data));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String resRest = response.body().string();
                        JSONObject jsonObject = new JSONObject(resRest);
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
                            adapter.add(item,a);
                            if(a == 0){
                                progressBar.setVisibility(View.GONE);

                            }
                        }

                        if(feedItemList.size() == 0){
                            notfound.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (Exception e) {
                    // Log.e("error", e.getMessage());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call,Throwable t) {
                try {
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(getActivity(), "Connection Timeout", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Check your Connection", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            call.cancel();
        }catch (Exception e){

        }
    }
}
