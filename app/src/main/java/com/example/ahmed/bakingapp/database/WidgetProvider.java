package com.example.ahmed.bakingapp.database;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import static com.example.ahmed.bakingapp.database.WidgetProvider.AUTHORITY;

@ContentProvider(authority = AUTHORITY,
        database = Database.class,
        packageName = "com.example.ahmed.bakingapp")
public final class WidgetProvider {


    public static final String AUTHORITY = "com.example.ahmed.bakingapp.database.WidgetProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public WidgetProvider() {

    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = Database.TABLE)
    public static class Table {
        @ContentUri(
                path = Database.TABLE,
                type = "vnd.android.cursor.dir/table",
                defaultSort = Columns._ID)
        public static final Uri CONTENT_URI = buildUri(Database.TABLE);

        @InexactContentUri(
                path = Database.TABLE + "/#",
                name = "TABLE_ID",
                type = "vnd.android.cursor.item/table",
                whereColumn = Columns._ID,
                pathSegment = 1
        )
        public static Uri withId(long id) {
            return buildUri(Database.TABLE, String.valueOf(id));
        }
    }

}
