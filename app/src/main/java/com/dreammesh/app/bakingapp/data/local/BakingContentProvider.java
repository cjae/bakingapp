package com.dreammesh.app.bakingapp.data.local;


import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Jedidiah on 02/05/2017.
 */

@ContentProvider(authority = BakingContentProvider.AUTHORITY, database = BakingDatabase.class)
public class BakingContentProvider {

    static final String AUTHORITY = "com.dreammesh.app.bakingapp.data.local.BakingContentProvider";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    private static Uri buildUri(String...paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = BakingDatabase.BAKING)
    public static class Baking {

        private static final String PATH = "baking";

        @ContentUri(
                path = PATH,
                type = "vnd.android.cursor.dir/baking",
                defaultSort = BakingColumns.COLUMN_MOVIE_TITLE + " ASC")
        public static final Uri CONTENT_URI = buildUri(PATH);

        @InexactContentUri(
                name = BakingColumns.COLUMN_MOVIE_ID,
                path = PATH + "/#",
                type = "vnd.android.cursor.item/baking",
                whereColumn = BakingColumns.COLUMN_MOVIE_ID,
                pathSegment = 1)
        public static Uri CONTENT_URI_WITH_ID(String id) {
            return buildUri(PATH, id);
        }
    }
}
