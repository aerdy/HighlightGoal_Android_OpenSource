package com.necisstudio.highlightgoal;

import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.necisstudio.highlightgoal.adapter.Adapter_ViewPager;
import com.necisstudio.highlightgoal.item.BannerItem;
import com.necisstudio.highlightgoal.manage.ApplicationConfig;
import com.necisstudio.highlightgoal.network.xxmdk.DeveloperKey;
import com.necisstudio.highlightgoal.ui.fragment.ActivityAbout;
import com.necisstudio.highlightgoal.ui.fragment.ActivityLicense;
import com.necisstudio.highlightgoal.ui.fragment.BannerFragment;
import com.necisstudio.highlightgoal.ui.fragment.highlight.HighlightFragment;
import com.necisstudio.highlightgoal.ui.fragment.highlight.HighlightLatestFragment;
import com.necisstudio.highlightgoal.ui.fragment.klasemen.KlasementLigaFragment;
import com.necisstudio.highlightgoal.ui.fragment.klasemen.TeamChampionsFragment;
import com.necisstudio.highlightgoal.ui.fragment.klasemen.TeamEuropaFragment;
import com.necisstudio.highlightgoal.ui.fragment.schedule.ScheduleLigaFragment;
import com.necisstudio.highlightgoal.ui.fragment.schedule.ScheduleLigaLatestFragment;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Adapter_ViewPager adapter_viewPager, adapter_viewPagerBanner;
    List<Fragment> fList, fListBanner;
    ViewPager pagerbanner;
    Toolbar toolbar;
    SearchView searchview;
    private CirclePageIndicator indicator;
    private ProgressBar progressBar;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    int iddrawer = ApplicationConfig.status;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    TabLayout tabHost;
    TextView txtTitle;
    ImageView imgLogo;
    InterstitialAd mInterstitialAd = ApplicationConfig.mInterstitialAd;

    //    AdView adView;
