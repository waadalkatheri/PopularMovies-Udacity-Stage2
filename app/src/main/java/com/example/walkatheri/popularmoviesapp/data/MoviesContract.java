package com.example.walkatheri.popularmoviesapp.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviesapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_NAME="movies";
    public static final class MoviesEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "movies";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME)
                .build();


        public static final String COLUMN_TITLE="title";

        public static final String COLUMN_POSTER="poster";
        public static final String COLUMN_SYNOPSIS="synopsis";
        public static final String COLUMN_USERRATING="userrating";
        public static final String COLUMN_RELEASEDATE="releasedate";
        //column to be used if store all data in future, update
        public static final String COLUMN_FAVOURITE="favourite";
        public static final String COLUMN_MOVIEID="movieid";


        public static Uri buildMovieUriWithId(long id) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(id))
                    .build();
        }

    }


}
