package com.dreammesh.app.bakingapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by osagieomon on 6/23/17.
 */

public class PrefManager {

    private static String SAVED_RECIPE = "isLogin";

    public static String getSavedRecipe(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(SAVED_RECIPE, "");
    }

    public static void setSavedRecipe(Context context, String recipeString) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SAVED_RECIPE, recipeString);
        editor.apply();
    }
}