//    AdRequest adRequest;
    //InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fabric.with(this, new Crashlytics());
        firebaseAuth = FirebaseAuth.getInstance();
        fListBanner = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        progressBar = (ProgressBar) findViewById(R.id.progressbarBanner);
        adapter_viewPagerBanner = new Adapter_ViewPager(getSupportFragmentManager(), fListBanner);
        pagerbanner = (ViewPager) findViewById(R.id.viewpagerBanner);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        pagerbanner.setAdapter(adapter_viewPagerBanner);
        indicator.setViewPager(pagerbanner);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        drawermenu(iddrawer);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        autFirebase();
    }


    private List<Fragment> getFragments(Fragment fragment1, Fragment fragment2, Fragment fragment3) {
        if (iddrawer == R.id.latest || iddrawer == 20) {
            fList.add(fragment1);
            fList.add(fragment3);
        } else {
            fList.add(fragment1);
            fList.add(fragment2);
            fList.add(fragment3);
        }
        return fList;
    }

    private void autFirebase() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    getBannerData();
                } else {
                    firebaseAuth.signInWithEmailAndPassword("aerdy4@gmail.com", "highlightgoal4")
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        getBannerData();
                                    }
                                }
                            });
                }

            }
        };

    }

    private void getBannerData() {
        DatabaseReference ref = ApplicationConfig.firebaseDatabase.getReference().child("banner");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fListBanner.clear();
                for (final DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            BannerItem item = postSnapshot.getValue(BannerItem.class);
                            fListBanner.add(BannerFragment.newInstance(item.getId(), item.getTitle(), item.getImage()));
                            adapter_viewPagerBanner.notifyDataSetChanged();
                        }
                    });
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                    finish();
                    return;
                } else {
                    if (iddrawer != R.id.latest) {
                        ApplicationConfig.status = R.id.latest;
                        iddrawer = R.id.latest;
                        imgLogo.setImageResource(0);
                        txtTitle.setText("Latest");
                        fList = new ArrayList<Fragment>();
                        List<Fragment> fragments = getFragments(HighlightLatestFragment.newInstance(""), KlasementLigaFragment.newInstance("inggris"), ScheduleLigaLatestFragment.newInstance(""));
                        adapter_viewPager = new Adapter_ViewPager(getSupportFragmentManager(), fragments);
                        final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
                        tabHost = (TabLayout) findViewById(R.id.materialTabHost);
                        pager.setAdapter(adapter_viewPager);
                        pager.setOffscreenPageLimit(3);
                        drawer.closeDrawer(GravityCompat.START);
                        tabHost.setupWithViewPager(pager);
                        tabHost.getTabAt(0).setText("Highlight");
                        tabHost.getTabAt(1).setText("Schedule");
                    } else {
                        Toast.makeText(getApplicationContext(), "Press again to quit", Toast.LENGTH_SHORT).show();
                    }
                }
                mBackPressed = System.currentTimeMillis();
            } else {
                getSupportFragmentManager().popBackStack();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            drawermenu(iddrawer);
            return true;
        } else if (id == R.id.search) {
            item.expandActionView();
            MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
//                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ApplicationConfig.status = R.id.latest;
                    iddrawer = R.id.latest;
                    imgLogo.setImageResource(0);
                    txtTitle.setText("Latest");
                    fList = new ArrayList<Fragment>();
                    List<Fragment> fragments = getFragments(HighlightLatestFragment.newInstance(""), KlasementLigaFragment.newInstance("inggris"), ScheduleLigaLatestFragment.newInstance(""));
                    adapter_viewPager = new Adapter_ViewPager(getSupportFragmentManager(), fragments);
                    final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
                    tabHost = (TabLayout) findViewById(R.id.materialTabHost);
                    pager.setAdapter(adapter_viewPager);
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    tabHost.setupWithViewPager(pager);
                    tabHost.getTabAt(0).setText("Highlight");
                    tabHost.getTabAt(1).setText("Schedule");
                    return true;
                }
            });

            searchview = (SearchView) MenuItemCompat.getActionView(item);
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchview.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                    );
                    iddrawer = 20;
                    fList = new ArrayList<Fragment>();
                    List<Fragment> fragments = getFragments(HighlightLatestFragment.newInstance(searchview.getQuery().toString()), KlasementLigaFragment.newInstance("inggris"), ScheduleLigaLatestFragment.newInstance(searchview.getQuery().toString()));
                    adapter_viewPager = new Adapter_ViewPager(getSupportFragmentManager(), fragments);
                    final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
                    tabHost = (TabLayout) findViewById(R.id.materialTabHost);
                    pager.setAdapter(adapter_viewPager);
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    tabHost.setupWithViewPager(pager);
                    tabHost.getTabAt(0).setText("Highlight");
                    tabHost.getTabAt(1).setText("Schedule");
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchview.setQuery(query, false);
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );

            iddrawer = 20;
            imgLogo.setImageResource(0);
            txtTitle.setText(searchview.getQuery().toString());
            fList = new ArrayList<Fragment>();
            List<Fragment> fragments = getFragments(HighlightLatestFragment.newInstance(searchview.getQuery().toString()), KlasementLigaFragment.newInstance("inggris"), ScheduleLigaLatestFragment.newInstance(searchview.getQuery().toString()));
            adapter_viewPager = new Adapter_ViewPager(getSupportFragmentManager(), fragments);
            final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
            tabHost = (TabLayout) findViewById(R.id.materialTabHost);
            pager.setAdapter(adapter_viewPager);
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            tabHost.setupWithViewPager(pager);
            tabHost.getTabAt(0).setText("Highlight");
            tabHost.getTabAt(1).setText("Schedule");
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        drawermenu(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    void drawermenu(int id) {
        if (id == R.id.rate) {
            Uri uri = Uri.parse("market://details?id=com.necisstudio.highlightgoal");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=com.necisstudio.highlightgoal")));
            }

        } else if (id == R.id.feedback) {
            Intent send = new Intent(Intent.ACTION_SENDTO);
            String uriText = "mailto:" + Uri.encode("report@necistudio.com") +
                    "?subject=" + Uri.encode("Feedback for Highlight Goal") +
                    "&body=" + Uri.encode("");
            Uri uri = Uri.parse(uriText);
            send.setData(uri);
            startActivity(Intent.createChooser(send, "Send mail..."));
        } else if (id == R.id.latest) {
            ApplicationConfig.status = id;
            iddrawer = id;
            imgLogo.setImageResource(0);
            txtTitle.setText("Latest");
            fList = new ArrayList<Fragment>();
            List<Fragment> fragments = getFragments(HighlightLatestFragment.newInstance(""), KlasementLigaFragment.newInstance("inggris"), ScheduleLigaLatestFragment.newInstance(""));
            adapter_viewPager = new Adapter_ViewPager(getSupportFragmentManager(), fragments);
            final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
            tabHost = (TabLayout) findViewById(R.id.materialTabHost);
            pager.setAdapter(adapter_viewPager);
            pager.setOffscreenPageLimit(3);
            tabHost.setupWithViewPager(pager);
            tabHost.getTabAt(0).setText("Highlight");
            tabHost.getTabAt(1).setText("Schedule");
        } else if (id == R.id.inggris) {
            ApplicationConfig.status = id;
            iddrawer = id;
            imgLogo.setImageResource(R.mipmap.premier);
            txtTitle.setText("Premier League");
            fList = new ArrayList<Fragment>();
            List<Fragment> fragments = getFragments(HighlightFragment.newInstance("inggris"), KlasementLigaFragment.newInstance("inggris"), ScheduleLigaFragment.newInstance("inggris"));
            adapter_viewPager = new Adapter_ViewPager(getSupportFragmentManager(), fragments);
            final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
            tabHost = (TabLayout) findViewById(R.id.materialTabHost);
            pager.setAdapter(adapter_viewPager);
            pager.setOffscreenPageLimit(3);

            tabHost.setupWithViewPager(pager);
            tabHost.getTabAt(0).setText("Highlight");
            tabHost.getTabAt(1).setText("Standings");
            tabHost.getTabAt(2).setText("Schedule");

        } else if (id == R.id.europa) {
            ApplicationConfig.status = id;
            iddrawer = id;
            imgLogo.setImageResource(R.mipmap.europa);
            txtTitle.setText("Europa League");
            fList = new ArrayList<Fragment>();
            List<Fragment> fragments = getFragments(HighlightFragment.newInstance("europa"), new TeamEuropaFragment(), ScheduleLigaFragment.newInstance("europa"));
            adapter_viewPager = new Adapter_ViewPager(getSupportFragmentManager(), fragments);
            final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
            tabHost = (TabLayout) findViewById(R.id.materialTabHost);
            pager.setAdapter(adapter_viewPager);
            pager.setOffscreenPageLimit(3);

            tabHost.setupWithViewPager(pager);
            tabHost.getTabAt(0).setText("Highlight");
            tabHost.getTabAt(1).setText("Standings");
            tabHost.getTabAt(2).setText("Schedule");
        } else if (id == R.id.champion) {
            ApplicationConfig.status = id;
            iddrawer = id;
            imgLogo.setImageResource(R.mipmap.champion);
            txtTitle.setText("Champions League");
            fList = new ArrayList<Fragment>();
            List<Fragment> fragments = getFragments(HighlightFragment.newInstance("champions"), new TeamChampionsFragment(), ScheduleLigaFragment.newInstance("champions"));
            adapter_viewPager = new Adapter_ViewPager(getSupportFragmentManager(), fragments);
            final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
            tabHost = (TabLayout) findViewById(R.id.materialTabHost);
            pager.setAdapter(adapter_viewPager);
            pager.setOffscreenPageLimit(3);

            tabHost.setupWithViewPager(pager);
            tabHost.getTabAt(0).setText("Highlight");
            tabHost.getTabAt(1).setText("Standings");
            tabHost.getTabAt(2).setText("Schedule");
        } else if (id == R.id.seria) {
            ApplicationConfig.status = id;
            iddrawer = id;
            imgLogo.setImageResource(R.mipmap.seria);
            txtTitle.setText("Seri A");
            fList = new ArrayList<Fragment>();
            List<Fragment> fragments = getFragments(HighlightFragment.newInstance("italia"), KlasementLigaFragment.newInstance("italia"), ScheduleLigaFragment.newInstance("italia"));
            adapter_viewPager = new Adapter_ViewPager(getSupportFragmentManager(), fragments);
            final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
            tabHost = (TabLayout) findViewById(R.id.materialTabHost);
            pager.setAdapter(adapter_viewPager);
            pager.setOffscreenPageLimit(3);

            tabHost.setupWithViewPager(pager);
            tabHost.getTabAt(0).setText("Highlight");
            tabHost.getTabAt(1).setText("Standings");
            tabHost.getTabAt(2).setText("Schedule");
        } else if (id == R.id.jerman) {
            ApplicationConfig.status = id;
            iddrawer = id;
            imgLogo.setImageResource(R.mipmap.bundes);
            txtTitle.setText("Bundesliga");
            fList = new ArrayList<Fragment>();
            List<Fragment> fragments = getFragments(HighlightFragment.newInstance("jerman"), KlasementLigaFragment.newInstance("jerman"), ScheduleLigaFragment.newInstance("jerman"));
            adapter_viewPager = new Adapter_ViewPager(getSupportFragmentManager(), fragments);
            final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
            tabHost = (TabLayout) findViewById(R.id.materialTabHost);
            pager.setAdapter(adapter_viewPager);
            pager.setOffscreenPageLimit(3);

            tabHost.setupWithViewPager(pager);
            tabHost.getTabAt(0).setText("Highlight");
            tabHost.getTabAt(1).setText("Standings");
            tabHost.getTabAt(2).setText("Schedule");
        } else if (id == R.id.spain) {
            ApplicationConfig.status = id;
            iddrawer = id;
            imgLogo.setImageResource(R.mipmap.bbva);
            txtTitle.setText("BBVA League");
            fList = new ArrayList<Fragment>();
            List<Fragment> fragments = getFragments(HighlightFragment.newInstance("spain"), KlasementLigaFragment.newInstance("spain"), ScheduleLigaFragment.newInstance("spain"));
            adapter_viewPager = new Adapter_ViewPager(getSupportFragmentManager(), fragments);
            final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
            tabHost = (TabLayout) findViewById(R.id.materialTabHost);
            pager.setAdapter(adapter_viewPager);
            pager.setOffscreenPageLimit(3);

            tabHost.setupWithViewPager(pager);
            tabHost.getTabAt(0).setText("Highlight");
            tabHost.getTabAt(1).setText("Standings");
            tabHost.getTabAt(2).setText("Schedule");
        } else if (id == R.id.france) {
            ApplicationConfig.status = id;
            iddrawer = id;
            imgLogo.setImageResource(R.mipmap.ligue);
            txtTitle.setText("League 1");
            fList = new ArrayList<Fragment>();
            List<Fragment> fragments = getFragments(HighlightFragment.newInstance("france"), KlasementLigaFragment.newInstance("france"), ScheduleLigaFragment.newInstance("france"));
            adapter_viewPager = new Adapter_ViewPager(getSupportFragmentManager(), fragments);
            final ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
            tabHost = (TabLayout) findViewById(R.id.materialTabHost);
            pager.setAdapter(adapter_viewPager);
            pager.setOffscreenPageLimit(3);

            tabHost.setupWithViewPager(pager);
            tabHost.getTabAt(0).setText("Highlight");
            tabHost.getTabAt(1).setText("Standings");
            tabHost.getTabAt(2).setText("Schedule");
        } else if (id == R.id.about) {
            Intent intent = new Intent(MainActivity.this, ActivityAbout.class);
            startActivity(intent);
        } else if (id == R.id.license) {
            Intent intent = new Intent(MainActivity.this, ActivityLicense.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }
    }

    public void playVideo(String idvideo) {
        Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                this, DeveloperKey.DEVELOPER_KEY, idvideo, 0, true, false);
        if (intent != null) {
            if (canResolveIntent(intent)) {
                startActivityForResult(intent, 1);
            } else {
                YouTubeInitializationResult.SERVICE_MISSING
                        .getErrorDialog(this, 2).show();
            }
        }
    }

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

}
