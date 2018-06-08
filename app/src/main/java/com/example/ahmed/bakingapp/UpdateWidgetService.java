package com.example.ahmed.bakingapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.example.ahmed.bakingapp.database.RecipeContract;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class UpdateWidgetService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_UPDATE_WIDGET = "com.example.ahmed.bakingapp.action.UPDATE";
    ArrayList<String> names;

    public UpdateWidgetService() {
        super("UpdateWidgetService");
        names = new ArrayList<>();
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionRecipes(Context context) {
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                handleActionRecipes();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionRecipes() {

        Cursor cursor = getContentResolver().query(RecipeContract.TableColumns.CONTENT_URI,
                null,
                null,
                null,
                RecipeContract.TableColumns._ID);

        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    names.add(cursor.getString(cursor.getColumnIndex(RecipeContract.TableColumns.COLUMN_RECIPE)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widgetListView);
        IngredientsWidget.updateWidget(this, appWidgetManager, appWidgetIds, names);
    }

}
