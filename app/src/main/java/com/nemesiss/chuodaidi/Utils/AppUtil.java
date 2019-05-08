package com.nemesiss.chuodaidi.Utils;

import android.net.Uri;
import com.nemesiss.chuodaidi.BuildConfig;

public class AppUtil {
    public static final String RESOURCE = "android.resource://";
    public static String packageName = BuildConfig.APPLICATION_ID;

    public static Uri ParseResourceIdToUri(int resId)
    {
        return Uri.parse(RESOURCE + packageName + "/" + resId);
    }
}
