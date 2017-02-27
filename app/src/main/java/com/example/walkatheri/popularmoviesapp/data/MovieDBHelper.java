package com.example.walkatheri.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MovieDBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "movies.db";
    public static final int DBVERSION = 2;

    public MovieDBHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_STATEMENT = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +


                MoviesContract.MoviesEntry._ID + " INTEGER, " +

                MoviesContract.MoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +

                MoviesContract.MoviesEntry.COLUMN_POSTER + " TEXT NOT NULL," +

                MoviesContract.MoviesEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_USERRATING + " INTEGER NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIEID + " INTEGER PRIMARY KEY NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_FAVOURITE + " INTEGER NOT NULL DEFAULT 0, " +
                MoviesContract.MoviesEntry.COLUMN_RELEASEDATE + " TEXT NOT NULL)";

        sqLiteDatabase.execSQL(CREATE_STATEMENT);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
