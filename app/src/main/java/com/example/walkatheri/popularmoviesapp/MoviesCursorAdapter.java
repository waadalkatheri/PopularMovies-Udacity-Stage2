package com.example.walkatheri.popularmoviesapp;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.walkatheri.popularmoviesapp.data.Movie;
import com.example.walkatheri.popularmoviesapp.data.MoviesContract;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Adapter class for DB cursor query data
 */
public class MoviesCursorAdapter extends RecyclerView.Adapter<MoviesCursorAdapter.MoviesAdapterViewHolder> {


    private Cursor mCursor;
    private Context mContext;
    private final MoviesCursorOnClickHandler mClickHandler;



    public MoviesCursorAdapter(Context mContext) {
        this.mContext = mContext;
        mClickHandler=(MoviesCursorOnClickHandler)mContext;
        mCursor=Movie.oldCursor;
    }



    /**
     * The interface that provides onclick.
     */
    public interface MoviesCursorOnClickHandler {
        void onClickCursor(Movie movie);
    }


    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_movies;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new MoviesAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MoviesAdapterViewHolder holder, int position) {

        int posterIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER);
       final int titleIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE);
        Log.i("posterIndex", String.valueOf(posterIndex));

        mCursor.moveToPosition(position);

        String posterPath = mCursor.getString(posterIndex);
        final String movieTitle = mCursor.getString(titleIndex);
        Log.i("posterIndex", posterPath);
        holder.mImageView.setVisibility(View.VISIBLE);

        Picasso.with(mContext).load(posterPath.toString()).into(holder.mImageView,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

                Log.i("SUCESSS","IMG sucess");
                holder.mnoImageTextView.setVisibility(View.GONE);


            }

            @Override
            public void onError() {
                holder.mnoImageTextView.setVisibility(View.VISIBLE);
                holder.mImageView.setVisibility(View.GONE);
                holder.mnoImageTextView.setText(movieTitle);
                Log.i("ERROR","IMG error");

            }
        });;

    }


    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public void swapCursor(Cursor c) {
        if (mCursor == c) {
            Log.i("sameCursor","sameCursor");
            return;
        }

       mCursor = c;

        if (c != null) {
            this.notifyDataSetChanged();

        }

    }


    class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public final ImageView mImageView;
        public final TextView mnoImageTextView;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.movie_poster);
            mnoImageTextView = (TextView) itemView.findViewById(R.id.noposter_tittle);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {
            int adapterPosition=getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int titleIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE);
            int posterIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER);
            int ratingIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_USERRATING);
            int overviewIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_SYNOPSIS);
            int dateIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASEDATE);
            int movieIDIndex = mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIEID);
            String movieTitle = mCursor.getString(titleIndex);
            String posterPath = mCursor.getString(posterIndex);
            URL posterurl=null;
            try {
                 posterurl= new URL(posterPath);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            String overview = mCursor.getString(overviewIndex);
            String relsDate = mCursor.getString(dateIndex);
            long rating=(long)mCursor.getInt(ratingIndex);
            long movieID=(long)mCursor.getInt(movieIDIndex);

            Movie movieClicked= new Movie(movieTitle,rating,0,overview,relsDate,posterurl,movieID);
            Log.i("gotMoview",movieTitle);
            mClickHandler.onClickCursor(movieClicked);
        }
    }
}