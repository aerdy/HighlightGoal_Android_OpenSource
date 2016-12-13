package com.necisstudio.highlightgoal.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.necisstudio.highlightgoal.MainActivity;
import com.necisstudio.highlightgoal.R;
import com.necisstudio.highlightgoal.manage.ApplicationConfig;

/**
 * Created by mr on 30/07/16.
 */

public class BannerFragment extends Fragment {
    private String id,title,image;
    InterstitialAd mInterstitialAd  = ApplicationConfig.mInterstitialAd;
    ImageView imageViewVideo;
    public static BannerFragment newInstance(String id,String title,String image) {
        Bundle args = new Bundle();
        args.putString("id",id);
        args.putString("title",title);
        args.putString("image",image);
        BannerFragment fragment = new BannerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString("id");
        title = getArguments().getString("title");
        image = getArguments().getString("image");
        requestNewInterstitial();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_banner,container,false);
        imageViewVideo = (ImageView)view.findViewById(R.id.imageViewVideo);
        TextView textViewVideo = (TextView) view.findViewById(R.id.textViewVideo);
        RelativeLayout relativeMain = (RelativeLayout) view.findViewById(R.id.relativemain);
        textViewVideo.setText(title);

        relativeMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).playVideo(id);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Glide.with(getActivity()).load(image).into(imageViewVideo);
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
}
