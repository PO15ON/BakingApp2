package com.example.ahmed.bakingapp.data;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.ahmed.bakingapp.IngredientsWidget;
import com.example.ahmed.bakingapp.R;
import com.example.ahmed.bakingapp.database.RecipeContentProvider;
import com.example.ahmed.bakingapp.database.RecipeContract;
import com.example.ahmed.bakingapp.database.RecipeContract.TableColumns;

import static com.example.ahmed.bakingapp.IngredientsWidget.TAG;

public class GridWidgetService extends RemoteViewsService {
    @Override
    public WidgetRemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteViewsFactory(this.getApplicationContext());
    }
}

class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Cursor cursor;


    public WidgetRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

        Uri uri = TableColumns.CONTENT_URI;
        if(cursor != null) cursor.close();
        cursor = mContext.getContentResolver().query(uri,
                null,
                null,
                null,
                TableColumns._ID);

    }

    @Override
    public void onDestroy() {
        if(cursor != null) cursor.close();
    }

    @Override
    public int getCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {

        if (cursor == null || cursor.getCount() == 0) {
            Log.d("error", "getViewAt: Error");
            return null;
        }

        // FIXME: 04/06/18 
        cursor.moveToPosition(i);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget);
        String text = cursor.getString(cursor.getColumnIndex(TableColumns.COLUMN_RECIPE));
//        Log.d("widget", "getViewAt: text = " + text);
        views.setTextViewText(R.id.widgetItemTaskNameLabel, text);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
