package com.dreammesh.app.bakingapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.File;

/**
 * Created by Jedidiah on 12/06/2017.
 */

public class CommonUtil {

    public static String RECIPE_KEY = "recipe";
    public static String RECIPE_FRAGMENT_KEY = "recipeItem";

    public static String RECIPE_STEP_ITEM_KEY = "step";
    public static String STEP_FRAGMENT_KEY = "stepItem";

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static File getVideoCacheDir(Context context) {
        return new File(context.getExternalCacheDir(), "video-cache");
    }
}
