package com.necisstudio.highlightgoal.manage;

import android.content.Context;
import android.os.Build;

/**
 * Created by jarod on 8/8/15.
 */
public class VersionName {
    public String getVersionName(Context context) {
        String versi = "";
        try {
            versi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            return versi;
        } catch (Exception e) {

        }
        return versi;
    }
}
