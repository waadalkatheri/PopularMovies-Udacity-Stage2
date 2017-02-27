package com.example.walkatheri.popularmoviesapp.utilities;
import android.content.Context;
import android.util.Log;

import com.example.walkatheri.popularmoviesapp.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MovieJSONUtils {



    static ArrayList<Movie> movieArryList =null;

   private final static String MOVIES_LIST = "results";


    private final static String POSTER_PATH = "poster_path";
    private final static String MOVIE_OVERVIEW = "overview";
    private final static String MOVIE_RELEASE_DATE = "release_date";
    private final static String MOVIE_TITLE = "original_title";
    private final static String MOVIE_POPULARITY = "popularity";
    private final static String MOVIE_RATING = "vote_average";
    private final static String MOVIE_ID = "id";

    public static ArrayList<Movie> getMoviesFromJSON(Context context, String moviesJSON)
            throws JSONException {
        movieArryList=new ArrayList<Movie>();
        JSONObject movieObject =new JSONObject(moviesJSON);
        JSONArray resultArray= movieObject.getJSONArray(MOVIES_LIST);
        Log.v("JSON GOTTTT",resultArray.toString());
        int resultLength=resultArray.length();



        for (int iter=0; iter<resultLength; iter++) {
            JSONObject movie = resultArray.getJSONObject(iter);


            String title = movie.getString(MOVIE_TITLE);

            long rating = movie.getLong(MOVIE_RATING);

            long popularity = movie.getLong(MOVIE_POPULARITY);
            String overview = movie.getString(MOVIE_OVERVIEW);


            String releaseDate = movie.getString(MOVIE_RELEASE_DATE);

            String posterPath = movie.getString(POSTER_PATH);

            URL moviePosterUrl = NetworkUtils.buildImageURL(posterPath);

            long movieID = movie.getLong(MOVIE_ID);




            movieArryList.add( new Movie(title, rating, popularity, overview, releaseDate, moviePosterUrl,movieID));
        }

        return movieArryList;
    }



}
