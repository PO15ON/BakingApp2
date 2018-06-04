package com.example.ahmed.bakingapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.ahmed.bakingapp.database.RecipeContract.TableColumns;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "db";
    public static final int VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + TableColumns.TABLE_NAME + " (" +
                TableColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TableColumns.COLUMN_RECIPE + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableColumns.TABLE_NAME);
        onCreate(db);
    }
}
