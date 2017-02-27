package com.example.walkatheri.popularmoviesapp.utilities;


import android.util.Log;

import com.example.walkatheri.popularmoviesapp.data.TrailerReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrailerReviewJSONUtils {

    private static ArrayList<TrailerReview> trailerlist =null;

    private final static String TRAILERS = "trailers";
    private final static String TRAILERS_YOUTUBELIST = "youtube";
    private final static String TRAILER_NAME = "name";
    private final static String TRAILER_SOURCE = "source";
    private final static String TRAILER_TYPE = "type";

    private final static String REVIEWS=  "reviews";
    private final static String REVIEW_RESULTLIST="results";
    private final static String REVIEW_AUTHOR="author";
    private final static String REVIEW_CONTENT="content";

    public static ArrayList<TrailerReview> getTrailersFromJSON(String response)
    {
        Log.i("respnse",response);
        trailerlist = new ArrayList<TrailerReview>();

        try {
            JSONObject jsonresponse = new JSONObject(response);
            JSONObject trailers= jsonresponse.getJSONObject(TRAILERS);

            JSONArray trailersArr= trailers.getJSONArray(TRAILERS_YOUTUBELIST);

            int iter=0;
            int arrlength=trailersArr.length();
            for(iter=0;iter<arrlength;iter++)
            {
                JSONObject youtube = trailersArr.getJSONObject(iter);

                String type= youtube.getString(TRAILER_TYPE);
                if(type.equals("Trailer"))
                {
                    String name= youtube.getString(TRAILER_NAME);
                    String source= youtube.getString(TRAILER_SOURCE);
                    String youtubeurl = NetworkUtils.buildYoutubeurl(source);
                    trailerlist.add(new TrailerReview(name,source,youtubeurl));
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  trailerlist;
    }

    public static ArrayList<TrailerReview> getReviewsFromJSON(String response)
    {
        trailerlist = new ArrayList<TrailerReview>();

        try {
            JSONObject jsonresponse = new JSONObject(response);
            JSONObject reviews= jsonresponse.getJSONObject(REVIEWS);

            JSONArray reviewsArr= reviews.getJSONArray(REVIEW_RESULTLIST);

            int iter=0;
            int arrlength=reviewsArr.length();
            for(iter=0;iter<arrlength;iter++)
            {
                JSONObject review = reviewsArr.getJSONObject(iter);

                String author= review.getString(REVIEW_AUTHOR);

                    String content= review.getString(REVIEW_CONTENT);

                    trailerlist.add(new TrailerReview(author,content));


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return  trailerlist;
    }

}
