package com.example.ahmed.bakingapp.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.ExecOnCreate;
import net.simonvt.schematic.annotation.OnConfigure;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

@net.simonvt.schematic.annotation.Database(version = Database.VERSION,
        packageName = "com.example.ahmed.bakingapp")
public final class Database {

    public static final int VERSION = 1;
    @Table(Columns.class)
    public static final String TABLE = "table";
    @ExecOnCreate
    public static final String EXEC_ON_CREATE = "SELECT * FROM " + TABLE;

    private Database() {
    }

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
    }

    @OnConfigure
    public static void onConfigure(SQLiteDatabase db) {
    }

}
