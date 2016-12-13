package com.necisstudio.highlightgoal.manage;

import android.app.Application;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.ads.InterstitialAd;
import java.lang.reflect.Field;
import java.util.Map;

import com.google.firebase.database.FirebaseDatabase;
import com.necisstudio.highlightgoal.R;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by mr on 30/07/16.
 */

public class ApplicationConfig extends Application {
    public static FirebaseDatabase firebaseDatabase;
    public static InterstitialAd mInterstitialAd;
    public static int status;

    @Override
    public void onCreate() {
        super.onCreate();
        status = R.id.latest;
        firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ads_necis));

        Typeface font = Typeface.createFromAsset(this.getResources().getAssets(),"fonts/Roboto-Thin.ttf");
        injectTypeface("Roboto-Thin", font);

        Typeface font2 = Typeface.createFromAsset(this.getResources().getAssets(),"fonts/Roboto-Bold.ttf");
        injectTypeface("Roboto-Bold", font2);

        Typeface font3 = Typeface.createFromAsset(this.getResources().getAssets(),"fonts/RobotoSlab-Regular.ttf");
        injectTypeface("Roboto-Slab", font3);
    }
    private boolean injectTypeface(String fontFamily, Typeface typeface)
    {
        try
        {
            Field field = Typeface.class.getDeclaredField("sSystemFontMap");
            field.setAccessible(true);
            Object fieldValue = field.get(null);
            Map<String, Typeface> map = (Map<String, Typeface>) fieldValue;
            map.put(fontFamily, typeface);
            return true;
        }
        catch (Exception e)
        {
            Log.e("Font-Injection", "Failed to inject typeface.", e);
        }
        return false;
    }

}
