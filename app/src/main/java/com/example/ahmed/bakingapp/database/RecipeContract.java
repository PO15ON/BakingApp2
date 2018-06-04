package com.example.ahmed.bakingapp.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class RecipeContract {

    public static final String AUTHORITY = "com.example.ahmed.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String RECIPE_PATH = TableColumns.TABLE_NAME;

    public static final class TableColumns implements BaseColumns {

        public static final String TABLE_NAME = "recipes";
        public static final String COLUMN_RECIPE = "recipe";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(RECIPE_PATH).build();
    }

}
