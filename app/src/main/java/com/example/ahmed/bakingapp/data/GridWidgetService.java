package com.example.ahmed.bakingapp.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.ahmed.bakingapp.R;
import com.example.ahmed.bakingapp.database.RecipeContract.TableColumns;

import java.util.ArrayList;

import static com.example.ahmed.bakingapp.MainActivity.modelListAll;

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

        cursor.moveToPosition(i);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.collection_widget_list_item);
        String text = cursor.getString(cursor.getColumnIndex(TableColumns.COLUMN_RECIPE));
        Log.d("widget", "getViewAt: text = " + text);
        views.setTextViewText(R.id.widgetItemTaskNameLabel, text);

//        Log.d(TAG, "getViewAt: " + modelListAll.contains(text));
//        Log.d(TAG, "getViewAt: modelListAll = " + modelListAll.get(modelListAll.indexOf(text)));
        int size = modelListAll.size();
        String title = null;
        ArrayList<Ingredient> ingredients = null;
        int index = 0;

        for (int in = 0; in < size; in++) {
            if (modelListAll.get(in).getTitle().equals(text)) {
                title = modelListAll.get(in).getTitle();
                ingredients = modelListAll.get(in).getIngredients();
                index = in;

            }
        }

        Intent intent = new Intent();

        intent.putExtra("title", title);
        intent.putExtra("ingredients", ingredients);
        intent.putExtra("index", index);

        views.setOnClickFillInIntent(R.id.widgetItemTaskNameLabel, intent);

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
