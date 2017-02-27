package com.example.walkatheri.popularmoviesapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.walkatheri.popularmoviesapp.data.Movie;
import com.example.walkatheri.popularmoviesapp.data.MoviePrefernces;
import com.example.walkatheri.popularmoviesapp.data.MoviesContract;
import com.example.walkatheri.popularmoviesapp.utilities.MovieJSONUtils;
import com.example.walkatheri.popularmoviesapp.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesOnClickHandler, MoviesCursorAdapter.MoviesCursorOnClickHandler, NetworkUtils.onResponseHandler, LoaderManager.LoaderCallbacks<Cursor> {


    private final static String PREFERENCEONE = "popular";
    private final static String PREFERENCETWO = "top_rated";
    public final static String FAVORITEMOVIES = "favorite";
    private final static int LOADER_ID = 0;
    private MoviePrefernces movieprefernce;
    private RecyclerView mRecyclerView;


    private TextView mErrorMessageView;

    private LoaderManager loaderManager;

    private ProgressBar mLoadingIndicator;
    private MoviesAdapter moviesAdapter;
    private MoviesCursorAdapter moviesCursorAdapter;
    private ArrayList<Movie> movieArrayList;
    public static String APIKEY;


    /**
     * creates the activity first screen that will appear on launch
     * initialize reference to all the views in main activity layout to refer later in the code
     * assigns GridLayoutManager to recyclerview with 2 cloumns
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        APIKEY = BuildConfig.MOVIEDB_API_KEY;
        moviesAdapter = new MoviesAdapter(this);
        moviesCursorAdapter = new MoviesCursorAdapter(this);

        movieprefernce = new MoviePrefernces(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        mRecyclerView.setAdapter(moviesAdapter);

       // GridLayoutManager layoutManager= new GridLayoutManager(this, 2);

//        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);


        mErrorMessageView = (TextView) findViewById(R.id.error_msg);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);
        /**
         * change the no. of movies column view on orientation
         */

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        else{
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }


        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            Log.i("savestate", "null");
            String prefernce = movieprefernce.getMoviePrfrnce();
            if (prefernce.equals(FAVORITEMOVIES)) {
                mRecyclerView.setAdapter(moviesCursorAdapter);
                getSupportLoaderManager().initLoader(LOADER_ID, null, MainActivity.this);
            } else {
                URL movieUrl = NetworkUtils.buildApiUrl(prefernce, MainActivity.APIKEY);

                loadMoviesData(movieUrl.toString());
            }
        } else {
            Log.i("savestate", "Notnull");
            movieArrayList = savedInstanceState.getParcelableArrayList("movies");
            if (movieArrayList != null) {
                if (movieArrayList.size() == 0) {
                    String prefernce = movieprefernce.getMoviePrfrnce();

                    if (prefernce.equals(FAVORITEMOVIES)) {
                        mRecyclerView.setAdapter(moviesCursorAdapter);
                        getSupportLoaderManager().initLoader(LOADER_ID, null, MainActivity.this);

                    } else {

                        URL movieUrl = NetworkUtils.buildApiUrl(prefernce, MainActivity.APIKEY);

                        loadMoviesData(movieUrl.toString());
                    }

                } else {
                    moviesAdapter.setMoviesData(movieArrayList);
                }
            }

        }


    }

    @Override
    protected void onPause() {
        Log.i("onpause", "onpause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("inResume", "inResume");
        String prefernce = movieprefernce.getMoviePrfrnce();
        if (prefernce.equals(FAVORITEMOVIES) && MovieDetails.favoriteChanged) {
            Log.i("loaderrestated", "resumeloader resarted");
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("Maniactivity", "on save instance called");
        String prefernce = movieprefernce.getMoviePrfrnce();
        // not required for cursor as already handled by loaderCallback
        if (prefernce.equals(FAVORITEMOVIES)) {
            super.onSaveInstanceState(outState);
            return;
        }
        outState.putParcelableArrayList("movies", movieArrayList);

        super.onSaveInstanceState(outState);
    }

    /**
     * makes a call to movie API
     *
     * @param movieURL api url to be called
     */

    private void loadMoviesData(String movieURL) {
        NetworkUtils.getResponseUsingVolley(movieURL, this);
    }

    /**
     * callback after getting response from making call to API
     *
     * @param response from request by volley to moviedb API
     */

    @Override
    public void onResponse(String response) {
        String msg = getString(R.string.noData);
        try {

            if (response == null) {
                showErrorView(msg);
            } else {
                ArrayList<Movie> lmovieArrayList = MovieJSONUtils.getMoviesFromJSON(MainActivity.this, response);
                movieArrayList = lmovieArrayList;
                moviesAdapter.setMoviesData(lmovieArrayList);
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                if (movieArrayList != null) {
                    showMoviesData();
                } else {
                    showErrorView(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorView(msg);

        }
    }

    /**
     * hides error view if fail to fetch movies
     * shows view when able to fetch movies
     */

    private void showMoviesData() {
        mErrorMessageView.setVisibility(View.INVISIBLE);

        mRecyclerView.setVisibility(View.VISIBLE);

    }


    /**
     * shows error view if fail to fetch movies
     * hides view that will show movies
     *
     * @param message
     */


    private void showErrorView(String message) {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageView.setText(message);
        mErrorMessageView.setVisibility(View.VISIBLE);
        ;
    }

    /**
     * click event on each view of recyclerview
     *
     * @param movie
     */

    @Override
    public void onClick(Movie movie) {
        Log.d("MNIN MOv", movie.toString());
        Intent intent = new Intent(MainActivity.this, MovieDetails.class);
        intent.putExtra("title", movie.getTitle());

        intent.putExtra("rating", movie.getRating());
        intent.putExtra("popularity", movie.getPopularity());
        intent.putExtra("overview", movie.getOverview());
        intent.putExtra("releasedate", movie.getReleaseDate());
        intent.putExtra("posterpath", movie.getPosterPath().toString());
        intent.putExtra("movieID", movie.getMovieId());
        Log.i("mainactivity mivuie id", String.valueOf(movie.getMovieId()));
        MovieDetails.favoriteChanged = false;
        startActivity(intent);
    }

    @Override
    public void onClickCursor(Movie movie) {
        onClick(movie);

    }

    /**
     * getting data from local DB for favourite movies using contentProvider
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(MainActivity.this) {
            Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {

                if (mMovieData == null || MovieDetails.favoriteChanged) {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    Log.i("forceO", "forceLOad");
                    forceLoad();
                    ;
                } else {
                    deliverResult(mMovieData);
                }
            }

            @Override
            protected void onForceLoad() {
                super.onForceLoad();
            }

            @Override
            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }

            @Override
            public Cursor loadInBackground() {

                try {
                    return getContentResolver().query(MoviesContract.MoviesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                } catch (Exception e) {

                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    /**
     * @param loader
     * @param data   response data comes after querying the movies DB
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        int crsrCount = data.getCount();
        if (crsrCount > 0) {
            showMoviesData();
        } else {
            String msg = getString(R.string.noFavorites);
            showErrorView(msg);
        }
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        Movie.oldCursor = data;
        moviesCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    /**
     * Inflate filter options menu
     *
     * @param menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("MANACTIVITY", "createdddddddd menu");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    /**
     * is called everytime menu is created.
     * check the user prefernce and check the right option
     *
     * @param menu
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("MANACTIVITY", "preparecallleddddddddd");

        /**
         * everytime menu is created check for user preference and set same menu to checked
         */

        String menuItemSelected = movieprefernce.getMoviePrfrnce();
        if (menuItemSelected != null) {
            Log.d("MAINACTIVITY", menuItemSelected);
            if (menuItemSelected.equals(PREFERENCEONE)) {
                MenuItem menuitem = menu.findItem(R.id.popular_action);
                menuitem.setChecked(true);

            } else if (menuItemSelected.equals(PREFERENCETWO)) {
                MenuItem menuitem = menu.findItem(R.id.top_rated_action);
                menuitem.setChecked(true);
            } else {
                MenuItem menuitem = menu.findItem(R.id.favourite_action);
                menuitem.setChecked(true);
            }
        } else {
            movieprefernce.setMoviePrfrnce(PREFERENCEONE);


        }
        return true;

    }

    /**
     * handle click event on menu
     *
     * @param item
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.top_rated_action:
                loadTopRatedMovies(PREFERENCETWO);
                return true;
            case R.id.popular_action:
                loadPopularMovies(PREFERENCEONE);
                return true;
            case R.id.favourite_action:
                loadFavoriteMovie(FAVORITEMOVIES);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * set the user preference as per selected option from menu setting.
     * setMoviePrfrnce method defined in MoviepPrefernces class
     *
     * @param preference clicked from menu settings
     */

    public void loadPopularMovies(String preference) {
        Boolean errorVisible = checkViewVisibility(mErrorMessageView);
        Boolean samePreference = movieprefernce.checkSamePreferenceClick(preference);
        if (!samePreference || errorVisible) {
            movieArrayList = null;
            moviesAdapter.setMoviesData(null);

            mRecyclerView.setAdapter(moviesAdapter);

            URL movieUrl = NetworkUtils.buildApiUrl(preference, MainActivity.APIKEY);

            loadMoviesData(movieUrl.toString());
            movieprefernce.setMoviePrfrnce(preference);

        }

    }

    public void loadTopRatedMovies(String preference) {
        Boolean errorVisible = checkViewVisibility(mErrorMessageView);

        Boolean samePreference = movieprefernce.checkSamePreferenceClick(preference);
        if (!samePreference || errorVisible) {
            movieArrayList = null;
            moviesAdapter.setMoviesData(null);
            mRecyclerView.setAdapter(moviesAdapter);

            URL movieUrl = NetworkUtils.buildApiUrl(preference, MainActivity.APIKEY);

            loadMoviesData(movieUrl.toString());


            movieprefernce.setMoviePrfrnce(preference);
        }

    }


    private void loadFavoriteMovie(String preference) {
        Boolean errorVisible = checkViewVisibility(mErrorMessageView);

        Boolean samePreference = movieprefernce.checkSamePreferenceClick(preference);
        if (!samePreference || errorVisible) {
            movieArrayList = null;
            moviesAdapter.setMoviesData(null);
            Bundle bundleForLoader = null;
            mRecyclerView.setAdapter(moviesCursorAdapter);
            loaderManager = getSupportLoaderManager();
            Log.i("forceO", "inittt");

            loaderManager.initLoader(LOADER_ID, bundleForLoader, MainActivity.this);
            movieprefernce.setMoviePrfrnce(preference);
        }

    }

    /**
     * @param v type of view to check
     * @return true if view visible else false
     */

    public boolean checkViewVisibility(View v) {
        int visible = v.getVisibility();
        Log.i("MAINACTIVITY VISIBILITY", String.valueOf(visible));
        if (visible == 4)
            return false;
        else
            return true;
    }


}
