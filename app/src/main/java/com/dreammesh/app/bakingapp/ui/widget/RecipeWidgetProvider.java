package com.dreammesh.app.bakingapp.ui.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.dreammesh.app.bakingapp.R;
import com.dreammesh.app.bakingapp.data.model.BakingWrapper;
import com.dreammesh.app.bakingapp.util.CommonUtil;
import com.dreammesh.app.bakingapp.util.PrefManager;
import com.google.gson.Gson;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

        String recipeString = PrefManager.getSavedRecipe(context);
        BakingWrapper bakingWrapper = new Gson().fromJson(recipeString, BakingWrapper.class);

        if(bakingWrapper == null) {
            views.setTextViewText(R.id.widget_recipe_name, context.getString(R.string.no_view_recipe));
        } else {
            CharSequence recipeName = bakingWrapper.getName();
            CharSequence recipeServings = String.format(context.getString(R.string.servings_format),
                    bakingWrapper.getServings());
            CharSequence recipeIngredients = CommonUtil.getStringIngredients(context,
                    bakingWrapper.getIngredients());

            views.setTextViewText(R.id.widget_recipe_name, recipeName);

            views.setTextViewText(R.id.widget_recipe_servings, recipeServings);

            views.setTextViewText(R.id.widget_recipe_ingredients, recipeIngredients);
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if(AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())){

            Bundle extras = intent.getExtras();
            if(extras != null) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                ComponentName thisAppWidget = new ComponentName(context.getPackageName(),
                        RecipeWidgetProvider.class.getName());
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

                onUpdate(context, appWidgetManager, appWidgetIds);
            }
        }
    }
}