package com.example.ahmed.bakingapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.example.ahmed.bakingapp.data.GridWidgetService;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidget extends AppWidgetProvider {
    public static final String TAG = "widget";
    static ArrayList<AbstractModel> modelList;
    ListView listView;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Log.d(TAG, "updateAppWidget: ");
        modelList = MainActivity.modelListAll;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
//        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
//        views.setRemoteAdapter(R.id.widget_list_view, new Intent(context, GridWidgetService.class));
//
//        Intent appIntent = new Intent(context, DetailsActivity.class);
//        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        views.setPendingIntentTemplate(R.id.widget_list_view, appPendingIntent);

        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widgetListView, intent);

//        String name = modelList.get(0).getTitle();
//        views.setTextViewText(R.id.widget_text_view, name);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        Log.d(TAG, "updateAppWidget: 2");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.d(TAG, "onUpdate: ");

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        Log.d(TAG, "onUpdate: 2");
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}

