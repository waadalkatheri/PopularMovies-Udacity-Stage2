package com.example.walkatheri.popularmoviesapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.walkatheri.popularmoviesapp.data.MoviePrefernces;
import com.example.walkatheri.popularmoviesapp.data.MoviesContract;
import com.example.walkatheri.popularmoviesapp.data.TrailerReview;
import com.example.walkatheri.popularmoviesapp.utilities.NetworkUtils;
import com.example.walkatheri.popularmoviesapp.utilities.TrailerReviewJSONUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class MovieDetails extends AppCompatActivity implements NetworkUtils.onResponseHandler {
    private ImageView imageView;
    private ImageView imageViewPoster;
    private TextView textViewReleaseDt;
    private TextView textViewTitle;
    private TextView textViewRating;
    private TextView textViewOverview;
    private ListView listViewTrailers;
    private ListView listViewReviews;
    private FloatingActionButton favoriteButton;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private long movieId;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    public static boolean favoriteChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        textViewReleaseDt = (TextView) findViewById(R.id.movie_releasedt);

        favoriteButton = (FloatingActionButton) findViewById((R.id.favoritebutn));


        textViewRating = (TextView) findViewById(R.id.rating);
        textViewOverview = (TextView) findViewById(R.id.movie_overview);

        imageView = (ImageView) findViewById(R.id.bigPoster);
        imageViewPoster = (ImageView) findViewById(R.id.movie_img);

        trailerAdapter = new TrailerAdapter(this, 0, null);
        reviewAdapter = new ReviewAdapter(this, 0, null);


        listViewTrailers = (ListView) findViewById(R.id.trailersListview);

        listViewTrailers.setAdapter(trailerAdapter);

        listViewTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int iposition, long l) {
                TrailerReview trailer = (TrailerReview) adapterView.getItemAtPosition(iposition);
                Log.i("trailerGotonclick", trailer.getTrailerurl());
                runTrailer(trailer.getTrailerurl());

            }
        });



        listViewReviews = (ListView) findViewById(R.id.reviewsListView);
        listViewReviews.setAdapter(reviewAdapter);


        Intent intent = getIntent();
        final String title = intent.getStringExtra("title");
        final String releaseDate = intent.getStringExtra("releasedate");
        final Long rating = intent.getLongExtra("rating", 0);
        final String overview = intent.getStringExtra("overview");
        final String posterpath = intent.getStringExtra("posterpath");


        if (intent.hasExtra("movieID")) {
            movieId = intent.getLongExtra("movieID", 0);
            String prefernce = new MoviePrefernces(this).getMoviePrfrnce();
            if (prefernce.equals(MainActivity.FAVORITEMOVIES)) {
                favoriteButton.setImageResource(R.drawable.ic_favorite_white_24dp);
                favoriteButton.setTag(R.id.favouritemovie, R.drawable.ic_favorite_white_24dp);
            } else {
                new checkMovieFavorite().execute(movieId);
            }
            URL reviewtrailerURL = NetworkUtils.buildReviewTrailerURL(movieId, MainActivity.APIKEY);

            NetworkUtils.getResponseUsingVolley(reviewtrailerURL.toString(), this);
            Log.i("MovieId", String.valueOf(movieId));

        }
        if (title != null) {
            collapsingToolbarLayout.setTitle(title);
        }
        if (releaseDate != null) {
            textViewReleaseDt.setText(releaseDate.substring(0, 4));
        }
        if (rating != null) {
            textViewRating.setText(String.valueOf(rating) + "/10");
        }
        if (posterpath != null) {
            Picasso.with(this).load(posterpath).into(imageView);
            Picasso.with(this).load(posterpath).into(imageViewPoster);


        }
        if (overview != null) {
            textViewOverview.setText(overview);

        }

        /*
        click event on favorite button to insert and delete into dB
         */

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int currentImage = (Integer) favoriteButton.getTag(R.id.favouritemovie);
                favoriteChanged = true;
                if (currentImage == R.drawable.ic_favorite_white_24dp) {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    favoriteButton.setTag(R.id.favouritemovie, R.drawable.ic_favorite_border_white_24dp);
                    deletetMoviefromFAvorite(movieId);

                } else {
                    favoriteButton.setImageResource(R.drawable.ic_favorite_white_24dp);
                    favoriteButton.setTag(R.id.favouritemovie, R.drawable.ic_favorite_white_24dp);
                    insertMovietoFAvorite(title, posterpath, overview, rating, releaseDate, 0, movieId);

                }
            }
        });
    }

    @Override
    public void onResponse(String response) {
        if (response == null) {
            return;
        } else {
            ArrayList<TrailerReview> trailerlist = TrailerReviewJSONUtils.getTrailersFromJSON(response);
            trailerAdapter.setTrailerlist(trailerlist);
            if(trailerlist.size()>0) {
                setListViewHeightBasedOnChildren(listViewTrailers);
            }
            ArrayList<TrailerReview> reviewlist = TrailerReviewJSONUtils.getReviewsFromJSON(response);
            reviewAdapter.setReviewlist(reviewlist);
            if(reviewlist.size()>0) {

                setListViewHeightBasedOnChildren(listViewReviews);
            }
        }

    }

    /**
     *Check weather movie is added as favorite/not and set image on the basis of same
     */
    public class checkMovieFavorite extends AsyncTask<Long, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Long... longs) {
            long movieId = longs[0];
            Uri movieUri = MoviesContract.MoviesEntry.buildMovieUriWithId(movieId);
            return getContentResolver().query(movieUri, null, MoviesContract.MoviesEntry.COLUMN_MOVIEID, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            int cursorCount = cursor.getCount();
            setFavriteImage(cursorCount);
        }
    }

    private void setFavriteImage(int count) {
        if (count > 0) {
            favoriteButton.setImageResource(R.drawable.ic_favorite_white_24dp);
            favoriteButton.setTag(R.id.favouritemovie, R.drawable.ic_favorite_white_24dp);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_favorite_border_white_24dp);
            favoriteButton.setTag(R.id.favouritemovie, R.drawable.ic_favorite_border_white_24dp);
        }
    }

    /**
     * run trailer in youtube app outside intent
     * @param url youtube url of movie
     */
    public void runTrailer(String url) {
        Intent videoIntent = (new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        startActivity(videoIntent);

    }

    /**
     * share url link
     * @param url
     */

    public void shareTrailer(String url) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, url);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void insertMovietoFAvorite(String title, String poster, String synopsis, Long userrating, String releasedate, int favourite, long movieid) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE, title);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER, poster);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_SYNOPSIS, synopsis);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_USERRATING, userrating);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASEDATE, releasedate);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_FAVOURITE, favourite);
        contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIEID, movieid);

        Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, contentValues);


    }

    public void deletetMoviefromFAvorite(long movieID) {
        Uri deleturi = MoviesContract.MoviesEntry.buildMovieUriWithId(movieID);
        getContentResolver().delete(deleturi, null, null);

    }

    /**
     * dynamically set height of listview
     * Hack for having listview in scrollview
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


}
