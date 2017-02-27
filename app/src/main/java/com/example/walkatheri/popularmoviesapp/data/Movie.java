package com.example.walkatheri.popularmoviesapp.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.walkatheri.popularmoviesapp.utilities.NetworkUtils;

import java.net.URL;


public class Movie implements Parcelable{
    private String title ;
    private long rating ;
    private long popularity;
    private String overview ;
    private String releaseDate;

    private URL posterPath;

    private long movieId;

    public static Cursor oldCursor=null;

    /**
     * constructor Creates movie object
     * @param title
     * @param rating
     * @param popularity
     * @param overview
     * @param releaseDate
     * @param posterPath
     */

    public Movie(String title, long rating, long popularity, String overview, String releaseDate, URL posterPath,long movieId) {
        this.title = title;
        this.rating = rating;
        this.popularity = popularity;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.movieId = movieId;
    }

    protected Movie(Parcel in) {
        title = in.readString();
        rating = in.readLong();
        popularity = in.readLong();
        overview = in.readString();
        releaseDate = in.readString();
        movieId=in.readLong();
        posterPath= NetworkUtils.buildImageURL(in.readString());
    }



    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeLong(rating);
        parcel.writeLong(popularity);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
        parcel.writeLong(movieId);
        parcel.writeString(posterPath.toString());


    }

    /**
     * Below are the setter and getter methods for all properties of Movie Object type
     */

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String synopsis) {
        this.overview = synopsis;
    }

    public long getPopularity() {
        return popularity;
    }

    public void setPopularity(long popularity) {
        this.popularity = popularity;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public URL getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(URL posterPath) {
        this.posterPath = posterPath;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }



}
