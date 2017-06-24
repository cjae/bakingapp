package com.dreammesh.app.bakingapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dreammesh.app.bakingapp.data.model.Ingredient;

import java.io.File;
import java.util.List;

/**
 * Created by Jedidiah on 12/06/2017.
 */

public class CommonUtil {

    public static String RECIPE_KEY = "recipe";
    public static String RECIPE_FRAGMENT_KEY = "recipeItem";

    public static String RECIPE_STEP_LIST_KEY = "stepList";
    public static String RECIPE_STEP_ID_KEY = "stepID";
    public static String STEP_FRAGMENT_KEY = "stepItem";

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getStringIngredients(Context context, List<Ingredient> ingredientList) {
        StringBuilder sb = new StringBuilder();

        for (Ingredient ingredient : ingredientList) {

            String name = ingredient.getIngredient();
            float quantity = ingredient.getQuantity();
            String measure = ingredient.getMeasure();

            sb.append("\n");
            sb.append(StringUtil.prepareIngredient(context, name, quantity, measure));
        }

        return sb.toString().trim();
    }
}
