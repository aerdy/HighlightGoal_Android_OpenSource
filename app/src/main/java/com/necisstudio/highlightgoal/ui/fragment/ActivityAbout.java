package com.necisstudio.highlightgoal.ui.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.necisstudio.highlightgoal.R;
import com.necisstudio.highlightgoal.manage.VersionName;

/**
 * Created by Jarod on 2015-10-23.
 */
public class ActivityAbout extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("About");
        toolbar.setLogo(R.mipmap.ic_back);
        setSupportActionBar(toolbar);
        final TextView version = (TextView)findViewById(R.id.version);
        version.setText("version " + new VersionName().getVersionName(this));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
